package net.crewco.Treasury.task

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.models.AccountType
import java.util.*

class InterestAndLimitTask @Inject constructor(private val plugin: Startup) : Runnable {

	override fun run() {
		val config = plugin.config
		val now = System.currentTimeMillis()

		val businessRate = config.getDouble("interest.business-rate") / 100.0
		val playerRate = config.getDouble("interest.player-rate") / 100.0
		val cap = config.getDouble("interest.cap")

		val taxEnabled = config.getBoolean("tax.enabled")
		val taxRate = config.getDouble("tax.rate")
		val taxRecipient = config.getString("tax.recipient")!!

		// --- Business Interest ---
		businessManager.getAll().forEach { business ->
			val elapsed = now - business.lastInterestTime
			if (elapsed >= 86_400_000) {
				var interest = business.balance * businessRate
				interest = interest.coerceAtMost(cap)

				var netInterest = interest
				if (taxEnabled) {
					val tax = interest * taxRate
					netInterest -= tax
					sendTax(tax, taxRecipient)
				}

				business.balance += netInterest
				business.lastInterestTime = now
				business.transactions.add("§7Daily interest: §a+$${"%.2f".format(netInterest)}")
			}
		}

		// --- Player Bank Interest ---
		bankManager.getAllAccounts().forEach { account ->
			val elapsed = now - account.lastInterestTimestamp
			if (elapsed >= 86_400_000) {
				val rate = when (account.accountType) {
					AccountType.BANK -> playerRate
					else -> businessRate
				}

				var interest = account.balance * rate
				interest = interest.coerceAtMost(cap)

				var netInterest = interest
				if (taxEnabled) {
					val tax = interest * taxRate
					netInterest -= tax
					sendTax(tax, taxRecipient)
				}

				account.balance += netInterest
				account.lastInterestTimestamp = now
				account.dailyWithdrawn = 0.0
			}
		}
	}

	private fun sendTax(amount: Double, recipient: String) {
		try {
			val uuid = UUID.fromString(recipient)
			val taxAccount = businessManager.getBusinessByOwner(uuid)?.let { businessManager.getBusiness(it.id) } ?: return
			taxAccount.balance += amount
		} catch (e: IllegalArgumentException) {
			plugin.logger.warning("Invalid tax recipient UUID: $recipient")
		}
	}
}
