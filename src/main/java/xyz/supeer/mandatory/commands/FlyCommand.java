package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class FlyCommand implements CommandExecutor {

    private final Main plugin;
    public FlyCommand(Main plugin){

        this.plugin = plugin;
        plugin.getCommand("fly").setExecutor(this);


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {

            if (!sender.hasPermission("mandatory.command.fly")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return true;
            }

            if (!(sender instanceof Player)) {
                sender.sendMessage("Endast spelare kan utfärda detta kommando.");
                return true;
            }


            Player p = (Player) sender;
            if (p.getAllowFlight() == false) {
                p.setAllowFlight(true);
                p.sendMessage(ChatColor.AQUA + "Flygläge aktiverat");
                return true;
            }

            p.setAllowFlight(false);
            p.sendMessage(ChatColor.AQUA + "Flygläge avaktiverat");

        } else if (args.length == 1) {
            if (!sender.hasPermission("mandatory.command.fly.others")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return true;
            }

            Player t = Bukkit.getPlayerExact(args[0]);

            if (t.getAllowFlight() == false) {
                t.setAllowFlight(true);
                sender.sendMessage(ChatColor.AQUA + "Flygläge aktiverat för " + t.getDisplayName() + ".");
                return true;
            }

            t.setAllowFlight(false);
            sender.sendMessage(ChatColor.AQUA + "Flygläge avaktiverat för " + t.getDisplayName() + ".");
        }
        return false;
    }

}
