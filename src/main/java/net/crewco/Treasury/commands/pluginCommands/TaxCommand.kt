package net.crewco.Treasury.commands.pluginCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.bankNotes
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.serverAccountManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.*
import java.util.stream.Stream

class TaxCommand @Inject constructor(private val plugin:Startup){
	@Command("tax <args>")
	@Permission("treasury.tax.admin")
	fun onExecute(player:Player, @Argument("args", suggestions = "args") args:Array<String>){
		if (args.size < 2) {
			player.sendMessage("${sysMsg}Usage: /walletadmin <set|give|resetwithdraw> <player> [amount]")
			return
		}

		when (args[0].lowercase()) {
			"rate" -> {
				val rate = args[1].toDoubleOrNull()
				val config = plugin.config
				config.set("tax.interest-tax-rate", rate)
				plugin.saveConfig()
				if (rate != null) {
					player.sendMessage("${sysMsg}Tax rate set to ${rate * 100}%.")
				}
			}
			"info" -> {
				val config = plugin.config
				val rate = config.getDouble("tax.interest-tax-rate") * 100
				val recipient = config.getString("tax.tax-recipient")
				player.sendMessage("${sysMsg}Current Tax Rate: §a$rate%")
				player.sendMessage("${sysMsg}Recipient UUID: §f$recipient")
			}

			"balance" -> {
				val account = serverAccountManager.getServerAccount()
				val balance = account.balance
				player.sendMessage("${sysMsg}Government Account Balance: §a$${"%.2f".format(balance)}")
			}

			"withdraw" -> {
				val amount = args[1].toDoubleOrNull()
				if (serverAccountManager.withdrawFromServer(amount!!)){
					player.sendMessage("${sysMsg}Withdrew $${"%.2f".format(amount)} from government account.")
					bankNotes.createBankNoteBusiness(amount,"Government")
				}else{
					player.sendMessage("§cInsufficient funds.")
				}
			}

			"help" ->{
				player.sendMessage("${sysMsg}/tax <rate|info|balance|withdraw|help|> <amount>")
			}

			/*
			"deposit" -> {
				val amount = args[1].toDoubleOrNull()!!
				serverAccountManager.depositToServer(amount)
				player.sendMessage("${sysMsg}Deposited $${"%.2f".format(amount)} to government account.")
			}
			*/

		}

	}
	@Suggestions("args")
	fun containerSuggestions(
		context: CommandContext<Player>,
		input: String
	): Stream<String> {
		val commandSuggestions = mutableListOf("help","withdraw","balance","info","rate")
		return commandSuggestions.stream()
	}
}