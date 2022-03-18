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

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

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
                 double money = SQLGetter.getBalance(p.getUniqueId());
                 String dmoney = String.format("%,.0f",money);
                 String fmoney = String.valueOf(dmoney).replace(","," ");
            p.sendMessage(ChatColor.GREEN + "Saldo: " + fmoney + " minemynt");
            return true;
             } else
             if (args.length == 1) {
                 if (!sender.hasPermission("mandatory.command.balance.others")) {
                     sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                     return true;
                 }
                 if (!MySQL.isConnected()) {
                     p.sendMessage(ChatColor.RED + "Saldot för den specifierade spelaren kan inte hämtas just nu.");
                     return true;
                 }
                 OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                 UUID playerUUID = target.getUniqueId();
                 if (!SQLGetter.balanceExists(playerUUID)) {
                     p.sendMessage("§cDen angivna spelaren hittades inte.");
                     return false;
                 }
                 if (playerUUID == p.getUniqueId()){
                     double money = SQLGetter.getBalance(p.getUniqueId());
                     String dmoney = String.format("%,.0f",money);
                     String fmoney = String.valueOf(dmoney).replace(","," ");
                     p.sendMessage(ChatColor.GREEN + "Saldo: " + fmoney + " minemynt");
                     return true;
                 }
                 double money = SQLGetter.getBalance(playerUUID);
                 String dmoney = String.format("%,.0f",money);
                 String fmoney = String.valueOf(dmoney).replace(","," ");
                 p.sendMessage(ChatColor.GREEN + "Saldo för " + target.getName() + ": " + fmoney + " minemynt");
                 return true;

                 }

            if (args.length == 2) {
                if (!sender.hasPermission("mandatory.command.balance.modify")) {
                    sender.sendMessage(ChatColor.RED + "Åtkomst nekad.");
                    return true;
                }
                if (!MySQL.isConnected()) {
                    p.sendMessage(ChatColor.RED + "Saldot för den specifierade spelaren kan inte hämtas / modifieras just nu.");
                    return true;
                }

                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                UUID playerUUID = target.getUniqueId();
                if (!SQLGetter.balanceExists(playerUUID)) {
                    p.sendMessage("§cDen angivna spelaren hittades inte.");
                    return false;
                }

                if (args[1].startsWith("-")) {
                    if (SQLGetter.getBalance(target.getUniqueId()) == 0) {
                        p.sendMessage("§cDen angivna spelaren har inga minemynt att konfiskera");
                        return false;
                    }
                    if (Integer.parseInt(args[1].replace("-", "")) > SQLGetter.getBalance(target.getUniqueId())) {
                        p.sendMessage("§cDen angivna spelaren har inte så mycket minemynt. Överväg att använda /balance §nnamn§r§c all istället.");
                        return false;
                    }
                    SQLGetter.removeBalance(playerUUID, Integer.parseInt(args[1].replace("-", "")));
                    double money = Integer.parseInt(args[1].replace("-",""));
                    String dmoney = String.format("%,.0f",money);
                    String fmoney = String.valueOf(dmoney).replace(","," ");
                    p.sendMessage("§a" + fmoney + " minemynt konfiskerades från " + target.getName() + ".");
                    return false;
                } else

                if (args[1].startsWith("+")) {
                    SQLGetter.addBalance(playerUUID, Integer.parseInt(args[1].replace("+", "")));
                    double money = Integer.parseInt(args[1].replace("+",""));
                    String dmoney = String.format("%,.0f",money);
                    String fmoney = String.valueOf(dmoney).replace(","," ");
                    p.sendMessage("§a" + fmoney + " minemynt gavs till " + target.getName() + ".");
                } else

                if (args[1].equalsIgnoreCase("all")) {
                    if (SQLGetter.getBalance(target.getUniqueId()) == 0) {
                        p.sendMessage("§cDen angivna spelaren har inga minemynt att konfiskera.");
                        return false;
                    }
                    double money = SQLGetter.getBalance(target.getUniqueId());
                    String dmoney = String.format("%,.0f",money);
                    String fmoney = String.valueOf(dmoney).replace(","," ");
                    p.sendMessage("§aAlla minemynt (" + fmoney + ") konfiskerades från " + target.getName() + ".");
                    SQLGetter.removeBalance(target.getUniqueId(),SQLGetter.getBalance(target.getUniqueId()));
                } else {
                    p.sendMessage("§7Syntax: /balance §nnamn§r§7 §n-mängd§r§7/§n+mängd§r§7/§nall");
                    return false;
                }

                return false;
            }

            return false;
        }
             


        }


