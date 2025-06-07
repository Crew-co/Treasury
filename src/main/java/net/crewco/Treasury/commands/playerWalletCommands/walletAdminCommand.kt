package net.crewco.Treasury.commands.playerWalletCommands

import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.dbManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class walletAdminCommand {
	@Command("walletAdmin <args>")
	@Permission("treasury.wallet.admin")
	fun onExecute(sender:Player,@Argument("args", suggestions = "args") args:Array<String>){
		if (args.size < 2) {
			sender.sendMessage("§cUsage: /walletadmin <set|give|resetwithdraw> <player> [amount]")
			return
		}

		val target = Bukkit.getPlayer(args[1])
		if (target == null) {
			sender.sendMessage("§cPlayer not found.")
			return
		}

		when (args[0].lowercase()) {
			"set" -> {
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				if (amount == null) {
					sender.sendMessage("§cUsage: /walletadmin set <player> <amount>")
					return
				}
				accountManager.setBalance(target.uniqueId, amount)
				sender.sendMessage("§aSet ${target.name}'s balance to §6$${"%.2f".format(amount)}")
			}

			"give" -> {
				val amount = args.getOrNull(2)?.toDoubleOrNull()
				if (amount == null) {
					sender.sendMessage("§cUsage: /walletadmin give <player> <amount>")
					return
				}
				accountManager.deposit(target.uniqueId, amount)
				sender.sendMessage("§aGave ${target.name} §6$${"%.2f".format(amount)}")
			}

			"resetwithdraw" -> {
				accountManager.resetWithdraw(target.uniqueId)
				sender.sendMessage("§aReset daily withdrawal for ${target.name}")
			}
		}

		return
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("set","give","resetwithdraw")
		return commandSuggestions.stream()
	}
}