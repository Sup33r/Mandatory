package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class SpeedCommand implements CommandExecutor {

    private final Main plugin;

    public SpeedCommand(Main plugin) {

        this.plugin = plugin;
        plugin.getCommand("speed").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (args.length == 0) {
            if (sender.hasPermission("mandatory.command.speed.others")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Endast spelare kan utföra detta kommando.");
                    return false;
                }
                sender.sendMessage("§7/speed §nspelare§r §7§nhastighet");
                return true;
            } else if (sender.hasPermission("mandatory.command.speed")) {
                sender.sendMessage("§7speed §nhastighet");
            } else {
                sender.sendMessage("§cÅtkomst nekad.");
                return false;
            }
        }

        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Endast spelare kan utföra detta kommando.");
                return false;
            }
            Player p = (Player) sender;
            if (!p.hasPermission("mandatory.command.speed")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return false;
            }
            int speed = 2;
            try {
                speed = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                p.sendMessage(ChatColor.RED + "Var vänlig ange ett giltigt tal mellan 1 och 10.");
                return false;
            }
            if (speed < 1 || speed > 10) {
                p.sendMessage(ChatColor.RED + "Var vänlig ange ett giltigt tal mellan 1 och 10.");
                return false;
            }
            if (p.isFlying()) {
                p.setFlySpeed((float) speed / 10);
            } else {
                p.setWalkSpeed((float) speed / 10);
            }
            if (p.isFlying()) {
                p.sendMessage(ChatColor.AQUA + "Flyghastigeten sattes till " + speed);
            } else {
                p.sendMessage(ChatColor.AQUA + "Gånghastigeten sattes till " + speed);
            }
        } else if (args.length == 2) {
            Player t = Bukkit.getPlayerExact(args[0]);
            if (!sender.hasPermission("mandatory.command.speed.others")) {
                sender.sendMessage("§cÅtkomst nekad.");
                return false;
            }
            if (!(t instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Spelaren kunde inte hittas.");
                return false;
            }
            int speed = 2;
            try {
                speed = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Var vänlig ange ett giltigt tal mellan 1 och 10.");
                return false;
            }
            if (speed < 1 || speed > 10) {
                sender.sendMessage(ChatColor.RED + "Var vänlig ange ett giltigt tal mellan 1 och 10.");
                return false;
            }
            if (t.isFlying()) {
                t.setFlySpeed((float) speed / 10);
            } else {
                t.setWalkSpeed((float) speed / 10);
            }
            if (t.isFlying()) {
                sender.sendMessage(ChatColor.AQUA + "Flyghastigeten för " + t.getDisplayName() + " sattes till " + speed);
            } else {
                sender.sendMessage(ChatColor.AQUA + "Gånghastigeten för " + t.getDisplayName() + " sattes till " + speed);
            }
        }
        return false;
    }

}

