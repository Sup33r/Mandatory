package xyz.supeer.mandatory.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;

import java.util.Locale;

public class BuyCommand implements CommandExecutor {

    private Main plugin;

    public BuyCommand(Main plugin) {
    this.plugin = plugin;
    plugin.getCommand("buy").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("mandatory.command.buy")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("§7Syntax: /buy go §nnamn");
            return false;
        }
        if (args.length == 1) {
            if ("go".equals(args[0])) {
                p.sendMessage("§7Syntax: /buy go §nnamn");
                return false;
            }
        }
        if (args.length == 2) {
            if ("go".equals(args[0])) {
                String name = args[1].toLowerCase();
                if (SQLGetter.goExists(name)) {
                    p.sendMessage("§cNamnet används av en annan teleporteringspunkt.");
                    return false;
                }
                if (!p.hasPermission("mandatory.command.buy.go")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
                Location loc = p.getLocation();
                if (SQLGetter.getBalance(p.getUniqueId()) >= 15000) {
                    SQLGetter.removeBalance(p.getUniqueId(), 15000);
                    SQLGetter.addGo(loc.getWorld().getName(),name, loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw(), p.getUniqueId());
                    p.sendMessage("§aDu har köpt en teleporteringspunkt för 15 000 minemynt.");
                    return false;
                } else
                    if (SQLGetter.getBalance(p.getUniqueId()) <= 15000) {
                    p.sendMessage("§cDu har inte råd med 15 000 minemynt, som en teleporteringspunkt kostar.");
                    return false;
                    }

            }
        }

        return false;
    }
}
