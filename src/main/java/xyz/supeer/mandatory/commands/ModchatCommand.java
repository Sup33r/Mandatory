package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class ModchatCommand implements CommandExecutor {

    private final Main plugin;

    public ModchatCommand(Main plugin){
        this.plugin = plugin;
        plugin.getCommand("modchat").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Detta kommando kan endast användas av spelare");
            return false;
        }
        Player p = (Player) sender;
        if (!(p.hasPermission("mandatory.modchat"))) {
            p.sendMessage("§cÅtkomst nekad");
            return false;
        }

        if (args.length < 1) {
            //Här kommer switch saken senare, den som byter mellan chattkanalerna.
            p.sendMessage("§cMeddelandet kan inte vara tomt.");
            return false;
        }

        if (args[0] == "help") {
            
        }

        String message = "§4#modzone §7| §6" + p.getDisplayName() + "§7> §c";
        for (String s : args) {
            message = message + s + " ";
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("mandatory.modchat")) {
                player.sendMessage(message);
            }
        }

        return false;
    }
}
