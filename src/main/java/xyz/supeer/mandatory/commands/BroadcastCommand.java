package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.supeer.mandatory.Main;

import java.util.Arrays;

public class BroadcastCommand implements CommandExecutor{

    private final Main plugin;
    public BroadcastCommand(Main plugin){

        this.plugin = plugin;
        plugin.getCommand("broadcast").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("mandatory.command.broadcast")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatColor.GRAY + "Syntax: /broadcast " + ChatColor.UNDERLINE + "meddelande");
            return true;
        }

        String msg = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        plugin.getServer().broadcastMessage(" ");
        plugin.getServer().broadcastMessage(ChatColor.AQUA + msg );
        plugin.getServer().broadcastMessage(" ");
        return true;
    }

}
