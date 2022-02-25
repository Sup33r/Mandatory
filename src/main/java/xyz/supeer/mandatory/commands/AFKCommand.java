package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;
import xyz.supeer.mandatory.utils.TeleportUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Objects;

public class AFKCommand implements CommandExecutor {

    private final Main plugin;

    public AFKCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("afk").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (!p.hasPermission("mandatory.command.afk")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }
        if (!(args.length == 0)) {
            p.sendMessage("§7Syntax: /afk");
            return false;
        }
        if (plugin.afkPlayers.containsKey(p)) {
            p.sendMessage("§cDu har redan tagit Avstånd Från Kottcrafts stadsvärld (AFK).");
            return false;
        }

        if (plugin.getCustomConfig().getLocation("afk") == null) {
            p.sendMessage("§cDet finns inget definerat afk-rum på den här servern.");
            return false;
        }

        plugin.afkPlayers.put(p, new ArrayList<String>());
        TeleportUtil.Teleport(p.getUniqueId(), plugin.getCustomConfig().getLocation("afk"), "AFK-rummet");

        return false;
    }
}
