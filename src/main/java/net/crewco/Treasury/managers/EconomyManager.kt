package net.crewco.Treasury.managers


import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import net.crewco.Treasury.models.AccountType
import net.crewco.Treasury.models.Transaction
import net.crewco.Treasury.models.TransactionType
import org.bukkit.Bukkit
import java.util.*

class EconomyManager(private val accountManager: AccountManager) {
	private val transactions = mutableListOf<Transaction>()

	fun transfer(sender: UUID, receiver: UUID, amount: Double,transactionType: TransactionType,accountType:AccountType): Boolean {
		if (accountType == AccountType.BANK){
			if (bankManager.withdraw(sender,amount) && bankManager.getBankAccount(Bukkit.getPlayer(sender)!!.uniqueId)!!.balance >= amount){
				bankManager.deposit(receiver,amount)
				transactions += Transaction(sender = sender, receiver = receiver, amount = amount, transactionType = transactionType, accountType = accountType)
				return true
			}else{
				Bukkit.getPlayer(sender)?.sendMessage("${sysMsg}Not enough funds")
			}
		}else if (accountType == AccountType.PLAYER){
			if (accountManager.withdraw(sender, amount)) {
				accountManager.deposit(receiver, amount)
				transactions += Transaction(sender = sender, receiver = receiver, amount = amount, transactionType = transactionType, accountType = accountType)
				return true
			}
		}else if (accountType == AccountType.SHARED){
			if (businessManager.withdraw(getBSID(sender)!!,amount)){
				businessManager.deposit(getBRID(receiver)!!,amount)
				transactions += Transaction(sender = sender, receiver = receiver, amount = amount, transactionType = transactionType, accountType = accountType)
				return true
			}
		}
		return false
	}

	private fun getBSID(sender: UUID): String? {
		return businessManager.getBusinessByOwner(Bukkit.getOfflinePlayer(sender).uniqueId)?.id?: businessManager.getBusinessByMember(sender)?.id
	}

	private fun getBRID(receiver: UUID): String? {
		return businessManager.getBusinessByOwner(Bukkit.getOfflinePlayer(receiver).uniqueId)?.id?: businessManager.getBusinessByMember(receiver)?.id
	}

	fun getTransactions(): List<Transaction> = transactions
}
