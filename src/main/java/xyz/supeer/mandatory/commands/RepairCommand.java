package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.utils.RepairUtils;

public class RepairCommand implements CommandExecutor {

    private final Main plugin;

    public RepairCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("repair").setExecutor(this);
    }



    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;

        if (!sender.hasPermission("mandatory.command.repair")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utföra detta kommando.");
            return true;
        }

        RepairUtils.repairhand(p);
        p.sendMessage(ChatColor.GREEN + "Föremålet i din hand har reparerats.");

        return true;
    }
}
