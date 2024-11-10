package net.crewco.Template.commands

import cloud.commandframework.annotations.CommandDescription
import cloud.commandframework.annotations.CommandMethod
import cloud.commandframework.annotations.CommandPermission
import com.google.inject.Inject
import net.crewco.Template.Startup
import org.bukkit.entity.Player

class TemplateCommand @Inject constructor(private val plugin: Startup) {
	@CommandMethod("templatecommand")
	@CommandDescription("Template Command")
	@CommandPermission("templateplugin.command.templatecommand")
	suspend fun template(player: Player) {
	}
}