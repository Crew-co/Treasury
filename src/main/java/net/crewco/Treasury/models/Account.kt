package net.crewco.Treasury.models

import java.util.UUID

data class Account(
	val uuid: UUID,
	var balance: Double,
	val accountType: AccountType = AccountType.PLAYER
)

enum class AccountType {
	PLAYER, BANK, SHARED
}