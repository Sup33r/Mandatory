package xyz.supeer.mandatory.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.utils.TeleportUtil;

import java.util.List;

public class SpawnCommand implements CommandExecutor {
    private final Main plugin;

    public SpawnCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }

        Player p = (Player) sender;

        if (!p.hasPermission("mandatory.command.spawn")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }

        String name = "startpunkten";
        Location location = plugin.getCustomConfig().getLocation("spawn");

        if (location == null ) {
            p.sendMessage("§cIngen startpunkt är satt.");
        } else {
            List<String> afkMessages = plugin.afkPlayers.get(p);

            if (afkMessages != null) {
                for (String afkMessage : afkMessages) {
                    p.sendMessage(afkMessage);
                }
            }
            plugin.afkPlayers.remove(p);
            TeleportUtil.Teleport(p.getUniqueId(), location, name);
        }


        return false;
    }
}
