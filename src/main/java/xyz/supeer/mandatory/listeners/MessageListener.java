package xyz.supeer.mandatory.listeners;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class MessageListener implements Listener {
    static ConsoleCommandSender console = Bukkit.getConsoleSender();
    @EventHandler
    public void onMessage(AsyncPlayerChatEvent e) {
        console.sendMessage("[CHAT] " + e.getPlayer().getDisplayName() + ": " + e.getMessage());
        e.setCancelled(true);
        if (e.getPlayer().hasPermission("ledning")) {
            TextComponent prefix = new TextComponent("§0[§dA§0]§r");
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§dLedningsmedlem").create()));

            TextComponent beforename = new TextComponent(" ");

            TextComponent name = new TextComponent("§6" + e.getPlayer().getDisplayName());
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new  ComponentBuilder("§bÖppna spelarens profil på hemsidan").create()));
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kottcraft.se/player/" + e.getPlayer().getUniqueId()));
            TextComponent aftername = new TextComponent("§7> §r");
            TextComponent message = new TextComponent(e.getMessage());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(prefix, beforename ,name, aftername ,message);
            }

        } else if (e.getPlayer().hasPermission("admin")) {
            TextComponent prefix = new TextComponent("§3[§bA§3]§r");
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bAdministratör").create()));

            TextComponent beforename = new TextComponent(" ");

            TextComponent name = new TextComponent("§6" + e.getPlayer().getDisplayName());
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new  ComponentBuilder("§bÖppna spelarens profil på hemsidan").create()));
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kottcraft.se/player/" + e.getPlayer().getUniqueId()));
            TextComponent aftername = new TextComponent("§7> §r");
            TextComponent message = new TextComponent(e.getMessage());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(prefix, beforename ,name, aftername ,message);
            }

        } else if (e.getPlayer().hasPermission("moderator")) {
            TextComponent prefix = new TextComponent("§3[§bM§3]§r");
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bModerator").create()));

            TextComponent beforename = new TextComponent(" ");

            TextComponent name = new TextComponent("§6" + e.getPlayer().getDisplayName());
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new  ComponentBuilder("§bÖppna spelarens profil på hemsidan").create()));
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kottcraft.se/player/" + e.getPlayer().getUniqueId()));
            TextComponent aftername = new TextComponent("§7> §r");
            TextComponent message = new TextComponent(e.getMessage());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(prefix, beforename ,name, aftername ,message);
            }

        } else if (e.getPlayer().hasPermission("helper")) {
            TextComponent prefix = new TextComponent("§3[§bC§3]§r");
            prefix.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§bChattvakt").create()));

            TextComponent beforename = new TextComponent(" ");

            TextComponent name = new TextComponent("§6" + e.getPlayer().getDisplayName());
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new  ComponentBuilder("§bÖppna spelarens profil på hemsidan").create()));
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kottcraft.se/player/" + e.getPlayer().getUniqueId()));
            TextComponent aftername = new TextComponent("§7> §r");
            TextComponent message = new TextComponent(e.getMessage());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(prefix, beforename ,name, aftername ,message);
            }

        } else {
            TextComponent name = new TextComponent("§6" + e.getPlayer().getDisplayName());
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new  ComponentBuilder("§bÖppna spelarens profil på hemsidan").create()));
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://kottcraft.se/player/" + e.getPlayer().getUniqueId()));
            TextComponent aftername = new TextComponent("§7> §r");
            TextComponent message = new TextComponent(e.getMessage());

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().sendMessage(name, aftername ,message);
            }

        }

        Player p = (Player) e.getPlayer();
        if (e.getPlayer().hasPermission("ledning")) {
            String prefixVariable = "§0[§dA§0]§r ";
            String suffixVariable = "";
            p.setPlayerListName(prefixVariable + p.getDisplayName() + suffixVariable);
        } else if (e.getPlayer().hasPermission("admin")) {
            String prefixVariable = "§3[§bA§3]§r ";
            String suffixVariable = "";
            p.setPlayerListName(prefixVariable + p.getDisplayName() + suffixVariable);
        } else if (e.getPlayer().hasPermission("moderator")) {
            String prefixVariable = "§3[§bM§3]§r ";
            String suffixVariable = "";
            p.setPlayerListName(prefixVariable + p.getDisplayName() + suffixVariable);
        } else if (e.getPlayer().hasPermission("helper")) {
            String prefixVariable = "§3[§bC§3]§r ";
            String suffixVariable = "";
            p.setPlayerListName(prefixVariable + p.getDisplayName() + suffixVariable);
        } else if (!p.hasPermission("helper") || !p.hasPermission("moderator") || !p.hasPermission("admin") || !p.hasPermission("ledning")) {
            p.setPlayerListName(p.getDisplayName());
        }

    }

}
