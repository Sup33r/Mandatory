package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.util.ArrayList;
import java.util.Arrays;

public class HelpopCommand implements CommandExecutor {

    private final Main plugin;

    public HelpopCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("helpop").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mandatory.command.helpop")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage("§7Syntax: /helpop §ndin fråga");
            return false;
        }
        String msg = String.join(" ", Arrays.copyOfRange(args, 0, args.length));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("mandatory.modchat")) {
                player.sendMessage("§4#modzone §7| §c" + p.getDisplayName() + " behöver hjälp: " + msg);
                p.sendMessage("§aDitt meddelande har skickats till våra moderatorer, och de kontaktar dig så fort det finns tid och möjlighet. Du kan alltid komma i kontakt med oss genom att skapa ett ärende på hemsidan: https://kottcraft.se/support/new");
                return false;
            } else {
                p.sendMessage("§cTyvärr så är ingen personalmedlem online för tillfället. Överväg istället att göra ett supportärende. https://kottcraft.se/support/new");
                return false;
            }
        }


        return false;
    }
}
