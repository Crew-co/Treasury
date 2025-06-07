package net.crewco.Treasury.task

import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.interestCap
import net.crewco.Treasury.Startup.Companion.interestRate

class InterestAndLimitTask(private val plugin: Startup) : Runnable {
	override fun run() {
		val now = System.currentTimeMillis()

		// Apply interest to businesses
		businessManager.getAll().forEach { business ->
			val elapsed = now - business.lastInterestTime
			if (elapsed >= 86_400_000) { // 24h in millis
				val interest = business.balance * (interestRate / 100.0)
				val capped = interest.coerceAtMost(interestCap)
				business.balance += capped
				business.lastInterestTime = now
				business.transactions.add("§7Daily interest: §a+$${"%.2f".format(capped)}")
			}
		}

		// Reset withdrawal limits
		bankManager.getAllAccounts().forEach { account ->
			val elapsed = now - account.lastWithdrawReset
			if (elapsed >= 86_400_000) {
				account.dailyWithdrawn = 0.0
				account.lastWithdrawReset = now
			}
		}
	}
}