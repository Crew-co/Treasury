package net.crewco.Treasury.commands.bankCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.bankManager
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class BankCommand @Inject constructor(private val plugin:Startup) {
	@Command("bank <args>")
	@Permission("treasury.bank.use")
	fun onExecute(player: Player, @Argument("args", suggestions = "args") args:Array<String>){
		val bank = bankManager.getBankAccount(player.uniqueId)!!
		val wallet = accountManager

		if (args.isEmpty()){
			player.sendMessage("§e/bank deposit <amount>, /bank withdraw <amount>, /bank bal")
			return
		}
		when (args[0].lowercase()){
			"bal" -> {
				player.sendMessage("§aBank balance: §6${"%.2f".format(bank.balance)}")
			}
			"deposit" -> {
				val amount = args.getOrNull(1)?.toDoubleOrNull()
				if (amount == null || amount <= 0) {
					player.sendMessage("§cInvalid amount.")
					return
				}
				if (wallet.withdraw(player.uniqueId, amount)) {
					bankManager.deposit(player.uniqueId, amount)
					player.sendMessage("§aDeposited §6$amount §ato your bank.")
				} else {
					player.sendMessage("§cNot enough in wallet.")
				}
			}
			"withdraw" -> {
				val amount = args.getOrNull(1)?.toDoubleOrNull()
				if (amount == null || amount <= 0) {
					player.sendMessage("§cInvalid amount.")
					return
				}
				if (bankManager.withdraw(player.uniqueId, amount)) {
					wallet.deposit(player.uniqueId, amount)
					player.sendMessage("§aWithdrew §6$amount §afrom your bank.")
				} else {
					player.sendMessage("§cNot enough in bank.")
				}
			}

			else -> player.sendMessage("§e/bank deposit <amount>, /bank withdraw <amount>, /bank bal")
		}
	}

	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("deposit","withdraw","bal")
		return commandSuggestions.stream()
	}
}