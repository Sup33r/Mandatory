package xyz.supeer.mandatory.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.supeer.mandatory.Main;

public class VisionCommand implements CommandExecutor {

    private final Main plugin;

    public VisionCommand(Main plugin) {

        this.plugin = plugin;
        plugin.getCommand("vision").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("mandatory.command.vision")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Endast spelare kan utfärda detta kommando.");
            return true;
        }

        Player p = (Player) sender;
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 12000, 2, false , false));
        p.sendMessage(ChatColor.GREEN + "Du har nu fått 10 minuter av synlighet.");

        return true;
    }

}
