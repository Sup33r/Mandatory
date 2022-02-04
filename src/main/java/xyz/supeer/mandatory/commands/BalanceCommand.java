package xyz.supeer.mandatory.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.MySQL;
import xyz.supeer.mandatory.sql.SQLGetter;

import java.util.UUID;

import static org.bukkit.Bukkit.*;

public class BalanceCommand implements CommandExecutor {



        private final Main plugin;

        public BalanceCommand(Main plugin) {
            this.plugin = plugin;
            plugin.getCommand("balance").setExecutor((CommandExecutor) this);
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

            Player p = (Player) sender;

            if (!sender.hasPermission("mandatory.command.balance")) {
                sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                return true;
            }

             if (args.length == 0) {

             if (!MySQL.isConnected()) {
              p.sendMessage(ChatColor.RED + "Ditt saldo kan inte hämtas just nu.");
                return true;
              }
            p.sendMessage(ChatColor.GREEN + "Saldo: " + SQLGetter.getBalance(p.getUniqueId()) + " minemynt");
            return true;
             } else
             if (args.length == 1) {

                 if (!sender.hasPermission("mandatory.command.balance.others")) {
                     sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                     return true;
                 }

                 Player t = Bukkit.getPlayerExact(args[0]);

                 if (t == p){
                     p.sendMessage(ChatColor.GREEN + "Saldo: " + SQLGetter.getBalance(t.getUniqueId()) + " minemynt");
                     return true;
                 }

                 if (!(t instanceof Player)) {
                     p.sendMessage(ChatColor.RED + "Spelaren kunde inte hittas.");
                     return true;
                 }

                 p.sendMessage(ChatColor.GREEN + "Saldo för " + t.getDisplayName() + ": " + SQLGetter.getBalance(t.getUniqueId()) + " minemynt");
                 return true;

                 }

            return false;
        }
             


        }


