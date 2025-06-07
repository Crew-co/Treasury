package net.crewco.Treasury.commands.businessCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.bukkit.Bukkit
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class BusinessAdminCommand @Inject constructor(private val plugin:Startup) {
	@Command("businessAdmin <args>")
	@Permission("treasury.business.admin")
	fun onExecute(player:Player, @Argument("args", suggestions = "args") args:Array<String>) {
		when (args.getOrNull(0)?.lowercase()) {
			"create" -> {
				val id = args.getOrNull(1)
				val name = args.getOrNull(2)
				val target = args.getOrNull(3)?.let { Bukkit.getOfflinePlayer(it).uniqueId }
				if (id != null && name != null && target != null) {
					if (businessManager.createBusiness(id, name, target)) {
						player.sendMessage("${sysMsg}Business '$id' created with owner $target")
					} else {
						player.sendMessage("${sysMsg}Business ID already exists.")
					}
				} else player.sendMessage("${sysMsg}Usage: /businessadmin create <id> <name> <owner>")
			}

			"set-balance" -> {
				val id = args.getOrNull(1)
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				val business = businessManager.getBusiness(id ?: "")
				if (business != null && amount != null) {
					business.balance = amount
					player.sendMessage("${sysMsg}Balance for '$id' set to $amount")
				} else player.sendMessage("${sysMsg}Usage: /businessadmin setbalance <id> <amount>")
			}

			"addmember" -> {
				val id = args.getOrNull(1)
				val player = args.getOrNull(2)?.let { Bukkit.getOfflinePlayer(it).uniqueId }
				if (id != null && player != null && businessManager.addMember(id, player)) {
					Bukkit.getPlayer(player)?.sendMessage("${sysMsg}Added member to $id")
				} else player?.let {
					Bukkit.getPlayer(it)?.sendMessage("${sysMsg}Usage: /businessadmin addmember <id> <player>")
				}
			}

			"removemember" -> {
				val id = args.getOrNull(1)
				val player = args.getOrNull(2)?.let { Bukkit.getOfflinePlayer(it).uniqueId }
				if (id != null && player != null && businessManager.removeMember(id, player)) {
					Bukkit.getPlayer(player)?.sendMessage("${sysMsg}Removed member from $id")
				} else player?.let {
					Bukkit.getPlayer(it)?.sendMessage("${sysMsg}Usage: /businessadmin removemember <id> <player>")
				}
			}

			"delete" -> {
				val id = args.getOrNull(1)
				if (id != null && businessManager.deleteBusiness(id)) {
					player.sendMessage("${sysMsg}Business '$id' deleted.")
				} else player.sendMessage("${sysMsg}Business ID not found.")
			}

			else -> player.sendMessage("${sysMsg}/businessadmin create|set-balance|addmember|removemember|delete ...")
		}

	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("create","set-balance","addmember","removemember","delete")
		return commandSuggestions.stream()
	}
}