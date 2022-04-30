package xyz.supeer.mandatory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.commands.KickCommand;
import xyz.supeer.mandatory.commands.MessageCommand;
import xyz.supeer.mandatory.commands.TeleportAskCommand;
import xyz.supeer.mandatory.sql.SQLGetter;


public class JoinLeaveListener implements Listener {

    private final Main plugin;

    public JoinLeaveListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void OnJoin (PlayerJoinEvent e) {

        Player player = e.getPlayer();
        if (!SQLGetter.playerExists(player.getUniqueId())) {
            SQLGetter.createPlayer(player);
        } else {
            SQLGetter.updatePlayerJoin(player);
        }
        player.sendTitle("Tjuhu, " + player.getDisplayName() + "!", "Välkommen till Kottcraft.", 0, 70,20);

        if (!player.hasPermission("mandatory.hide.join")) {

            if (player.hasPlayedBefore()) {
                e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName() + "");
            } else {
                e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName() + " loggade in för första gången.");
            }
        } else
        e.setJoinMessage("");
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Main.getInstance().getTpa().remove(e.getPlayer());
        MessageCommand.lastMessageSender.remove(e.getPlayer());

        if (plugin.afkPlayers.containsKey(e.getPlayer())) {
            plugin.afkPlayers.remove(e.getPlayer());
        }

        Player player = e.getPlayer();
        if (SQLGetter.playerExists(player.getUniqueId())) {
            SQLGetter.updatePlayerLeave(player);
        }
        if (!plugin.kickedPlayers.containsKey(player)) {
            if (!player.hasPermission("mandatory.hide.quit")) {
                e.setQuitMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "-" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + player.getDisplayName() + "");
            } else {
                e.setQuitMessage("");
            }
        } else {
            e.setQuitMessage("§4[§c-§4] §6" + e.getPlayer().getDisplayName() + " fick en varning.");
            plugin.kickedPlayers.remove(player);
        }

    }
}
