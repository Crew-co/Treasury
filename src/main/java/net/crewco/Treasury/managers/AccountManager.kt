package net.crewco.Treasury.managers

import net.crewco.Treasury.models.Account
import net.crewco.Treasury.models.AccountType
import java.util.UUID

class AccountManager(private val databaseManager: DatabaseManager) {

	fun createAccount(uuid: UUID): Account {
		databaseManager.createAccountIfMissing(uuid)
		return Account(uuid, databaseManager.getBalance(uuid), AccountType.PLAYER)
	}

	fun getBalance(uuid: UUID): Double = databaseManager.getBalance(uuid)

	fun setBalance(uuid: UUID, amount: Double) {
		val current = getBalance(uuid)
		when {
			amount > current -> deposit(uuid, amount - current)
			amount < current -> withdraw(uuid, current - amount)
		}
	}

	fun deposit(uuid: UUID, amount: Double): Boolean {
		return databaseManager.deposit(uuid, amount)
	}

	fun withdraw(uuid: UUID, amount: Double): Boolean {
		return databaseManager.withdraw(uuid, amount)
	}

	fun resetWithdraw(uuid:UUID){
		return databaseManager.resetWithdraw(uuid)
	}

	fun getWithdrawnToday(uuid:UUID):Double{
		return databaseManager.getWithdrawnToday(uuid)
	}

	fun addWithdrawnToday(uuid:UUID,ammout:Double){
		return databaseManager.addWithdrawnToday(uuid,ammout)
	}

	fun getAccount(uuid:UUID): Account? {
		return databaseManager.getAccount(uuid)
	}

}
