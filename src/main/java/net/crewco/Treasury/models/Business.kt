package net.crewco.Treasury.models

import java.util.*

data class Business(
	val id: String,
	val name: String,
	val owner: UUID,
	val accountType: AccountType = AccountType.SHARED,
	val members: MutableSet<UUID> = mutableSetOf(),
	var balance: Double = 0.0,
	val transactions: MutableList<String> = mutableListOf(),
	var lastInterestTime: Long = 0L

)
