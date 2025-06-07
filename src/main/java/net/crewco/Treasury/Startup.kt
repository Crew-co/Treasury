package net.crewco.Treasury

import net.crewco.Treasury.commands.bankCommands.BankAdminCommand
import net.crewco.Treasury.commands.bankCommands.BankCommand
import net.crewco.Treasury.commands.businessCommands.BusinessAdminCommand
import net.crewco.Treasury.commands.businessCommands.BusinessCommand
import net.crewco.Treasury.commands.playerWalletCommands.walletAdminCommand
import net.crewco.Treasury.commands.playerWalletCommands.walletCommand
import net.crewco.Treasury.commands.pluginCommands.ReloadCommand
import net.crewco.Treasury.hooks.VaultBankEconomy
import net.crewco.Treasury.hooks.VaultEconomyProvider
import net.crewco.Treasury.itemValue.bankNotes.BankNotes
import net.crewco.Treasury.listeners.accountCreateListener
import net.crewco.Treasury.listeners.bankNoteRedeem
import net.crewco.Treasury.managers.AccountManager
import net.crewco.Treasury.managers.AccountsDataBase
import net.crewco.Treasury.managers.BankDatabase
import net.crewco.Treasury.managers.BankManager
import net.crewco.Treasury.managers.BusinessDatabase
import net.crewco.Treasury.managers.BusinessManager
import net.crewco.Treasury.managers.DatabaseManager
import net.crewco.Treasury.managers.EconomyManager
import net.crewco.Treasury.task.InterestAndLimitTask
import net.crewco.common.CrewCoPlugin
import net.milkbowl.vault2.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.ServicePriority
import java.io.File
import kotlin.properties.Delegates


class Startup : CrewCoPlugin() {
	companion object{
		lateinit var plugin:Startup
			private set
		lateinit var dbManager:DatabaseManager
		lateinit var accountManager: AccountManager
		lateinit var econManager: EconomyManager
		lateinit var bankManager: BankManager
		lateinit var bankDatabase: BankDatabase
		lateinit var businessdb:BusinessDatabase
		lateinit var businessManager: BusinessManager
		var interestRate by Delegates.notNull<Double>()
		var interestCap by Delegates.notNull<Double>()
		var withdrawLimit by Delegates.notNull<Double>()
		lateinit var accountsDataBase: AccountsDataBase
		lateinit var sysMsg:String
		lateinit var bankNotes: BankNotes
	}

	private lateinit var econ: Economy;
	override suspend fun onEnableAsync() {
		super.onEnableAsync()

		val dataFolder = dataFolder

		// Make sure the folder exists
		if (!dataFolder.exists()) {
			dataFolder.mkdirs()
		}

		//Inits
		plugin = this
		sysMsg = ChatColor.translateAlternateColorCodes('&', "&7[&6Treasury&7]> ")
		bankNotes = BankNotes(this)
		// Init DataBases
		dbManager = DatabaseManager(File(dataFolder,"economy.db"))
		accountsDataBase = AccountsDataBase(File(dataFolder,"economy.db"))
		bankDatabase = BankDatabase(File(dataFolder,"banks.db"))
		businessdb = BusinessDatabase(File(dataFolder, "businesses.db"))


		// Init Managers

		// Account Manager
		accountManager = AccountManager(dbManager)

		// Economy
		econManager = EconomyManager(accountManager)

		//Bank Manager
		bankManager = BankManager(bankDatabase)
		bankManager.load()

		//Business Manager
		businessManager = BusinessManager(businessdb)
		businessManager.load()

		// Optional: save periodically
		server.scheduler.runTaskTimer(this, Runnable {
			bankManager.save()
			businessManager.save()
		}, 0L, 6000L)

		// Schedule interest accrual every 5 minutes (6000 ticks)
		val rate = 0.01 // 1%
		val maxDailyInterest = 100.0 // Cap at $100/day

		// Interest tick
		server.scheduler.runTaskTimer(this, Runnable {
			bankManager.accrueInterest(rate, maxDailyInterest)
			bankManager.save()
		}, 0L, 6000L)

		Bukkit.getScheduler().runTaskTimer(plugin, InterestAndLimitTask(this), 0L, 20L * 60 * 60)


		//Config
		plugin.config.options().copyDefaults()
		saveDefaultConfig()
		interestRate = config.getDouble("interest.business-rate")
		interestCap = config.getDouble("interest.business-cap")
		withdrawLimit = config.getDouble("withdraw.daily-limit")

		registerCommands(walletCommand::class, BankCommand::class, BankAdminCommand::class,walletAdminCommand::class,BusinessCommand::class,BusinessAdminCommand::class,ReloadCommand::class)
		registerListeners(accountCreateListener::class,bankNoteRedeem::class)

		// Register Vault Hooks
		val vaultProvider = VaultEconomyProvider(accountManager)
		server.servicesManager.register(Economy::class.java, vaultProvider, this, ServicePriority.Normal)
		logger.info("Vault economy provider registered.")

		val vaultProvider2 = VaultBankEconomy(bankManager)
		server.servicesManager.register(Economy::class.java, vaultProvider2, this, ServicePriority.Normal)
		logger.info("Vault economy hooked using BankEconomy.")


		if (!setupVault()) {
			logger.severe(String.format("[%s] - Disabled VaultHooks due to no Vault dependency found!", getDescription().name));
			return;
		}

		logger.info("Treasury Plugin loaded successfully")
	}

	override suspend fun onDisableAsync() {
		super.onDisableAsync()
		logger.info("Treasury Plugin disabled")

		bankManager.save()
		bankDatabase.disconnect()

		businessManager.save()
		businessdb.disconnect()
	}

	private fun setupVault():Boolean {
		val rsp = server.servicesManager.getRegistration(Economy::class.java)
		if (rsp == null) {
			logger.warning("Vault not found. Economy won't be available to other plugins.")
			return false
		}
		econ = rsp.provider
		return true
	}
	fun reloadConfigFiles() {
		saveConfig()
		reloadConfig()

		/* If using additional configs like messages.yml
		val messagesFile = File(dataFolder, "messages.yml")
		if (!messagesFile.exists()) saveResource("messages.yml", false)
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile)*/
		logger.info("${sysMsg}Treasury config files reloaded.")
	}

}