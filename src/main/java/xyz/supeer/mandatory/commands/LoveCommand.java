package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;

import java.util.Arrays;

public class LoveCommand implements CommandExecutor {

    private final Main plugin;

    public LoveCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("love").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;


        if(!sender.hasPermission("mandatory.command.love.others")){
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utfärda detta kommando.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(ChatColor.GRAY + "Syntax: /love " + ChatColor.UNDERLINE + "spelare" + ChatColor.RESET + ChatColor.GRAY + " [" + ChatColor.UNDERLINE + "anledning" + ChatColor.RESET + ChatColor.GRAY + "]");
        }

        if (args.length == 1) {
            Player t = Bukkit.getPlayerExact(args[0]);
            if (!(t instanceof Player)) {
                p.sendMessage(ChatColor.RED + "Hittade ingen online med det namnet.");
                return true;
            }

            if (t == p) {
                p.sendMessage(ChatColor.RED + "Du kan inte skicka kärlekshälsningar till dig själv...");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "Kärleken har framgångsrikt skickats till " + t.getDisplayName() +  "!");
            SQLGetter.addLoves(t.getUniqueId(), 1);
            t.sendMessage(ChatColor.DARK_RED + "❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤");
            t.sendMessage(ChatColor.GREEN + "Du har fått en kärlekshälsning från " + ChatColor.BOLD + p.getDisplayName() + "!");
            t.sendMessage(ChatColor.DARK_RED + "❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤");

        } else
        if (args.length >= 2) {
            Player t = Bukkit.getPlayerExact(args[0]);
            if (!(t instanceof Player)) {
                p.sendMessage(ChatColor.RED + "Hittade ingen online med det namnet.");
                return true;
            }

            if (t == p) {
                p.sendMessage(ChatColor.RED + "Du kan inte skicka kärlekshälsningar till dig själv...");
                return true;
            }

            p.sendMessage(ChatColor.GREEN + "Kärleken har framgångsrikt skickats till " + t.getDisplayName() +  "!");
            SQLGetter.addLoves(t.getUniqueId(), 1);
            String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

            t.sendMessage(ChatColor.DARK_RED + "❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤");
            t.sendMessage(ChatColor.GREEN + "Du har fått en kärlekshälsning från " + ChatColor.BOLD + p.getDisplayName() + ChatColor.RESET + ChatColor.GREEN + "!");
            t.sendMessage(ChatColor.DARK_GREEN + "Anledning: " + ChatColor.GREEN + msg);
            t.sendMessage(ChatColor.DARK_RED + "❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤ ❤");

        }

        return true;
    }
}
