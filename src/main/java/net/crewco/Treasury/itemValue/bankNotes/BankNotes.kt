package net.crewco.Treasury.itemValue.bankNotes

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class BankNotes @Inject constructor(private val plugin:Startup){
	fun createBankNote(amount: Double, issuer: Player): ItemStack {
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
}