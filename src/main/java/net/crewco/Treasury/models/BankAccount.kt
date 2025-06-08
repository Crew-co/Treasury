package net.crewco.Treasury.models

import java.util.*

data class BankAccount(
	val owner: UUID,
	var balance: Double,
	val name: String = owner.toString(),

	var interestEarnedToday: Double = 0.0,  // NEW
	var lastInterestTimestamp: Long = 0L,   // NEW

	var dailyWithdrawn: Double = 0.0,
	var lastWithdrawReset: Long = 0L,
	val accountType: AccountType = AccountType.BANK

)
