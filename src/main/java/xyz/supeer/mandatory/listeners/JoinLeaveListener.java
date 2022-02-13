package xyz.supeer.mandatory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.supeer.mandatory.commands.KickCommand;
import xyz.supeer.mandatory.commands.MessageCommand;


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
        MessageCommand.lastMessageSender.remove(e.getPlayer());

        Player player = e.getPlayer();

        if (KickCommand.kicked) {

            KickCommand.kicked = false;
            return;
        }

        if (!player.hasPermission("mandatory.hide.quit")) {
            e.setQuitMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "-" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + player.getDisplayName()+ "");
        } else
            e.setQuitMessage("");

    }

    @EventHandler
    public  void onKick(PlayerKickEvent e) {
        e.setLeaveMessage("§4[§c-§4] §6" + e.getPlayer().getDisplayName() + " fick en varning.");
        if (e.getReason().equals("Det verkar som om att du flög. Om det är så att du fuskar råder vi dig att omedelbart sluta med det. Tack!")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("mandatory.modchat")) {
                    player.sendMessage("§4#modzone §7| §cMisstänkt flygfusk hos " + e.getPlayer().getDisplayName() + ".");
                }
            }

        }
    }

}
