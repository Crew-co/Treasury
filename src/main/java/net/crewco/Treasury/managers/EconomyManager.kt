package net.crewco.Treasury.managers


import net.crewco.Treasury.models.Transaction
import net.crewco.Treasury.models.TransactionType
import java.util.*

class EconomyManager(private val accountManager: AccountManager) {
	private val transactions = mutableListOf<Transaction>()

	fun transfer(sender: UUID, receiver: UUID, amount: Double): Boolean {
		if (accountManager.withdraw(sender, amount)) {
			accountManager.deposit(receiver, amount)
			transactions += Transaction(sender = sender, receiver = receiver, amount = amount, type = TransactionType.TRANSFER)
			return true
		}
		return false
	}

	fun getTransactions(): List<Transaction> = transactions
}
