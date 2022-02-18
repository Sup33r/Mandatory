package xyz.supeer.mandatory.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
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


        return false;
    }
}
