package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


public class KickCommand implements CommandExecutor, Listener {

    private final Main plugin;


    public KickCommand(Main plugin) {

        this.plugin = plugin;
        plugin.getCommand("kick").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(!sender.hasPermission("mandatory.command.kick")){
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0 || args.length == 1) {
            p.sendMessage("§7Syntax: /kick §nspelare§r§7 §nanledning");
            return true;
        }

        Player t = Bukkit.getPlayerExact(args[0]);
        if (t == null) {
            p.sendMessage("§cDen angivna spelaren hittades inte.");
            return true;
        }
        String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (Objects.equals(args[1], "spam")) {
            plugin.kickedPlayers.put(t, new ArrayList<String>());
            t.kickPlayer("§cDu har fått en varning:" + "\n" + "§r" + "Vänligen spamma ej i chatten. Tack!");
            SQLGetter.addKick(t.getDisplayName(),t.getUniqueId(),"Vänligen spamma ej i chatten. Tack!",p.getDisplayName(),p.getUniqueId());
            return true;
        }
        plugin.kickedPlayers.put(t, new ArrayList<String>());
        t.kickPlayer("§cDu har fått en varning:" + "\n" + "§r" + msg);
        SQLGetter.addKick(t.getDisplayName(),t.getUniqueId(),msg,p.getDisplayName(),p.getUniqueId());

        return false;
    }

    @EventHandler
    public  void onKick(PlayerKickEvent e) {
        if (e.getReason().equals("Det verkar som om att du flög. Om det är så att du fuskar råder vi dig att omedelbart sluta med det. Tack!")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("mandatory.modchat")) {
                    player.sendMessage("§4#modzone §7| §cMisstänkt flygfusk hos " + e.getPlayer().getDisplayName() + ".");
                }
            }

        }
    }

}



