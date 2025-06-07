package net.crewco.Treasury.commands.playerWalletCommands

import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.econManager
import net.crewco.Treasury.Startup.Companion.withdrawLimit
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import org.spigotmc.SpigotConfig.config
import java.util.stream.Stream

class walletCommand {
	@Command("wallet <args>")
	fun onExecute(sender:Player, @Argument("args", suggestions = "args") args:Array<String>){
		if (args.isEmpty()) {
			val balance = accountManager.getBalance(sender.uniqueId)
			sender.sendMessage("§aYour wallet balance: §6$${"%.2f".format(balance)}")
			return
		}

		when (args[0].lowercase()) {
			"pay" ->{
				if (args.size == 3){
					val amount = args.getOrNull(2)?.toDoubleOrNull()
					val target = Bukkit.getOfflinePlayer(args[1])
					econManager.transfer(sender.uniqueId,target.uniqueId,amount!!)
				}
			}

			"bal" -> {
				val balance = accountManager.getBalance(sender.uniqueId)
				sender.sendMessage("§aYour wallet balance: §6$${"%.2f".format(balance)}")
			}

			"deposit" -> {
				val amount = args.getOrNull(1)?.toDoubleOrNull()
				if (amount == null || amount <= 0) {
					sender.sendMessage("§cUsage: /wallet deposit <amount>")
					return
				}
				accountManager.deposit(sender.uniqueId, amount)
				sender.sendMessage("§aDeposited §6$${"%.2f".format(amount)}")
			}

			"withdraw" -> {
				val amount = args.getOrNull(1)?.toDoubleOrNull()
				if (amount == null || amount <= 0) {
					sender.sendMessage("§cUsage: /wallet withdraw <amount>")
					return
				}

				val withdrawnToday = accountManager.getWithdrawnToday(sender.uniqueId)
				val cap = withdrawLimit
				if (withdrawnToday + amount > cap) {
					sender.sendMessage("§cYou've reached your daily withdraw limit of $cap.")
					return
				}

				if (accountManager.withdraw(sender.uniqueId, amount)) {
					accountManager.addWithdrawnToday(sender.uniqueId, amount)
					sender.sendMessage("§aWithdrew §6$${"%.2f".format(amount)}")
				} else {
					sender.sendMessage("§cInsufficient balance.")
				}
			}
		}

		return
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("pay","bal","deposit","withdraw")
		return commandSuggestions.stream()
	}
}