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

#### Check Command
- /check <amount> <target>(player/business) <type>(bank for player, shared for business) <expire>
Example Player: /check 100 dummyPlayer bank 12m(d,m,s,h)
Example Business: /check 100 dummyBusiness shared 12m(d,m,s,h)
Note for businesses the business must exist and you your self must be in a business

------------

#### Tax Command
- /tax help - shows the help commands
- /tax withdraw - withdraw money from the government account
- /tax balance - see how much money the gov account has
- /tax info - shows the current tax rate and the id for government account
- /tax rate - set the tax rate 

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
business-rate: 1.5       # percent
business-cap: 10000.0    # max daily gain per business
player-rate: 0.1         # percent player rate

withdraw:
daily-limit: 5000.0      # max per player per day
enabled: true

bank-notes:
enabled: true

tax:
enabled: true            # whether the tax system is active
rate: 0.1                # 10% tax on earnings (as a decimal)
recipient: "00000000-0000-0000-0000-000000000001"  # UUID of the government/server account DO NOT CHANGE
apply-on-interest: true  # apply tax on daily interest earnings
apply-on-transfers: false # apply tax on player-to-player or business transfers


------------
Added Bank notes so the withdraw command for the wallet has a use
tweaked the intrest system a little bit 







