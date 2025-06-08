package net.crewco.Treasury.models

import java.util.*

data class Transaction(
	val id: Int = 0,
	val sender: UUID?,
	val receiver: UUID?,
	val amount: Double,
	val transactionType: TransactionType,
	val accountType: AccountType,
	val timestamp: Long = System.currentTimeMillis()
)
