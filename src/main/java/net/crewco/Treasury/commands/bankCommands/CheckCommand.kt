package net.crewco.Treasury.commands.bankCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.bankNotes
import net.crewco.Treasury.Startup.Companion.businessManager
import net.crewco.Treasury.Startup.Companion.sysMsg
import net.crewco.Treasury.models.AccountType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission
import org.incendo.cloud.annotations.suggestion.Suggestions
import org.incendo.cloud.context.CommandContext
import java.util.stream.Stream

class CheckCommand @Inject constructor(private val plugin:Startup) {
	@Command("check <args>")
	@Permission("treasury.check.write")
	fun onExecute(sender: Player, @Argument("args", suggestions = "args") args:Array<String>){
		when(args[0].lowercase()){
			"help" -> {
				sender.sendMessage("${sysMsg}Usage: /check <amount> <recipient> <type>(bank,shared(business account) <expire>)")
			}else -> {
			val amount = args[0].toDoubleOrNull()
			if (amount == null || amount <= 0) {
				sender.sendMessage("${sysMsg}Invalid amount.")
				return
			}

			val target = args[1]
			if (target == sender.name){
				sender.sendMessage("${sysMsg}You can not write a check to your self")
				return
			}

			val type = AccountType.valueOf(args[2].uppercase()).name


			val expireStr = args[3]
			sender.sendMessage("${sysMsg}EXP-TIME:${expireStr}")
			val expireDuration = parseDuration(expireStr) ?: run {
				sender.sendMessage("${sysMsg}§cInvalid time format. Use formats like 1d, 3h, 45m, 30s.")
				return
			}

			val now = System.currentTimeMillis()
			val expireAt = now + expireDuration


			sender.sendMessage("${sysMsg}NOW: $now")
			sender.sendMessage("${sysMsg}DURATION: $expireDuration")
			sender.sendMessage("${sysMsg}EXPIRES AT: $expireAt")

			val readable = when {
				expireStr.endsWith("d", true) -> "${expireStr.dropLast(1)} day(s)"
				expireStr.endsWith("h", true) -> "${expireStr.dropLast(1)} hour(s)"
				expireStr.endsWith("m", true) -> "${expireStr.dropLast(1)} minute(s)"
				expireStr.endsWith("s", true) -> "${expireStr.dropLast(1)} second(s)"
				else -> expireStr
			}

			val issuerId = sender.uniqueId
			val account = bankManager.getBankAccount(issuerId)
			if (account == null || account.balance < amount) {
				sender.sendMessage("${sysMsg}Insufficient bank funds.")
				return
			}

			//account.balance -= amount
			when(type){
				AccountType.PLAYER.name -> {
				}
				AccountType.BANK.name -> {
					val check = bankNotes.createCheck( plugin,issuerId,amount,target, AccountType.valueOf(type),readable,expireAt)
					sender.inventory.addItem(check)
					sender.sendMessage("${sysMsg}Bank Check for $${"%.2f".format(amount)} created.")
				}
				AccountType.SHARED.name -> {
					if (businessManager.getBusinessByName(target)?.let { businessManager.getBusiness(it.name) } == null){sender.sendMessage("${sysMsg}Business not found")}
					val businessName = businessManager.getBusinessByName(target)!!.name
					val check = bankNotes.createCheck( plugin,issuerId,amount,businessName, AccountType.valueOf(type),readable,expireAt)
					sender.inventory.addItem(check)
					sender.sendMessage("${sysMsg}Business Check for $${"%.2f".format(amount)} created.")
				}
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
		val commandSuggestions = mutableListOf("help")
		return commandSuggestions.stream()
	}

	private fun parseDuration(input: String): Long? {
		val regex = Regex("(?i)^(\\d+)([dhms])$")
		val match = regex.matchEntire(input) ?: return null

		val (valueStr, unit) = match.destructured
		val value = valueStr.toLongOrNull() ?: return null

		return when (unit.lowercase()) {
			"d" -> value * 86_400_000L   // 24*60*60*1000
			"h" -> value * 3_600_000L    // 60*60*1000
			"m" -> value * 60_000L       // 60*1000
			"s" -> value * 1_000L        // 1*1000
			else -> null
		}
	}
}