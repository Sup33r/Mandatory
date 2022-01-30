package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class PlayerCommand implements CommandExecutor {


    private final Main plugin;
    public PlayerCommand(Main plugin){

        this.plugin = plugin;
        plugin.getCommand("player").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utföra detta kommando.");
        }

        Player p = (Player) sender;

        if (!sender.hasPermission("mandatory.command.player")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.DARK_GREEN + "--- " + ChatColor.GREEN + "Spelare: " + p.getDisplayName() + ChatColor.DARK_GREEN + " ---");
            p.sendMessage(ChatColor.DARK_GREEN + "Spelat sedan: " + ChatColor.GREEN + "PLACEHOLDER");
            if (p.isOnline()) { p.sendMessage(ChatColor.DARK_GREEN + "Status: " + ChatColor.GREEN + "Online, sedan " + "PLACEHOLDER"); }
            else { p.sendMessage(ChatColor.DARK_GREEN + "Status: " + ChatColor.GREEN + "Offline, sedan " + "PLACEHOLDER"); }
            p.sendMessage(ChatColor.DARK_GREEN + "Senaste positionen: " + ChatColor.GREEN + p.getLocation());
            p.sendMessage(ChatColor.DARK_GREEN + "Plånbok: " + ChatColor.GREEN + "SQL PLACEHOLDER GET SENDER BALANCE" + " minemynt");
            p.sendMessage(ChatColor.DARK_GREEN + "Stadsmedlemskap: " + ChatColor.GREEN + "PLACEHOLDER, PLACEHOLDER2 (assistent) & PLACEHOLDER3 (borgmästare)");
        }

        return false;
    }


}

