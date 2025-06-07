package net.crewco.Treasury.managers

import net.crewco.Treasury.models.BankAccount
import java.util.*
import kotlin.collections.HashMap

class BankManager(private val bankDb:BankDatabase){
	private val bankAccounts: MutableMap<UUID, BankAccount> = HashMap()

	fun load() {
		for (acc in bankDb.loadAccounts()) {
			bankAccounts[acc.owner] = acc
		}
	}

	fun save() {
		bankDb.saveAccounts(bankAccounts.values.toList())
	}

	fun createBankAccount(uuid: UUID): BankAccount? {
		return if (bankAccounts.containsKey(uuid)) {
			null // Account already exists
		} else {
			val account = BankAccount(owner = uuid, balance = 0.0)
			bankAccounts[uuid] = account
			account
		}
	}

	fun getBankAccount(uuid: UUID): BankAccount? = bankAccounts[uuid]

	fun deposit(uuid: UUID, amount: Double): Boolean {
		val account = bankAccounts[uuid] ?: return false
		account.balance += amount
		return true
	}

	fun withdraw(uuid: UUID, amount: Double): Boolean {
		val account = bankAccounts[uuid] ?: return false
		if (account.balance >= amount) {
			account.balance -= amount
			return true
		}
		return false
	}

	fun getBalance(uuid: UUID): Double {
		return bankAccounts[uuid]?.balance ?: 0.0
	}

	fun accrueInterest(rate: Double, maxDailyInterest: Double) {
		val currentTime = System.currentTimeMillis()
		val oneDayMs = 86_400_000L

		for ((_, account) in bankAccounts) {
			val timeSinceLast = currentTime - account.lastInterestTimestamp

			// Reset daily interest if over 24h
			if (timeSinceLast >= oneDayMs) {
				account.interestEarnedToday = 0.0
				account.lastInterestTimestamp = currentTime
			}

			val interest = account.balance * rate
			val canEarn = maxDailyInterest - account.interestEarnedToday
			val actualInterest = if (interest > canEarn) canEarn else interest

			if (actualInterest > 0) {
				account.balance += actualInterest
				account.interestEarnedToday += actualInterest
			}
		}
	}

	fun getAllAccounts(): List<BankAccount> = bankAccounts.values.toList()
}