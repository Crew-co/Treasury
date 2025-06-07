package net.crewco.Treasury.managers

import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*
import net.crewco.Treasury.models.Business

class BusinessDatabase(private val dbFile: File) {
	private lateinit var connection: Connection

	init {
		connect()
	}

	private fun connect() {
		if (!dbFile.exists()) {
			dbFile.parentFile.mkdirs()
			dbFile.createNewFile()
		}

		connection = DriverManager.getConnection("jdbc:sqlite:${dbFile.path}")
		connection.createStatement().use { stmt ->
			stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS businesses (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    owner TEXT,
                    members TEXT,
                    balance REAL,
					transactions TEXT
                );
            """.trimIndent())
		}
	}

	fun saveBusinesses(list: List<Business>) {
		val sql = """
            INSERT INTO businesses (id, name, owner, members, balance)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(id) DO UPDATE SET
                name=excluded.name,
                owner=excluded.owner,
                members=excluded.members,
                balance=excluded.balance;
        """.trimIndent()

		connection.prepareStatement(sql).use { stmt ->
			for (biz in list) {
				stmt.setString(1, biz.id)
				stmt.setString(2, biz.name)
				stmt.setString(3, biz.owner.toString())
				stmt.setString(4, biz.members.joinToString(",") { it.toString() })
				stmt.setDouble(5, biz.balance)
				stmt.addBatch()
				stmt.setString(6, biz.transactions.joinToString("|"))
			}
			stmt.executeBatch()
		}

	}

	fun loadBusinesses(): List<Business> {
		val list = mutableListOf<Business>()
		connection.createStatement().use { stmt ->
			val rs = stmt.executeQuery("SELECT * FROM businesses")
			while (rs.next()) {
				val id = rs.getString("id")
				val name = rs.getString("name")
				val owner = UUID.fromString(rs.getString("owner"))
				val members = rs.getString("members")
					.split(",")
					.filter { it.isNotBlank() }
					.map { UUID.fromString(it) }
					.toMutableSet()
				val balance = rs.getDouble("balance")
				val transactions = rs.getString("transactions")?.split("|")?.toMutableList() ?: mutableListOf()

				list.add(Business(id, name, owner, members, balance,transactions))
			}
		}
		return list
	}

	fun disconnect() {
		if (::connection.isInitialized) connection.close()
	}

	fun deleteBusiness(id: String) {
		connection.prepareStatement("DELETE FROM businesses WHERE id = ?").use { stmt ->
			stmt.setString(1, id)
			stmt.executeUpdate()
		}
	}
}