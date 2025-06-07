package net.crewco.Treasury.models

import java.util.*

data class Transaction(
	val id: Int = 0,
	val sender: UUID?,
	val receiver: UUID?,
	val amount: Double,
	val type: TransactionType,
	val timestamp: Long = System.currentTimeMillis()
)

enum class TransactionType {
	DEPOSIT, WITHDRAW, TRANSFER, TAX, INTEREST
}
