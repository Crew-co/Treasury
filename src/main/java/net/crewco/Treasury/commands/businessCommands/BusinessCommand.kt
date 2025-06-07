package net.crewco.Treasury.commands.businessCommands

import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.withdrawLimit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class BusinessCommand {
	@Command("business <args>")
	fun onExecute(player:Player, @Argument("args", suggestions = "args") args:Array<String>){

		when (args.getOrNull(0)?.lowercase()) {

			"create" -> {
				val name = args.getOrNull(0)
				val target = player.uniqueId

				if (name == null) {
					player.sendMessage("§cUsage: /business create <name>")
					return
				}

				// Check if player already owns a business
				val ownsBusiness = businessManager.getAll().any { it.owner == target }
				if (ownsBusiness) {
					player.sendMessage("§cYou already own a business and cannot create another.")
					return
				}

				// Generate a new business ID
				val id = businessManager.generateBusinessId()

				if (businessManager.createBusiness(id, name, target)) {
					player.sendMessage("§aBusiness '$id' created with owner $target")
				} else {
					player.sendMessage("§cA business with this ID or name already exists.")
				}
			}

			"delete" -> {
				val id = args.getOrNull(1)
				if (id == null) {
					player.sendMessage("§cPlease specify a business ID to delete.")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("§cBusiness ID '$id' not found.")
					return
				}

				if (business.owner != player.uniqueId) {
					player.sendMessage("§cOnly the owner can delete this business.")
					return
				}

				if (businessManager.deleteBusiness(id)) {
					player.sendMessage("§cBusiness '$id' deleted.")
				} else {
					player.sendMessage("§cFailed to delete business '$id'.")
				}
			}

			"hire" -> {
				val id = args.getOrNull(1)
				val memberName = args.getOrNull(2)
				if (id == null || memberName == null) {
					player.sendMessage("§cUsage: /business addmember <id> <player>")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("§cBusiness ID '$id' not found.")
					return
				}

				// Only owner can add members
				if (business.owner != player.uniqueId) {
					player.sendMessage("§cOnly the owner can add members.")
					return
				}

				val member = Bukkit.getOfflinePlayer(memberName).uniqueId

				if (business.members.contains(member)) {
					player.sendMessage("§cThat player is already a member.")
					return
				}

				if (businessManager.addMember(id, member)) {
					player.sendMessage("§aPlayer $memberName added to business '$id'.")
					Bukkit.getPlayer(member)?.sendMessage("§aYou were added to the business '${business.name}'.")
				} else {
					player.sendMessage("§cFailed to add member.")
				}
			}

			"fire" -> {
				val id = args.getOrNull(1)
				val memberName = args.getOrNull(2)
				if (id == null || memberName == null) {
					player.sendMessage("§cUsage: /business fire <id> <player>")
					return
				}

				val business = businessManager.getBusiness(id)
				if (business == null) {
					player.sendMessage("§cBusiness ID '$id' not found.")
					return
				}

				// Only owner can remove members
				if (business.owner != player.uniqueId) {
					player.sendMessage("§cOnly the owner can remove members.")
					return
				}

				val member = Bukkit.getOfflinePlayer(memberName).uniqueId

				if (!business.members.contains(member)) {
					player.sendMessage("§cThat player is not a member.")
					return
				}

				// Prevent owner from removing themselves
				if (business.owner == member) {
					player.sendMessage("§cYou cannot remove yourself as owner.")
					return
				}

				if (businessManager.removeMember(id, member)) {
					player.sendMessage("§aPlayer $memberName removed from business '$id'.")
					Bukkit.getPlayer(member)?.sendMessage("§cYou were removed from the business '${business.name}'.")
				} else {
					player.sendMessage("§cFailed to remove member.")
				}
			}

			"info" -> {
				val id = args.getOrNull(1)
				val biz = businessManager.getBusiness(id ?: "")
				if (biz != null && biz.members.contains(player.uniqueId)) {
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
					player.sendMessage("§cThere are no businesses.")
				} else {
					player.sendMessage("§6Businesses:")
					businesses.forEach {
						player.sendMessage(" §e${it.id}§7: §f${it.name} (§a$${it.balance}§f)")
					}
				}
			}

			"deposit" -> {
				val id = args.getOrNull(1)
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				val biz = businessManager.getBusiness(id ?: "")
				val bank = bankManager.getBankAccount(player.uniqueId)
				if (biz != null && amount != null && amount > 0 && biz.members.contains(player.uniqueId)) {
					if (bank != null) {
						if (bank.balance >= amount) {
							bank.balance -= amount
							biz.balance += amount
							biz.transactions.add("${player.name} deposited $$amount")
							player.sendMessage("§aDeposited $$amount into ${biz.name}")
						} else {
							player.sendMessage("§cNot enough funds.")
						}
					}
				} else {
					player.sendMessage("§cUsage: /business deposit <id> <amount>")
				}
			}

			"withdraw" -> {
				val id = args.getOrNull(1)
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				val biz = businessManager.getBusiness(id ?: "")
				val bank = bankManager.getBankAccount(player.uniqueId)
				if (biz != null && amount != null && amount > 0 && biz.members.contains(player.uniqueId)) {
					if (biz.balance >= amount) if (bank != null) {
						run {
							if (bank.dailyWithdrawn + amount > withdrawLimit) {
								player.sendMessage("§cYou've reached your daily withdraw limit of $${withdrawLimit}")
								return
							}
							biz.balance -= amount
							bank.balance += amount
							biz.transactions.add("${player.name} withdrew $$amount")
							player.sendMessage("§aWithdrew $$amount from ${biz.name}")
						}
					} else {
						player.sendMessage("§cBusiness doesn't have enough.")
					}
				} else {
					player.sendMessage("§cUsage: /business withdraw <id> <amount>")
				}
			}

			else -> {
				player.sendMessage("§e/business info <id>")
				player.sendMessage("§e/business list")
				player.sendMessage("§e/business deposit <id> <amount>")
				player.sendMessage("§e/business withdraw <id> <amount>")
				player.sendMessage("§e/business hire/fire <id> <player>")
				player.sendMessage("§e/business delete <id>")
			}
		}

		return
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("info","list","deposit","withdraw","delete","hire","fire")
		return commandSuggestions.stream()
	}
}