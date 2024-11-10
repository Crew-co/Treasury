package net.crewco.Template

import net.crewco.common.CrewCoPlugin

class Startup : CrewCoPlugin() {
	companion object{
		lateinit var plugin:Startup
			private set
	}
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		//Inits
		plugin = this



		//Config
		plugin.config.options().copyDefaults()
		plugin.saveDefaultConfig()

	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
	}
}