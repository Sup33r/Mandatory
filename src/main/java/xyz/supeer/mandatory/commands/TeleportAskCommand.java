package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class TeleportAskCommand implements CommandExecutor {


    private final Main plugin;

    public TeleportAskCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("teleportask").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("mandatory.command.teleportrequest")) {
            sender.sendMessage("§cÅtkomst nekad.");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }

        Player p = (Player) sender;

        if (args.length == 1) {
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 != null && p2.isOnline()) {
                if (p2 == p) {
                    p.sendMessage("§cDu kan inte teleportera till dig själv.");
                    return false;
                }
                if (!Main.getInstance().getTpa().containsValue(p)) {
                    Main.getInstance().getTpa().put(p2,p);

                    p2.sendMessage("§bSpelare " + p.getDisplayName() + " vill teleportera sig till dig.");
                    p2.sendMessage("§bSvara med §3/tpyes §beller §3/tpno§b.");
                    p.sendMessage("§aFörfrågan om teleportering har skickats till " + p2.getDisplayName() + ".");
                    return false;
                } else {
                    Main.getInstance().getTpa().remove(p2,p);
                    p.sendMessage("§aAvbröt teleporteringsförfrågan till " + p2.getDisplayName() + ".");
                    p2.sendMessage("§b" + p.getDisplayName() + " avbröt sin teleporteringsförfrågan.");
                    return false;
                }

            } else {
                p.sendMessage("§cSpelaren du vill teleportera till är inte online.");
                return false;
            }
        } else {
            p.sendMessage("§7Syntax: /teleportask §nspelare");
        }

        return false;
    }

}
