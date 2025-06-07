package net.crewco.Treasury.commands.bankCommands

import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.bankNotes
import net.crewco.Treasury.Startup.Companion.sysMsg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class BankAdminCommand {
	@Command("bankAdmin <args>")
	@Permission("treasury.bank.admin")
	fun onExecute(player: Player, @Argument("args", suggestions = "args") args:Array<String>){
		if (args.size <2){
			player.sendMessage("$sysMsg/bankadmin <give|set|bal> <player> [amount]")
			return
		}

		val target = Bukkit.getOfflinePlayer(args[1])
		val uuid = target.uniqueId
		val account = bankManager.getBankAccount(uuid)!!

		when (args[0].lowercase()) {
			"bal" -> {
				player.sendMessage("${sysMsg}§a${target.name}'s bank balance: §6$${"%.2f".format(account.balance)}")
			}
			"give" -> {
				val amount = args.getOrNull(2)?.toDoubleOrNull() ?: return player.error("Invalid amount.")
				account.balance += amount
				player.sendMessage("${sysMsg}Gave §6$amount §ato ${target.name}'s bank.")
			}
			"set" -> {
				val amount = args.getOrNull(2)?.toDoubleOrNull() ?: return player.error("Invalid amount.")
				account.balance = amount
				player.sendMessage("${sysMsg}Set ${target.name}'s bank balance to §6$amount.")
			}

			"note" -> {
				val amount = args.getOrNull(1)?.toDoubleOrNull() ?: return player.error("Invalid amount.")
				val target_note = Bukkit.getOfflinePlayer(args[2])
				if (amount <= 0.0) {
					player.sendMessage("§cAmount must be greater than 0.")
					return
				}

				val note = bankNotes.createBankNote(amount, player)
				player.inventory.addItem(note)
				player.sendMessage("§aCreated a bank note worth $${"%.2f".format(amount)}")
			}

			else -> player.sendMessage("${sysMsg}/bankadmin <give|set|bal|note> <player> [amount]")
		}
		return
	}

	private fun Player.error(msg: String) {
		sendMessage("${sysMsg}§c$msg")
		return
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("bal","give","set","note")
		return commandSuggestions.stream()
	}
}