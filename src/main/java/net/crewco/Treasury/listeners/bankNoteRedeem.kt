package net.crewco.Treasury.listeners

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import net.minecraft.network.chat.HoverEvent
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType

class bankNoteRedeem @Inject constructor(private val plugin:Startup):Listener {
	@EventHandler
	fun onBankNoteRedeem(event: PlayerInteractEvent) {
		val player = event.player
		val item = event.item ?: return
		if (item.type != Material.PAPER) return
		if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
		if (!item.hasItemMeta()) return

		val meta = item.itemMeta ?: return

		// Check display name
		if (!meta.hasDisplayName() || meta.displayName != "§6Bank Note") return

		// Check if lore contains the expected text
		if (!meta.hasLore() || meta.lore?.none { it.contains("Value") } != false) return

		// Try to read banknote value from PersistentDataContainer
		val key = NamespacedKey(plugin, "banknote_value")
		val value = meta.persistentDataContainer.get(key, PersistentDataType.DOUBLE)

		if (value == null || value <= 0.0) {
			player.sendMessage("§cInvalid or corrupted bank note.")
			return
		}

		event.isCancelled = true

		// Credit to player’s bank account
		val wallet = accountManager
		if (wallet.getAccount(player.uniqueId) == null) {
			player.sendMessage("§cCould not find your bank account.")
			return
		}

		wallet.deposit(player.uniqueId, amount = value)
		player.sendMessage("${sysMsg}Successfully redeemed a bank note worth $${"%.2f".format(value)}!")

		// Consume one item
		val updated = item.clone()
		updated.amount -= 1

		if (updated.amount <= 0) {
			player.inventory.remove(item)
		} else {
			player.inventory.setItem(player.inventory.heldItemSlot, updated)
		}

		player.updateInventory() // Sync just in case
	}
}