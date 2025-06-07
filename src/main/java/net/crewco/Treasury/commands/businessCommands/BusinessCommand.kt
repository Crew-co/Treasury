package net.crewco.Treasury.commands.businessCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.bankNotes
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import net.crewco.Treasury.Startup.Companion.withdrawLimit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class BusinessCommand @Inject constructor(private val plugin:Startup) {
	@Command("business <args>")
	@Permission("treasury.business.use")
	fun onExecute(player:Player, @Argument("args", suggestions = "args") args:Array<String>){

		when (args.getOrNull(0)?.lowercase()) {

			"create" -> {
				val name = args.getOrNull(1)
				val target = player.uniqueId

				if (name == null) {
					player.sendMessage("${sysMsg}Usage: /business create <name>")
					return
				}

				// Check if player already owns a business
				val ownsBusiness = businessManager.getAll().any { it.owner == target }
				if (ownsBusiness) {
					player.sendMessage("${sysMsg}You already own a business and cannot create another.")
					return
				}

				// Generate a new business ID
				val id = businessManager.generateBusinessId()

				if (businessManager.createBusiness(id, name, target)) {
					player.sendMessage("${sysMsg}Business '$name' created with owner ${Bukkit.getOfflinePlayer(target).name}")
				} else {
					player.sendMessage("${sysMsg}A business with this ID or name already exists.")
				}
			}

			"delete" -> {
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				if (id == null) {
					player.sendMessage("${sysMsg}Please specify a business ID to delete.")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("${sysMsg}Business name '$name' not found.")
					return
				}

				if (business.owner != player.uniqueId) {
					player.sendMessage("${sysMsg}Only the owner can delete this business.")
					return
				}

				if (businessManager.deleteBusiness(id)) {
					player.sendMessage("${sysMsg}Business '$id' deleted.")
				} else {
					player.sendMessage("${sysMsg}Failed to delete business '$id'.")
				}
			}

			"hire" -> {
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				val memberName = args.getOrNull(2)
				if (id == null || memberName == null) {
					player.sendMessage("${sysMsg}Usage: /business addmember <name> <player>")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("${sysMsg}Business '$name' not found.")
					return
				}

				// Only owner can add members
				if (business.owner != player.uniqueId) {
					player.sendMessage("${sysMsg}Only the owner can add members.")
					return
				}

				val member = Bukkit.getOfflinePlayer(memberName).uniqueId

				if (business.members.contains(member)) {
					player.sendMessage("${sysMsg}That player is already a member.")
					return
				}

				if (businessManager.addMember(id, member)) {
					player.sendMessage("${sysMsg}Player $memberName added to business '$id'.")
					Bukkit.getPlayer(member)?.sendMessage("${sysMsg}You were added to the business '${business.name}'.")
				} else {
					player.sendMessage("${sysMsg}Failed to add member.")
				}
			}

			"fire" -> {
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				val memberName = args.getOrNull(2)
				if (id == null || memberName == null) {
					player.sendMessage("${sysMsg}Usage: /business fire <name> <player>")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("${sysMsg}Business '$name' not found.")
					return
				}

				// Only owner can remove members
				if (business.owner != player.uniqueId) {
					player.sendMessage("${sysMsg}Only the owner can remove members.")
					return
				}

				val member = Bukkit.getOfflinePlayer(memberName).uniqueId

				if (!business.members.contains(member)) {
					player.sendMessage("${sysMsg}That player is not a member.")
					return
				}

				// Prevent owner from removing themselves
				if (business.owner == member) {
					player.sendMessage("${sysMsg}You cannot remove yourself as owner.")
					return
				}

				if (businessManager.removeMember(id, member)) {
					player.sendMessage("${sysMsg}Player $memberName removed from business '$id'.")
					Bukkit.getPlayer(member)?.sendMessage("${sysMsg}You were removed from the business '${business.name}'.")
				} else {
					player.sendMessage("${sysMsg}Failed to remove member.")
				}
			}

			"info" -> {
				player.sendMessage("${sysMsg}Business Info:")
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				val biz = businessManager.getBusiness(id ?: "")
				if (biz != null && biz.members.contains(player.uniqueId) || biz != null && biz.owner == player.uniqueId) {
					player.sendMessage("§6Business: §e${biz.name}")
					player.sendMessage("§7Owner: §f${Bukkit.getOfflinePlayer(biz.owner).name}")
					player.sendMessage("§7Members: §f${biz.members.map { Bukkit.getOfflinePlayer(it).name }.joinToString(", ")}")
					player.sendMessage("§7Balance: §a$${biz.balance}")
					player.sendMessage("§7Recent Transactions:")
					biz.transactions.takeLast(5).forEach {
						player.sendMessage(" §8- §f$it")
					}
				} else {
					player.sendMessage("§cNot found or you're not a member.")
				}
			}

			"list" -> {
				val businesses = businessManager.getAll()
				if (businesses.isEmpty()) {
					player.sendMessage("${sysMsg}There are no businesses.")
				} else {
					player.sendMessage("${sysMsg}Businesses:")
					businesses.forEach {
						player.sendMessage(" §e${it.id}§7: §f${it.name} (§a$${it.balance}§f)")
					}
				}
			}

			"deposit" -> {
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				val biz = businessManager.getBusiness(id ?: "")
				val bank = bankManager.getBankAccount(player.uniqueId)
				if (biz != null && amount != null && amount > 0 && biz.members.contains(player.uniqueId)) {
					if (bank != null) {
						if (bank.balance >= amount) {
							bank.balance -= amount
							biz.balance += amount
							biz.transactions.add("${player.name} deposited $$amount")
							player.sendMessage("${sysMsg}Deposited $$amount into ${biz.name}")
						} else {
							player.sendMessage("§cNot enough funds.")
						}
					}
				} else {
					player.sendMessage("${sysMsg}Usage: /business deposit <name> <amount>")
				}
			}

			"withdraw" -> {
				val name = args.getOrNull(1)?: businessManager.getBusinessByMember(player.uniqueId)?.name
				val id = name?.let { businessManager.getBusinessByName(it)?.id }
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				val biz = businessManager.getBusiness(id ?: "")
				if (biz != null && amount != null && amount > 0 && biz.members.contains(player.uniqueId)) {
					if (biz.balance >= amount) {
						run {
							biz.balance -= amount
							biz.transactions.add("${player.name} withdrew $$amount")
							player.inventory.addItem(bankNotes.createBankNoteBusiness(amount,biz.name))
							player.sendMessage("${sysMsg}Withdrew $$amount from ${biz.name}")
						}
					} else {
						player.sendMessage("${sysMsg}Business doesn't have enough.")
					}
				} else {
					player.sendMessage("${sysMsg}Usage: /business withdraw <name> <amount>")
				}
			}

			else -> {
				player.sendMessage("${sysMsg}Business Help")
				player.sendMessage("§e/business info <name>")
				player.sendMessage("§e/business list")
				player.sendMessage("§e/business deposit <name> <amount>")
				player.sendMessage("§e/business withdraw <name> <amount>")
				player.sendMessage("§e/business hire/fire <name> <player>")
				player.sendMessage("§e/business delete <name>")
			}
		}

		return
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("create","info","list","deposit","withdraw","delete","hire","fire")
		return commandSuggestions.stream()
	}
}