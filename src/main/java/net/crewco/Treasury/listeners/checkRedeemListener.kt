package net.crewco.Treasury.listeners

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import net.crewco.Treasury.models.AccountType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*

class checkRedeemListener @Inject constructor(private val plugin: Startup) : Listener {
	@EventHandler
	fun onCheckRedeem(event: PlayerInteractEvent) {
		val player = event.player
		val item = event.item ?: return
		if (item.type != Material.PAPER) return
		if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
		if (!item.hasItemMeta()) return

		val meta = item.itemMeta ?: return
		if (!meta.hasDisplayName() || !meta.displayName.contains("§6Check")) return

		val pdc = meta.persistentDataContainer
		val amountKey = NamespacedKey(plugin, "check_amount")
		val toKey = NamespacedKey(plugin, "check_recipient")
		val typeKey = NamespacedKey(plugin, "check_type")
		val issuerKey = NamespacedKey(plugin,"check_issuer")
		val expireAtKey = NamespacedKey(plugin, "check_expires")

		val amount = pdc.get(amountKey, PersistentDataType.DOUBLE)
		val to = pdc.get(toKey, PersistentDataType.STRING)
		val type = pdc.get(typeKey, PersistentDataType.STRING) ?: "BANK"
		val issuer = pdc.get(issuerKey, PersistentDataType.STRING)
		val expireAt = pdc.get(expireAtKey, PersistentDataType.LONG)

		player.sendMessage("${sysMsg}§7[DEBUG] Check Data — To: $to, Amount: $amount, Type: $type, Expire:${expireAt}")

		if (amount == null || to == null || issuer == null) {
			player.sendMessage("${sysMsg}§c[DEBUG] Missing amount or recipient/issuer name.")
			return
		}

		if (expireAt != null && System.currentTimeMillis() > expireAt) {
			player.sendMessage("${sysMsg}§cThis check has expired and cannot be redeemed.")
			return
		}

		event.isCancelled = true

		when (type) {
			AccountType.BANK.name -> {
				if (to != player.name) {
					player.sendMessage("${sysMsg}§cThis check is not addressed to you.")
					return
				}

				// Makes it work like Normal checks in real life the account must have the balance to be withdrawn
				if (Bukkit.getOfflinePlayer(UUID.fromString(issuer)).let { bankManager.getBankAccount(it.uniqueId) } == null){player.sendMessage("${sysMsg}Account not found"); return}
				if (Bukkit.getOfflinePlayer(UUID.fromString(issuer)).let { bankManager.getBankAccount(it.uniqueId) }!!.balance > amount) {
					val checkIssuer =  bankManager.getBankAccount(Bukkit.getOfflinePlayer(UUID.fromString(issuer)).uniqueId)!!
					checkIssuer.balance -= amount
					val success = bankManager.deposit(player.uniqueId, amount)
					if (!success) {
						player.sendMessage("${sysMsg}§cCould not deposit the check into your bank account.")
						return
					}else{
						removeCheck(item,player)
					}
					player.sendMessage("${sysMsg}You redeemed a check worth §a$$amount§f.")
				}else{
					player.sendMessage("${sysMsg}DEBUG: One or the other check is null")
				}
			}

			AccountType.SHARED.name -> {
				val business = businessManager.getBusinessByOwner(player.uniqueId)
					?: businessManager.getBusinessByMember(player.uniqueId)

				if (business == null) {
					player.sendMessage("${sysMsg}§cYou're not part of a business that can redeem this check.")
					return
				}

				if (to != business.name) {
					player.sendMessage("${sysMsg}§cThis check is addressed to §e$to§c, not your business.")
					return
				}

				if (businessManager.getBusinessByOwner(Bukkit.getOfflinePlayer(UUID.fromString(issuer)).uniqueId) == null){player.sendMessage("${sysMsg}Business not found"); return}
				val businessBalance = businessManager.getBusinessByOwner(Bukkit.getOfflinePlayer(UUID.fromString(issuer)).uniqueId)!!.balance
				if (businessBalance >= amount){
					val checkIssuer = businessManager.getBusinessByOwner(Bukkit.getOfflinePlayer(UUID.fromString(issuer)).uniqueId)!!
					checkIssuer.balance -= amount
					val success = businessManager.deposit(business.id, amount)
					if (!success) {
						player.sendMessage("${sysMsg}§cCould not deposit the check into the business account.")
						return
					}

					player.sendMessage("${sysMsg}You redeemed a check for §e$to§f worth §a$$amount§f.")
					checkIssuer.transactions.add("${player.name} deposited check for $$amount to business ${business.name}")
					business.transactions.add("Deposited check from ${checkIssuer.name} for $amount")
					removeCheck(item,player)
				}
			}
			else -> {
				player.sendMessage("${sysMsg}§c[DEBUG] Unknown check type: $type")
				return
			}
		}

	}

	// Remove Check
	private fun removeCheck(item:ItemStack,player:Player){
		// Remove 1 item
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
