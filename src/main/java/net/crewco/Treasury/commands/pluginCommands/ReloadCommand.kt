package net.crewco.Treasury.commands.pluginCommands

import com.google.inject.Inject
import net.crewco.Treasury.Startup
import net.crewco.Treasury.Startup.Companion.messagesConfig
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.Permission

class ReloadCommand @Inject constructor(private val plugin:Startup) {

	@Command("treasury-reload")
	@Permission("treasury.reload")
	fun reloadConfig(sender: Player) {
		plugin.reloadConfigFiles()
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&',messagesConfig.getString("system-message")+"&aTreasury config reloaded."))
	}

}