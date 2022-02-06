package xyz.supeer.mandatory.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.supeer.mandatory.commands.KickCommand;


public class JoinLeaveListener implements Listener {

    @EventHandler
    public void OnJoin (PlayerJoinEvent e) {
        Player player = e.getPlayer();
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
    public void onLeave(PlayerQuitEvent e){

        Player player = e.getPlayer();

        if (KickCommand.kicked) {
            e.setQuitMessage("§4[§c-§4] §6" + player.getDisplayName() + " fick en varning");
            KickCommand.kicked = false;
            return;
        }

        if (!player.hasPermission("mandatory.hide.join")) {
            e.setQuitMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "-" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + player.getDisplayName()+ "");
        } else
            e.setQuitMessage("");

    }

}
