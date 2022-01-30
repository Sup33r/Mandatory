package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.util.Arrays;

public class MeCommand implements CommandExecutor {

    private final Main plugin;

    public MeCommand(Main plugin) {

        this.plugin = plugin;
        plugin.getCommand("me").setExecutor(this);

    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("mandatory.command.me")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatColor.GRAY + "Syntax: /me " + ChatColor.UNDERLINE + "meddelande");
            return true;
        }

        String msg = String.join(" ", Arrays.copyOfRange(args, 0, args.length));

        plugin.getServer().broadcastMessage("⋆" + " " + sender.getName() + " " + msg );
        return true;

    }



}