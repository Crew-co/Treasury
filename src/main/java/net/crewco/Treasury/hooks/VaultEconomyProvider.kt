// VaultEconomyProvider.kt
package net.crewco.Treasury.hooks

import net.crewco.Treasury.Startup.Companion.withdrawLimit
import net.crewco.Treasury.managers.AccountManager
import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import net.milkbowl.vault2.economy.AccountPermission
import net.milkbowl.vault2.economy.Economy
import org.bukkit.Bukkit
import java.math.BigDecimal
import java.util.*

class VaultEconomyProvider(private val accountManager: AccountManager) : AbstractEconomy(), Economy {
	override fun isEnabled(): Boolean = true

	override fun getName(): String = "TreasuryPlugin"
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

	override fun hasBankSupport(): Boolean = true

	override fun fractionalDigits(): Int = 2

	override fun format(p0: Double): String = "$%.2f".format(p0)

	override fun currencyNamePlural(): String = "dollars"

	override fun currencyNameSingular(): String = "dollar"

	@Deprecated("Deprecated in Java")
	override fun hasAccount(p0: String?): Boolean = true
	override fun hasAccount(p0: String?, p1: String?): Boolean {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun getBalance(p0: String?): Double {
		val player = Bukkit.getPlayer(p0!!) ?: return 0.0
		return accountManager.getBalance(player.uniqueId)
	}

	override fun getBalance(p0: String?, p1: String?): Double {
		TODO("Not yet implemented")
	}

	override fun has(p0: String?, p1: Double): Boolean {
		return getBalance(p0) >= p1
	}

	override fun has(p0: String?, p1: String?, p2: Double): Boolean {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun withdrawPlayer(p0: String?, p1: Double): EconomyResponse {
		val player = Bukkit.getPlayer(p0!!)
		if (player != null) {
			val uuid = player.uniqueId
			val withdrawnToday = accountManager.getWithdrawnToday(uuid)
			val limit = withdrawLimit

			if (withdrawnToday + p1 > limit) {
				return EconomyResponse(
					0.0,
					accountManager.getBalance(uuid),
					EconomyResponse.ResponseType.FAILURE,
					"Daily withdraw limit of $$limit reached"
				)
			}

			return if (accountManager.withdraw(uuid, p1)) {
				accountManager.addWithdrawnToday(uuid, p1)
				EconomyResponse(p1, accountManager.getBalance(uuid), EconomyResponse.ResponseType.SUCCESS, "")
			} else {
				EconomyResponse(0.0, accountManager.getBalance(uuid), EconomyResponse.ResponseType.FAILURE, "Insufficient funds")
			}
		}
		return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "No Player Found")
	}

	override fun withdrawPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	@Deprecated("Deprecated in Java")
	override fun depositPlayer(p0: String?, p1: Double): EconomyResponse {
		val player = Bukkit.getPlayer(p0!!)
		if (player != null) {
			accountManager.deposit(player.uniqueId, p1)
			return EconomyResponse(p1, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "")
		}
		return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "No Player Found")
	}

	override fun depositPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun createBank(p0: String?, p1: String?): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun deleteBank(p0: String?): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun bankBalance(p0: String?): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun bankHas(p0: String?, p1: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun bankWithdraw(p0: String?, p1: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun bankDeposit(p0: String?, p1: Double): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun isBankOwner(p0: String?, p1: String?): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun isBankMember(p0: String?, p1: String?): EconomyResponse {
		TODO("Not yet implemented")
	}

	override fun getBanks(): MutableList<String> {
		TODO("Not yet implemented")
	}

	override fun createPlayerAccount(p0: String?): Boolean {
		TODO("Not yet implemented")
	}

	override fun createPlayerAccount(p0: String?, p1: String?): Boolean {
		TODO("Not yet implemented")
	}

	fun unsupported(): EconomyResponse {
		return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not supported")
	}
}
