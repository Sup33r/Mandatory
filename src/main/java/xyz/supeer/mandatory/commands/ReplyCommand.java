package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import static xyz.supeer.mandatory.commands.MessageCommand.getMessage;

public class ReplyCommand implements CommandExecutor {

    private final Main plugin;

    public ReplyCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("reply").setExecutor((CommandExecutor) this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player p = (Player) sender;
        if (!sender.hasPermission("mandatory.command.reply")) {
            sender.sendMessage("§cÅtkomst nekad.");
            return false;
        }
        if (args.length == 0) {
            p.sendMessage("§7Syntax /reply §nmeddelande");
            return false;
        }


        if (args.length >= 1) {
            Player t = MessageCommand.lastMessageSender.get(sender);
            String message = getMessage(args, 0);
            if (t != null) {
                if (t.isOnline()) {
                    t.sendMessage("§4[§c" + p.getDisplayName() + " -> dig§4] §r" + message);
                    sender.sendMessage("§4[§cdu -> " + t.getDisplayName() + "§4] §r" + message);
                    MessageCommand.lastMessageSender.put(t, p);
                } else {
                    sender.sendMessage("§cDen angivna spelaren är inte inloggad.");
                }
            } else {
                sender.sendMessage("§cDen angivna spelaren är inte inloggad.");
            }
        }


        return false;
    }
}
