package xyz.supeer.mandatory.commands;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utföra detta kommando.");
        }

        Player p = (Player) sender;
        if (!p.hasPermission("mandatory.command.speed")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Var vänlig ange en hastighet mellan 1 och 10.");
            return false;
        }
        int speed = 2;
        try {
           speed = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            p.sendMessage(ChatColor.RED + "Var vänlig ange ett giltigt tal mellan 1 och 10.");
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
        return false;
    }

}

