package net.crewco.Treasury.managers

import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.models.Account
import net.crewco.Treasury.models.Business
import java.util.*

class ServerAccountManager {
	private val SERVER_ACCOUNT_UUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000001")

	init {
		createServerAccount()
	}

	private fun createServerAccount(){
		businessManager.createBusiness("0000","Government",SERVER_ACCOUNT_UUID)
	}

	fun getServerAccount(): Business {
		return businessManager.getBusiness("0000") ?: error("Failed to get server account")
	}

	fun depositToServer(amount: Double): Boolean {
		return businessManager.deposit("0000",amount)
	}

	fun withdrawFromServer(amount: Double): Boolean {
		return if (getServerBalance() >= amount) {
			businessManager.withdraw("0000",amount)
		}else {
			false
		}
	}

	private fun getServerBalance(): Double {
		return getServerAccount().balance
	}
}