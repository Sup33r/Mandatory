package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.util.Objects;

public class GamemodeCommand implements CommandExecutor {

    private final Main plugin;

    public GamemodeCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("gamemode").setExecutor(this);
    }

    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        if (!sender.hasPermission("mandatory.command.gamemode")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (args.length == 1) {

            if (!sender.hasPermission("mandatory.command.gamemode")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(ChatColor.GRAY + "Syntax: /gamemode " + ChatColor.UNDERLINE + "spelläge");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("Endast spelare kan utfärda detta kommando.");
                return true;
            }


            if (isInt(args[0])) {
                if (Integer.parseInt(args[0]) < 0) {
                    p.sendMessage("§cVänligen ange ett giltigt spelläge.");
                    return true;
                } else if ((Integer.parseInt(args[0]) > 3)) {
                    p.sendMessage("§cVänligen ange ett giltigt spelläge.");
                    return true;
                }
                p.setGameMode(Objects.requireNonNull(GameMode.getByValue(Integer.parseInt(args[0]))));
            } else if (!isInt(args[0])) {
                if (args[0].equalsIgnoreCase("spectator")) {
                p.setGameMode(GameMode.SPECTATOR);
                } else  if (args[0].equalsIgnoreCase("creative")) {
                    p.setGameMode(GameMode.CREATIVE);
                } else  if (args[0].equalsIgnoreCase("adventure")) {
                    p.setGameMode(GameMode.ADVENTURE);
                } else  if (args[0].equalsIgnoreCase("survival")) {
                    p.setGameMode(GameMode.SURVIVAL);

                } else {
                    p.sendMessage("§cVänligen ange ett giltigt spelläge.");
                    return true;
                }

            }

            p.sendMessage(ChatColor.AQUA + "Ditt spelläge har ändrats till: " + p.getGameMode() + ".");
        } else if (args.length == 2) {

            if (!sender.hasPermission("mandatory.command.gamemode.others")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return true;
            }
            Player t = Bukkit.getPlayerExact(args[1]);

            if (t == p){
                p.setGameMode(GameMode.getByValue(Integer.parseInt(args[0])));

                p.sendMessage(ChatColor.AQUA + "Ditt spelläge har ändrats till: " + p.getGameMode() + ".");
                return true;
            }

            if (!(t instanceof Player)) {
                p.sendMessage(ChatColor.RED + "Spelaren kunde inte hittas.");
                return true;
            }

            t.setGameMode(GameMode.getByValue(Integer.parseInt(args[0])));
            p.sendMessage(ChatColor.AQUA + "Spelläge för " + t.getDisplayName() + " har ändrats till: " + t.getGameMode() + ".");
        }

        return true;

    }

}

