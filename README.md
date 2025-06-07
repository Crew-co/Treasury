# US(W) Treasy Plugin as well as Economy Plugin with Vault Support

### Treasury Plugin
------------
#### Player Commands
- /wallet bal
- /wallet pay(don't know if it works quite yet)
- /wallet deposit [amount]
- /wallet withdraw [amount]
------------
#### Bank Commands
- /bank deposit [amount]
- /bank withdraw [amount]
- /bank bal
------------
#### Business Commands
- /business info [id]
- /business list
- /business deposit [id] [amount]
- /business withdraw [id] [amount]
- /business hire/fire [id] [player]
- /business delete [id]
------------
#### Admin Commands

##### /treasury-reload
Does exactly what you think it does

------------

##### /walletAdmin
- /walletAdmin set [player] [amount]
- /walletAdmin give [player] [amount]
- /walletAdmin resetwithdraw [player] (Reset daily withdrawal limit for the targetd player)
------------
##### /bankAdmin
- /bankAdmin bal
- /bankAdmin give [player] [amount]
- /bankAdmin set [player] [amount]
- /bankAdmin note [amount] [issuer]
------------
##### /businessAdmin
Same as the normal business commands but can bypass safe guards normal players cant

------------
##### Yaml Configuration
Yaml Configuration for IntrestRate and withdraw limit

------------

interest:
  business-rate: 1.5     # percent
  business-cap: 10000.0  # max daily gain per business

withdraw:
  daily-limit: 5000.0    # max per player per day

------------
Added Bank notes so the withdraw command for the wallet has a use







