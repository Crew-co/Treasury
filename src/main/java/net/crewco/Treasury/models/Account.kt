package net.crewco.Treasury.models

import java.util.UUID

data class Account(
	val uuid: UUID,
	var balance: Double,
	val accountType: AccountType = AccountType.PLAYER,
	var lastInterestTime: Long = 0L,
	var lastWithdrawReset: Long = 0L,
	var dailyWithdrawn: Double = 0.0
)