# Mandatory
Ett Minecraft plugin som är högst experimentalt. Har några enklare funktioner från ett av Minecraftservern Kottcraft's plugins.

## Varning!

Detta insticksprogram KRÄVER att man har en anslutning till en MySQL databas för att pluginet ens ska kunna starta. De inställningarna lägger man in under internals.yml. Om det skulle vara så att någon inställning blir fel kommer inte programmet att starta, och ett felmeddelande kommer upp.

### Depends

För att kunna köra Mandatory behöver pluginet NBTAPI också vara installerat.

### Kommandolista

- /admin
- /balance
- /broadcast
- /buy
- /feed
- /fly
- /gamemode
- /go
- /helpop
- /kick
- /love
- /modchat
- /me
- /message
- /player
- /repair
- /reply
- /spawn
- /teleportask
- /teleportaccept
- /teleportdeny
- /vision

### Andra funktioner

- Varnar i modchat när Minecrafts automatiska flygfusk system upptäcker flygfusk
- Prefix och chattmodifikationer med klick och hoverbara namn
- Avaktiverar raketer för alla utan rättighetsflaggan mandatory.allow.boost
- Stänger av kopieringen av kopieringsskyddade kartor och böcker (OBS Experimentellt)
- Gömmer inloggnings och utloggningsmeddelandet för spelarna med rättighetsflaggorna mandatory.hide.join och mandatory.hide.quit
- Det mesta är implementerat med en databas loggning, men detta är just nu högst experimentellt.

### Rättighetsflaggor

- mandatory.command.admin
- mandatory.command.balance
- mandatory.command.balance.others
- mandatory.command.balance.modify
- mandatory.command.broadcast
- mandatory.command.feed
- mandatory.command.feed.others
- mandatory.command.fly
- mandatory.command.fly.others
- mandatory.command.gamemode
- mandatory.command.gamemode.others
- mandatory.command.kick
- mandatory.command.love
- mandatory.command.me
- mandatory.command.message
- mandatory.command.modchat
- mandatory.command.player
- mandatory.command.repair
- mandatory.command.reply
- mandatory.command.speed
- mandatory.command.speed.others
- mandatory.command.vision
- mandatory.hide.join
- mandatory.hide.quit
- mandatory.allow.boost
- mandatory.command.go
- mandatory.command.go.delete.own
- mandatory.command.go.delete.others
- mandatory.command.go.give.own
- mandatory.command.go.give.others
- mandatory.teleport.nowait
- mandatory.command.buy.head
- mandatory.command.buy.go
- mandatory.command.buy
- mandatory.command.spawn
- mandatory.command.admin.set.firstspawn
- mandatory.command.admin.set.spawn
- mandatory.command.admin.set.afk
- mandatory.command.teleportrequest

- admin
- moderator
- helper
- vip
- ledning
