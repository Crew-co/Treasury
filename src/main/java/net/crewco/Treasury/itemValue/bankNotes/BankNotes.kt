package net.crewco.Treasury.itemValue.bankNotes

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.models.AccountType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class BankNotes @Inject constructor(private val plugin:Startup){
	fun createBankNote(amount: Double, issuer: OfflinePlayer): ItemStack {
		val item = ItemStack(Material.PAPER)
		val meta = item.itemMeta!!

		meta.setDisplayName("§6Bank Note")
		meta.lore = listOf(
			"§7Value: §a$${"%.2f".format(amount)}",
			"§7Issued by: §e${issuer.name}",
			"§8Right-click to redeem"
		)

		val key = NamespacedKey(plugin, "banknote_value")
		meta.persistentDataContainer.set(key, PersistentDataType.DOUBLE, amount)

		item.itemMeta = meta
		return item
	}

	fun createBankNoteBusiness(amount: Double, issuer: String): ItemStack {
		val item = ItemStack(Material.PAPER)
		val meta = item.itemMeta!!

		meta.setDisplayName("§6Business Bank Note")
		meta.lore = listOf(
			"§7Value: §a$${"%.2f".format(amount)}",
			"§7Issued by: §e${issuer}",
			"§8Right-click to redeem"
		)

		val key = NamespacedKey(plugin, "banknote_value_business")
		meta.persistentDataContainer.set(key, PersistentDataType.DOUBLE, amount)

		item.itemMeta = meta
		return item
	}

	fun createCheck(
		plugin: Startup,
		issuer: UUID,
		amount: Double,
		recipient: String,
		accountType: AccountType,
		readable: String,
		expireMillis: Long // milliseconds from now when the check should expire
	): ItemStack {
		val item = ItemStack(Material.PAPER)
		val meta = item.itemMeta!!

		meta.setDisplayName("§6Check")
		meta.lore = listOf(
			"§7Amount: §a$${"%.2f".format(amount)}",
			"§7From: §e${Bukkit.getOfflinePlayer(issuer).name}",
			recipient.let { "§7To: §b${it}" },
			"§7CheckType:${accountType.name}",
			"§7Expires in: §f$readable"
		)

		val container = meta.persistentDataContainer
		container.set(NamespacedKey(plugin, "check_amount"), PersistentDataType.DOUBLE, amount)
		container.set(NamespacedKey(plugin, "check_issuer"), PersistentDataType.STRING, issuer.toString())
		container.set(NamespacedKey(plugin, "check_type"), PersistentDataType.STRING, accountType.name)
		container.set(NamespacedKey(plugin, "check_expires"), PersistentDataType.LONG, expireMillis)

		recipient.let {
			container.set(NamespacedKey(plugin, "check_recipient"), PersistentDataType.STRING, it)
		}

		item.itemMeta = meta
		return item
	}
}