package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.MySQL;
import xyz.supeer.mandatory.sql.SQLGetter;
import xyz.supeer.mandatory.utils.TeleportUtil;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class GoCommand implements CommandExecutor {

    private final Main plugin;

    public GoCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("go").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mandatory.command.go")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage("§cPLACEHOLDER LIST MED TELEPORTERINGSPUNKTER");
            return false;
        }
        if (args.length == 1) {
            String name = args[0].toLowerCase();
            if (!SQLGetter.goExists(name)) {
                p.sendMessage("§cHittade inte teleporteringspunkten.");
                return false;
            }
            if (!MySQL.isConnected()) {
                p.sendMessage("§cEtt fel inträffade (Databasen är inte ansluten.)");
                return false;
            }

            TeleportUtil.Teleport(p.getUniqueId(),Bukkit.getWorld(SQLGetter.getWorld(name)), SQLGetter.getXLocation(name), SQLGetter.getYLocation(name), SQLGetter.getZLocation(name), SQLGetter.getYawLocation(name), SQLGetter.getPitchLocation(name), name);
        }

        if (args.length == 2) {
            if (args[1].equalsIgnoreCase("delete")) {
                String name = args[0].toLowerCase();
                if (!SQLGetter.goExists(name)) {
                    p.sendMessage("§cHittade inte teleporteringspunkten.");
                    return false;
                }
                if (p.hasPermission("mandatory.command.go.delete.own")) {
                    if (Objects.equals(SQLGetter.getOwner(name), p.getUniqueId().toString())) {
                        SQLGetter.deleteGo(name);
                        p.sendMessage("§aDu tog bort teleporteringspunkten.");
                        return false;
                    }
                    if (!Objects.equals(SQLGetter.getOwner(name), p.getUniqueId().toString())) {
                        p.sendMessage("§cÅtkomst nekad.");
                        return false;
                    }
                } else
                if (p.hasPermission("mandatory.command.go.delete.others")) {
                    SQLGetter.deleteGo(name);
                    p.sendMessage("§aDu tog bort teleporteringspunkten.");
                    return false;
                } else
                    if (!p.hasPermission("mandatory.command.go.delete.others")) {
                        p.sendMessage("§cÅtkomst nekad.");
                        return false;
                    }
            }

            if (args[1].equalsIgnoreCase("give")) {
                String name = args[0].toLowerCase();
                if (!SQLGetter.goExists(name)) {
                    p.sendMessage("§cHittade inte teleporteringspunkten.");
                    return false;
                }

                if (!p.hasPermission("mandatory.command.go.give.own")) {

                } else
                if (p.hasPermission("mandatory.command.go.give.others")) {
                    String targetName = args[3];
                    Player t = Bukkit.getPlayer(targetName);
                    if (!t.hasPlayedBefore()) {
                        p.sendMessage("§cSpelaren hittades inte.");
                        return false;
                    }
                    SQLGetter.changeOwner(name, UUID.fromString(args[3]));
                    p.sendMessage("§a" + t.getDisplayName() + " äger nu teleporteringspunkten §2" + name + "§a.");
                    return false;
                } else
                if (!p.hasPermission("mandatory.command.go.give.others")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
            }

            if (args[1].equalsIgnoreCase("info")) {

            }

            if (args[1].equalsIgnoreCase("set")) {

            }

            if (args[1].equalsIgnoreCase("toggle")) {

            }

            if (args[1].equalsIgnoreCase("whitelist")) {

            }
        }


        return false;
    }


}
