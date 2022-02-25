package xyz.supeer.mandatory.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class TeleportDenyCommand implements CommandExecutor {


    private final Main plugin;

    public TeleportDenyCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("teleportdeny").setExecutor(this);
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

        return false;
    }

}
