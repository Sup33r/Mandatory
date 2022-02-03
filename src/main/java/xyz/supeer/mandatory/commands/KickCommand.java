package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.util.Arrays;
import java.util.Objects;


public class KickCommand implements CommandExecutor {

    private final Main plugin;
    public static boolean kicked = false;

    public KickCommand(Main plugin) {

        this.plugin = plugin;
        plugin.getCommand("kick").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Player p = (Player) sender;
        Player t = Bukkit.getPlayerExact(args[0]);

        if(!sender.hasPermission("mandatory.command.kick")){
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§7Syntax: /kick §nspelare§r§7 §nanledning");
            return true;
        }

        String msg = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        if (Objects.equals(args[1], "spam")) {
            kicked = true;
            t.kickPlayer("§cDu har fått en varning:" + "\n" + "§r" + "Vänligen spamma ej i chatten. Tack!");
            return true;
        }

        t.kickPlayer("§cDu har fått en varning:" + "\n" + "§r" + msg);
        kicked = true;

        //Här ska en MYSQL integration finnas med tid datum och meddelande + typ.

        return false;
    }

}



