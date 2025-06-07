package net.crewco.Treasury.managers

import net.crewco.Treasury.models.Account
import net.crewco.Treasury.models.AccountType
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class DatabaseManager(private val dbFile: File) {
	private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbFile")

	init {
		if (!dbFile.exists()) dbFile.parentFile.mkdirs()
		connection.createStatement().use { stmt ->
			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS accounts (
                    uuid TEXT PRIMARY KEY,
                    balance REAL NOT NULL,
                    account_type TEXT NOT NULL
                );
            """.trimIndent())

			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT,
                    receiver TEXT,
                    amount REAL,
                    type TEXT,
                    timestamp INTEGER
                );
            """.trimIndent())

			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS account_withdrawals (
                    uuid TEXT PRIMARY KEY,
                    amount REAL NOT NULL,
                    last_reset INTEGER NOT NULL
                );
            """.trimIndent())

			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS business_withdrawals (
                    name TEXT PRIMARY KEY,
                    amount REAL NOT NULL,
                    last_reset INTEGER NOT NULL
                );
            """.trimIndent())
		}
	}

	// --- Account Basics ---

	fun createAccountIfMissing(uuid: UUID) {
		val stmt = connection.prepareStatement("INSERT OR IGNORE INTO accounts (uuid, balance, account_type) VALUES (?, 0.0, 'PLAYER')")
		stmt.setString(1, uuid.toString())
		stmt.executeUpdate()
	}

	fun getBalance(uuid: UUID): Double {
		val stmt = connection.prepareStatement("SELECT balance FROM accounts WHERE uuid = ?")
		stmt.setString(1, uuid.toString())
		val rs = stmt.executeQuery()
		return if (rs.next()) rs.getDouble("balance") else 0.0
	}

	// --- Deposit & Withdraw ---

	fun deposit(uuid: UUID, amount: Double): Boolean {
		createAccountIfMissing(uuid)
		val stmt = connection.prepareStatement("UPDATE accounts SET balance = balance + ? WHERE uuid = ?")
		stmt.setDouble(1, amount)
		stmt.setString(2, uuid.toString())
		return stmt.executeUpdate() > 0
	}

	fun withdraw(uuid: UUID, amount: Double): Boolean {
		val currentBalance = getBalance(uuid)
		if (currentBalance < amount) return false

		val stmt = connection.prepareStatement("UPDATE accounts SET balance = balance - ? WHERE uuid = ?")
		stmt.setDouble(1, amount)
		stmt.setString(2, uuid.toString())
		return stmt.executeUpdate() > 0
	}

	// --- Withdraw Tracking ---

	fun getWithdrawnToday(uuid: UUID): Double {
		val now = System.currentTimeMillis()
		val stmt = connection.prepareStatement("SELECT amount, last_reset FROM account_withdrawals WHERE uuid = ?")
		stmt.setString(1, uuid.toString())
		val rs = stmt.executeQuery()

		if (rs.next()) {
			val lastReset = rs.getLong("last_reset")
			if (now - lastReset > 86_400_000) {
				resetWithdraw(uuid)
				return 0.0
			}
			return rs.getDouble("amount")
		}
		return 0.0
	}

	fun addWithdrawnToday(uuid: UUID, amount: Double) {
		val current = getWithdrawnToday(uuid)
		val newAmount = current + amount
		val now = System.currentTimeMillis()
		val stmt = connection.prepareStatement("""
            INSERT INTO account_withdrawals (uuid, amount, last_reset)
            VALUES (?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET amount = excluded.amount, last_reset = excluded.last_reset;
        """)
		stmt.setString(1, uuid.toString())
		stmt.setDouble(2, newAmount)
		stmt.setLong(3, now)
		stmt.executeUpdate()
	}

	fun resetWithdraw(uuid: UUID) {
		val stmt = connection.prepareStatement("""
            INSERT INTO account_withdrawals (uuid, amount, last_reset)
            VALUES (?, 0, ?)
            ON CONFLICT(uuid) DO UPDATE SET amount = 0, last_reset = excluded.last_reset;
        """)
		stmt.setString(1, uuid.toString())
		stmt.setLong(2, System.currentTimeMillis())
		stmt.executeUpdate()
	}

	fun getAccount(uuid: UUID): Account? {
		val stmt = connection.prepareStatement("SELECT balance, account_type FROM accounts WHERE uuid = ?")
		stmt.setString(1, uuid.toString())
		val rs = stmt.executeQuery()

		return if (rs.next()) {
			val balance = rs.getDouble("balance")
			val type = AccountType.valueOf(rs.getString("account_type"))
			Account(uuid, balance, type)
		} else {
			null
		}
	}

}
