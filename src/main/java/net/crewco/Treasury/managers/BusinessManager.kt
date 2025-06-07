package net.crewco.Treasury.managers

import net.crewco.Treasury.models.Business
import java.util.*
import kotlin.random.Random


class BusinessManager(private val businessdb: BusinessDatabase) {



	fun load() {
		for (biz in businessdb.loadBusinesses()) {
			businesses[biz.id] = biz
		}
	}

	fun save() {
		businessdb.saveBusinesses(businesses.values.toList())
	}

	private val businesses = mutableMapOf<String, Business>()

	fun createBusiness(id: String, name: String, owner: UUID): Boolean {
		if (businesses.containsKey(id)) return false
		// Check if business name already exists (case-insensitive)
		if (businesses.values.any { it.name.equals(name, ignoreCase = true) }) {
			return false // Indicate failure because name is already taken
		}
		businesses[id] = Business(id, name, owner, mutableSetOf(owner), 0.0)
		return true
	}


	fun getBusiness(id: String): Business? = businesses[id]

	fun addMember(id: String, member: UUID): Boolean {
		val business = businesses[id] ?: return false
		business.members.add(member)
		return true
	}

	fun removeMember(id: String, member: UUID): Boolean {
		val business = businesses[id] ?: return false
		business.members.remove(member)
		return true
	}

	fun deposit(id: String, amount: Double): Boolean {
		val business = businesses[id] ?: return false
		business.balance += amount
		return true
	}

	fun withdraw(id: String, amount: Double): Boolean {
		val business = businesses[id] ?: return false
		if (business.balance >= amount) {
			business.balance -= amount
			return true
		}
		return false
	}

	fun getAll(): List<Business> = businesses.values.toList()

	fun deleteBusiness(id: String): Boolean {
		val business = businesses.remove(id)
		if (business != null) {
			businessdb.deleteBusiness(id)
			return true
		}
		return false
	}


	// New functions to get info by business ID
	fun getOwner(id: String): UUID? {
		return businesses[id]?.owner
	}

	fun getMembers(id: String): Set<UUID>? {
		return businesses[id]?.members
	}

	fun getName(id: String): String? {
		return businesses[id]?.name
	}


	 fun generateBusinessId(): String {
		// Generate a 6-digit number as a string, padded with leading zeros if needed
		val number = Random.nextInt(0, 1_000_000)
		return number.toString().padStart(6, '0')
	}
}