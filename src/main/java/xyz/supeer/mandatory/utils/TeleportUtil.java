package xyz.supeer.mandatory.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.supeer.mandatory.Main;

import java.util.List;
import java.util.UUID;

public class TeleportUtil implements Listener {
    static Main plugin;
    public TeleportUtil(Main instance) {
        plugin = instance;
    }
    public static List<Player> players = Lists.newArrayList();
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (players.contains(event.getPlayer())) {
            players.remove(event.getPlayer());
            event.getPlayer().sendMessage("§cDen pågående teleporteringen avbröts.");
        }
    }

    @EventHandler
    public void onPlayerDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
        if (players.contains(p.getPlayer())) {
            players.remove(p.getPlayer());
            p.getPlayer().sendMessage("§cDen pågående teleporteringen avbröts.");
        }
        }
    }

    public static boolean Teleport(UUID PlayerUUID, World World, Double X, Double Y, Double Z, Float Yaw, Float Pitch, String Name) {
        Player p = Bukkit.getPlayer(PlayerUUID);

        if (p.hasPermission("mandatory.teleport.nowait")) {
            p.teleport(new Location(World, X, Y, Z, Yaw, Pitch));
            p.sendMessage("§aTeleporterar dig till §2" + Name + "§a...");
            return false;
        }

        if (players.contains(p)) {
            p.sendMessage("§cDu har redan en pågående teleportering.");
            return false;
        }
        players.add(p);
        p.sendMessage("§cStå still! §bDu kommer att teleporteras om 3 sekunder, men bara om du inte rör dig och inte tar skada.");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
            if (players.contains(p)) {
                p.teleport(new Location(World, X, Y, Z, Yaw, Pitch));
                p.sendMessage("§aTeleporterar dig till §2" + Name + "§a...");
            }
            }
        }, 20*3);


        return false;
    }

}
