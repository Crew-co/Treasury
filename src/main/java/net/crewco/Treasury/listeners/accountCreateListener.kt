package net.crewco.Treasury.listeners

import net.crewco.Treasury.Startup.Companion.accountManager
import net.crewco.Treasury.Startup.Companion.accountsDataBase
import net.crewco.Treasury.Startup.Companion.bankManager
import net.crewco.Treasury.Startup.Companion.dbManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class accountCreateListener:Listener {
	@EventHandler
	fun onJoin(e:PlayerJoinEvent){
		accountManager.createAccount(e.player.uniqueId)
		bankManager.createBankAccount(e.player.uniqueId)
	}
}