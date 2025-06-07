package net.crewco.Treasury.hooks

import com.google.inject.Inject
import net.crewco.Treasury.Startup.Companion.withdrawLimit
import net.crewco.Treasury.managers.BankManager
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault2.economy.AccountPermission
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import java.math.BigDecimal
import java.util.*

class VaultBankEconomy (private val bankManager: BankManager) : AbstractEconomy(), net.milkbowl.vault2.economy.Economy {
	override fun getName(): String = "BankEconomy"
	override fun hasSharedAccountSupport(): Boolean {
		TODO("Not yet implemented")
	}

	override fun hasMultiCurrencySupport(): Boolean {
		TODO("Not yet implemented")
	}

	override fun fractionalDigits(pluginName: String): Int {
		TODO("Not yet implemented")
	}

	override fun format(amount: BigDecimal): String {
		TODO("Not yet implemented")
	}

	override fun format(pluginName: String, amount: BigDecimal): String {
		TODO("Not yet implemented")
	}

	override fun format(amount: BigDecimal, currency: String): String {
		TODO("Not yet implemented")
	}

	override fun format(pluginName: String, amount: BigDecimal, currency: String): String {
		TODO("Not yet implemented")
	}

	override fun hasCurrency(currency: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun getDefaultCurrency(pluginName: String): String {
		TODO("Not yet implemented")
	}

	override fun defaultCurrencyNamePlural(pluginName: String): String {
		TODO("Not yet implemented")
	}

	override fun defaultCurrencyNameSingular(pluginName: String): String {
		TODO("Not yet implemented")
	}

	override fun currencies(): MutableCollection<String> {
		TODO("Not yet implemented")
	}

	override fun createAccount(accountID: UUID, name: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun createAccount(accountID: UUID, name: String, player: Boolean): Boolean {
		TODO("Not yet implemented")
	}

	override fun createAccount(accountID: UUID, name: String, worldName: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun createAccount(accountID: UUID, name: String, worldName: String, player: Boolean): Boolean {
		TODO("Not yet implemented")
	}

	override fun getUUIDNameMap(): MutableMap<UUID, String> {
		TODO("Not yet implemented")
	}

	override fun getAccountName(accountID: UUID): Optional<String> {
		TODO("Not yet implemented")
	}

	override fun hasAccount(accountID: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun hasAccount(accountID: UUID, worldName: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun renameAccount(accountID: UUID, name: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun renameAccount(plugin: String, accountID: UUID, name: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun deleteAccount(plugin: String, accountID: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun accountSupportsCurrency(plugin: String, accountID: UUID, currency: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun accountSupportsCurrency(plugin: String, accountID: UUID, currency: String, world: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun getBalance(pluginName: String, accountID: UUID): BigDecimal {
		TODO("Not yet implemented")
	}

	override fun getBalance(pluginName: String, accountID: UUID, world: String): BigDecimal {
		TODO("Not yet implemented")
	}

	override fun getBalance(pluginName: String, accountID: UUID, world: String, currency: String): BigDecimal {
		TODO("Not yet implemented")
	}

	override fun has(pluginName: String, accountID: UUID, amount: BigDecimal): Boolean {
		TODO("Not yet implemented")
	}

	override fun has(pluginName: String, accountID: UUID, worldName: String, amount: BigDecimal): Boolean {
		TODO("Not yet implemented")
	}

	override fun has(
		pluginName: String,
		accountID: UUID,
		worldName: String,
		currency: String,
		amount: BigDecimal
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun withdraw(
		pluginName: String,
		accountID: UUID,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun withdraw(
		pluginName: String,
		accountID: UUID,
		worldName: String,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun withdraw(
		pluginName: String,
		accountID: UUID,
		worldName: String,
		currency: String,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun deposit(
		pluginName: String,
		accountID: UUID,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun deposit(
		pluginName: String,
		accountID: UUID,
		worldName: String,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun deposit(
		pluginName: String,
		accountID: UUID,
		worldName: String,
		currency: String,
		amount: BigDecimal
	): net.milkbowl.vault2.economy.EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun createSharedAccount(pluginName: String, accountID: UUID, name: String, owner: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun isAccountOwner(pluginName: String, accountID: UUID, uuid: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun setOwner(pluginName: String, accountID: UUID, uuid: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun isAccountMember(pluginName: String, accountID: UUID, uuid: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun addAccountMember(pluginName: String, accountID: UUID, uuid: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun addAccountMember(
		pluginName: String,
		accountID: UUID,
		uuid: UUID,
		vararg initialPermissions: AccountPermission
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun removeAccountMember(pluginName: String, accountID: UUID, uuid: UUID): Boolean {
		TODO("Not yet implemented")
	}

	override fun hasAccountPermission(
		pluginName: String,
		accountID: UUID,
		uuid: UUID,
		permission: AccountPermission
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun updateAccountPermission(
		pluginName: String,
		accountID: UUID,
		uuid: UUID,
		permission: AccountPermission,
		value: Boolean
	): Boolean {
		TODO("Not yet implemented")
	}

	override fun isEnabled(): Boolean = true
	override fun hasBankSupport(): Boolean = false
	override fun fractionalDigits(): Int {
		TODO("Not yet implemented")
	}

	override fun format(p0: Double): String {
		TODO("Not yet implemented")
	}

	override fun currencyNamePlural(): String {
		TODO("Not yet implemented")
	}

	override fun currencyNameSingular(): String {
		TODO("Not yet implemented")
	}

	override fun getBalance(player: OfflinePlayer): Double {
		return bankManager.getBankAccount(player.uniqueId)?.balance ?: return 0.0
	}

	override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
		val account = bankManager.getBankAccount(player.uniqueId)!!
		account.balance += amount
		return EconomyResponse(amount, account.balance, EconomyResponse.ResponseType.SUCCESS, null)
	}

	override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse {
		val account = bankManager.getBankAccount(player.uniqueId)
		if (account != null) {



			if (account.dailyWithdrawn + amount > withdrawLimit) {
				return EconomyResponse(
					0.0,
					account.balance,
					EconomyResponse.ResponseType.FAILURE,
					"Withdraw limit reached"
				)
			}
		}
		if (account != null) {
			if (account.balance >= amount) {
				account.balance -= amount
				account.dailyWithdrawn += amount
				return EconomyResponse(amount, account.balance, EconomyResponse.ResponseType.SUCCESS, null)
			}
		}
		return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, null)
	}

	override fun has(player: OfflinePlayer, amount: Double): Boolean {
		return bankManager.getBankAccount(player.uniqueId)!!.balance >= amount
	}

	override fun createPlayerAccount(player: OfflinePlayer): Boolean = true
	override fun createPlayerAccount(p0: String?): Boolean {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun createPlayerAccount(p0: String?, p1: String?): Boolean {
		TODO("Not yet implemented")
	}

	// Not needed or stubbed
	@Deprecated("Deprecated in Java")
	override fun getBalance(player: String): Double = getBalance(UUID.fromString(player))
	@Deprecated("Deprecated in Java")
	override fun getBalance(p0: String?, p1: String?): Double {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun withdrawPlayer(player: String, amount: Double): EconomyResponse = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Use OfflinePlayer")
	override fun withdrawPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun depositPlayer(player: String, amount: Double): EconomyResponse = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Use OfflinePlayer")
	override fun depositPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun has(player: String, amount: Double): Boolean = false
	override fun has(p0: String?, p1: String?, p2: Double): Boolean {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun createBank(name: String?, player: String?): EconomyResponse = notSupported()
	override fun createBank(name: String?, player: OfflinePlayer?): EconomyResponse = notSupported()
	override fun deleteBank(name: String?): EconomyResponse = notSupported()
	override fun bankBalance(name: String?): EconomyResponse = notSupported()
	override fun bankHas(name: String?, amount: Double): EconomyResponse = notSupported()
	override fun bankWithdraw(name: String?, amount: Double): EconomyResponse = notSupported()
	override fun bankDeposit(name: String?, amount: Double): EconomyResponse = notSupported()
	@Deprecated("Deprecated in Java")
	override fun isBankOwner(name: String?, player: String?): EconomyResponse = notSupported()
	override fun isBankOwner(name: String?, player: OfflinePlayer?): EconomyResponse = notSupported()
	@Deprecated("Deprecated in Java")
	override fun isBankMember(name: String?, player: String?): EconomyResponse = notSupported()
	override fun isBankMember(name: String?, player: OfflinePlayer?): EconomyResponse = notSupported()
	override fun getBanks(): MutableList<String> = mutableListOf()
	@Deprecated("Deprecated in Java")
	override fun hasAccount(player: String?): Boolean = true
	override fun hasAccount(p0: String?, p1: String?): Boolean {
		TODO("Not yet implemented")
	}

	override fun hasAccount(player: OfflinePlayer?): Boolean = true

	private fun getBalance(uuid: UUID): Double = bankManager.getBankAccount(uuid)!!.balance
	private fun notSupported(): EconomyResponse = EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank APIs not supported.")
}