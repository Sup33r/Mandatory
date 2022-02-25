package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.utils.TeleportUtil;

public class TeleportAcceptCommand implements CommandExecutor {

    private final Main plugin;

    public TeleportAcceptCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("teleportaccept").setExecutor(this);
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

        if (args.length == 0) {
            if (Main.getInstance().getTpa().containsKey(p)) {
                Player p1 = Main.getInstance().getTpa().get(p);
                if (p1 != null && p1.isOnline()) {
                    p1.sendMessage("§bDin teleporteringsförfrågan till " + p.getDisplayName() + " har accepterats.");
                    p.sendMessage("§aTeleporteringsförfrågan har accepterats.");
                    TeleportUtil.Teleport(p1.getUniqueId(),p.getLocation(), p.getDisplayName());
                    Main.getInstance().getTpa().remove(p);
                } else {
                    p.sendMessage("§cSpelaren du vill teleportera till är inte online.");
                    Main.getInstance().getTpa().remove(p);
                    return false;
                }
            } else {
                p.sendMessage("§cDu har inga teleporteringsförfrågningar att acceptera.");
            }
        } else {
            p.sendMessage("§7Syntax: /teleportaccept");
            return false;
        }

        return false;
    }
}
