package net.crewco.Treasury.listeners

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

class bankNoteRedeem @Inject constructor(private val plugin: Startup) : Listener {

	@EventHandler
	fun onBankNoteRedeem(event: PlayerInteractEvent) {
		val player = event.player
		val item = event.item ?: return
		if (item.type != Material.PAPER) return
		if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
		if (!item.hasItemMeta()) return

		val meta = item.itemMeta ?: return
		if (!meta.hasDisplayName() || meta.displayName != "§6Bank Note") return
		if (!meta.hasLore() || meta.lore?.none { it.contains("Value") } != false) return

		val key = NamespacedKey(plugin, "banknote_value")
		val value = meta.persistentDataContainer.get(key, PersistentDataType.DOUBLE)

		if (value == null || value <= 0.0) {
			player.sendMessage("§cInvalid or corrupted bank note.")
			return
		}

		event.isCancelled = true

		if (accountManager.getAccount(player.uniqueId) == null) {
			player.sendMessage("§cCould not find your bank account.")
			return
		}

		accountManager.deposit(player.uniqueId, value)
		player.sendMessage("${sysMsg}Successfully redeemed a bank note worth $${"%.2f".format(value)}!")
		consumeOne(player, item)
	}

	@EventHandler
	fun onBankNoteRedeemBusiness(event: PlayerInteractEvent) {
		val player = event.player
		val item = event.item ?: return
		if (item.type != Material.PAPER) return
		if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
		if (!item.hasItemMeta()) return

		val meta = item.itemMeta ?: return
		if (!meta.hasDisplayName() || meta.displayName != "§6Business Bank Note") return
		if (!meta.hasLore() || meta.lore?.none { it.contains("Value") } != false) return

		val key = NamespacedKey(plugin, "banknote_value_business")
		val value = meta.persistentDataContainer.get(key, PersistentDataType.DOUBLE)

		if (value == null || value <= 0.0) {
			player.sendMessage("§cInvalid or corrupted business bank note.")
			return
		}

		event.isCancelled = true

		// Attempt to find the player's business by owner or member
		val business = businessManager.getBusinessByOwner(player.uniqueId)
			?: businessManager.getBusinessByMember(player.uniqueId)

		if (business != null) {
			businessManager.deposit(business.id, value)
			player.sendMessage("${sysMsg}Successfully redeemed a business bank note worth $${"%.2f".format(value)} into business '${business.name}'.")
		} else {
			// No business, fallback to personal bank account
			if (accountManager.getAccount(player.uniqueId) == null) {
				player.sendMessage("§cCould not find your personal bank account.")
				return
			}
			accountManager.deposit(player.uniqueId, value)
			player.sendMessage("${sysMsg}No business found. Deposited $${"%.2f".format(value)} to your personal account.")
		}

		consumeOne(player, item)
	}

	private fun consumeOne(player: Player, item: ItemStack) {
		val updated = item.clone()
		updated.amount -= 1
		if (updated.amount <= 0) {
			player.inventory.remove(item)
		} else {
			player.inventory.setItem(player.inventory.heldItemSlot, updated)
		}
		player.updateInventory()
	}
}
