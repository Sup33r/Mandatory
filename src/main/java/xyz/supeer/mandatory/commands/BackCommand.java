package xyz.supeer.mandatory.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.utils.TeleportUtil;

import java.util.HashMap;

public class BackCommand implements CommandExecutor {
    private HashMap<Player, Location> playerLocation = new HashMap<>();
    private final Main plugin;

    public BackCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("back").setExecutor((CommandExecutor) this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("mandatory.command.back")) {
            p.sendMessage("§cÅtkomst nekad.");
            return true;
        }
        if (!plugin.backLocations.containsKey(p)) {
            p.sendMessage("§cDu har inte en tillbakaplats.");
            return true;
        }
        playerLocation.put(p,p.getLocation());
        TeleportUtil.Teleport(p.getUniqueId(),plugin.backLocations.get(p),"din tidigare position");
        plugin.backLocations.replace(p,playerLocation.get(p));
        playerLocation.remove(p);
        return false;
    }
}
