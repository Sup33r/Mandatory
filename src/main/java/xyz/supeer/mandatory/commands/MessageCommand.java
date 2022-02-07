package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.util.HashMap;
import java.util.Map;

public class MessageCommand implements CommandExecutor {
    public static Map<Player, Player> lastMessageSender = new HashMap<>();

    private final Main plugin;

    public MessageCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("message").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player) sender;
            if (!sender.hasPermission("mandatory.command.message")) {
                sender.sendMessage("§cÅtkomst nekad.");
                return false;
            }

            if (args.length < 2) {
                p.sendMessage("§7Syntax /message §nspelare§r §7§nmeddelande");
                return false;
            }

            if (args.length >= 2) {
                String targetName = args[0];
                Player t = Bukkit.getPlayer(targetName);
                String message = getMessage(args, 1);
                if (t != null) {
                    t.sendMessage("§4[§c" + p.getDisplayName() + " -> dig§4] §r" + message);
                    sender.sendMessage("§4[§cdu -> " + t.getDisplayName() + "§4] §r" + message);
                    lastMessageSender.put(t, p);
                } else {
                   sender.sendMessage("§cDen angivna spelaren är inte inloggad.");
                }
            }

        }

        return false;
    }

    static String getMessage(String[] args, int index) {
        StringBuilder sb = new StringBuilder();
        for (int i = index; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        sb .setLength(sb.length() - 1);
        return sb.toString();
    }
}
