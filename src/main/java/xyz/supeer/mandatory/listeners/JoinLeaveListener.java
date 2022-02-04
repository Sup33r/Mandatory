package xyz.supeer.mandatory.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.supeer.mandatory.commands.KickCommand;
import xyz.supeer.mandatory.sql.MySQL;
import xyz.supeer.mandatory.sql.SQLGetter;

public class JoinLeaveListener implements Listener {

    public MySQL SQL;
    public SQLGetter data;

    @EventHandler
    public void onJoin(PlayerJoinEvent e){


        Player player = e.getPlayer();

        data.createPlayer(player);



        if(player.hasPlayedBefore()){
            e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName()+ "");
        }
        else{
            e.setJoinMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "+" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + player.getDisplayName()+ " loggade in för första gången.");
        }


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){

        Player player = e.getPlayer();

        if (KickCommand.kicked) {
        e.setQuitMessage("§4[§c-§4] §6" + player.getDisplayName() + " fick en varning");
        KickCommand.kicked = false;
        return;
        }

        e.setQuitMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "-" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + player.getDisplayName()+ "");

    }

}
