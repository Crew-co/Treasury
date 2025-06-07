// AccountManager.kt
package net.crewco.Treasury.managers

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.UUID

class AccountsDataBase(private val dbPath: File) {

	private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")

	init {
		connection.createStatement().use {
			it.executeUpdate("""
                CREATE TABLE IF NOT EXISTS account_withdrawals (
                    uuid TEXT PRIMARY KEY,
                    amount REAL NOT NULL,
                    last_reset INTEGER NOT NULL
                );
            """)
			it.executeUpdate("""
                CREATE TABLE IF NOT EXISTS business_withdrawals (
                    name TEXT PRIMARY KEY,
                    amount REAL NOT NULL,
                    last_reset INTEGER NOT NULL
                );
            """)
		}
	}

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

	fun getBusinessWithdrawnToday(business: String): Double {
		val now = System.currentTimeMillis()
		val stmt = connection.prepareStatement("SELECT amount, last_reset FROM business_withdrawals WHERE name = ?")
		stmt.setString(1, business)
		val rs = stmt.executeQuery()

		if (rs.next()) {
			val lastReset = rs.getLong("last_reset")
			if (now - lastReset > 86_400_000) {
				resetBusinessWithdraw(business)
				return 0.0
			}
			return rs.getDouble("amount")
		}
		return 0.0
	}

	fun addBusinessWithdrawnToday(business: String, amount: Double) {
		val current = getBusinessWithdrawnToday(business)
		val newAmount = current + amount
		val now = System.currentTimeMillis()
		val stmt = connection.prepareStatement("""
            INSERT INTO business_withdrawals (name, amount, last_reset)
            VALUES (?, ?, ?)
            ON CONFLICT(name) DO UPDATE SET amount = excluded.amount, last_reset = excluded.last_reset;
        """)
		stmt.setString(1, business)
		stmt.setDouble(2, newAmount)
		stmt.setLong(3, now)
		stmt.executeUpdate()
	}

	fun resetBusinessWithdraw(business: String) {
		val stmt = connection.prepareStatement("""
            INSERT INTO business_withdrawals (name, amount, last_reset)
            VALUES (?, 0, ?)
            ON CONFLICT(name) DO UPDATE SET amount = 0, last_reset = excluded.last_reset;
        """)
		stmt.setString(1, business)
		stmt.setLong(2, System.currentTimeMillis())
		stmt.executeUpdate()
	}
}
