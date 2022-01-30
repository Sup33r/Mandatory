package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class HejCommand implements CommandExecutor {

    private final Main plugin;
    public HejCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("hej").setExecutor(this);


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utfärda detta kommando.");
            return true;
        }
        Player p = (Player) sender;
        p.sendMessage("Tjo tjo! :)");
        p.setHealth(20);
        if (p.hasPermission("mandatory.command.hej")){

            return true;
        }  else {
            p.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

    }
}

