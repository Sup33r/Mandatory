package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class FeedCommand implements CommandExecutor{

    private final Main plugin;

    public FeedCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("feed").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("mandatory.command.feed")){
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utfärda detta kommando.");
            return true;
        }

        Player p = (Player) sender;
        if (p.getFoodLevel() == 20){
            p.sendMessage(ChatColor.RED + "Du är inte hungrig.");
            return true;
        } else{
            p.setFoodLevel(20);
            p.setExhaustion(0F);

            sender.sendMessage(ChatColor.GREEN + "Din hunger är nu mättad.");

            return true;
        }
    }


}