package net.crewco.Treasury.managers

import net.crewco.Treasury.models.BankAccount
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class BankDatabase(private val dbFile: File) {
	private lateinit var connection: Connection

	init {
		connect()
	}

	fun connect() {
		if (!dbFile.exists()) {
			dbFile.parentFile.mkdirs()
			dbFile.createNewFile()
		}

		connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.path}")
		connection.createStatement().use { stmt ->
			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS bank_accounts (
                    uuid TEXT PRIMARY KEY,
                    balance REAL,
                    interestEarnedToday REAL,
                    lastInterestTimestamp INTEGER
                );
            """.trimIndent())
		}
	}

	fun saveAccounts(accounts: List<BankAccount>) {
		val sql = """
            INSERT INTO bank_accounts (uuid, balance, interestEarnedToday, lastInterestTimestamp)
            VALUES (?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                balance=excluded.balance,
                interestEarnedToday=excluded.interestEarnedToday,
                lastInterestTimestamp=excluded.lastInterestTimestamp;
        """.trimIndent()

		connection.prepareStatement(sql).use { stmt ->
			for (acc in accounts) {
				stmt.setString(1, acc.owner.toString())
				stmt.setDouble(2, acc.balance)
				stmt.setDouble(3, acc.interestEarnedToday)
				stmt.setLong(4, acc.lastInterestTimestamp)
				stmt.addBatch()
			}
			stmt.executeBatch()
		}
	}

	fun loadAccounts(): List<BankAccount> {
		val accounts = mutableListOf<BankAccount>()
		connection.createStatement().use { stmt ->
			val rs = stmt.executeQuery("SELECT * FROM bank_accounts")
			while (rs.next()) {
				val uuid = UUID.fromString(rs.getString("uuid"))
				val balance = rs.getDouble("balance")
				val earned = rs.getDouble("interestEarnedToday")
				val last = rs.getLong("lastInterestTimestamp")

				accounts.add(BankAccount(uuid, balance, uuid.toString(), earned, last))
			}
		}
		return accounts
	}

	fun disconnect() {
		if (::connection.isInitialized) connection.close()
	}
}