package xyz.supeer.mandatory.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();

        if(player.hasPlayedBefore()){
            e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName()+ "");
        }
        else{
            e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName()+ " loggade in för första gången");
        }


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        Player player = e.getPlayer();
        e.setQuitMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "-" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + player.getDisplayName()+ "");

    }

}
