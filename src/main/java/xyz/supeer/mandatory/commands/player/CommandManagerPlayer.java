package xyz.supeer.mandatory.commands.player;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CommandManagerPlayer implements CommandExecutor {



    private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
    private Main plugin = Main.getInstance();

    public CommandManagerPlayer() {
    }

    // Subcommands
    public String main = "player";
    public String help = "help";

    public void setup() {
        plugin.getCommand(main).setExecutor(this);

        this.commands.add(new HelpCommand());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("mandatory.command.player")) {
            sender.sendMessage(ChatColor.RED + "Åtkomst nekad");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Endast spelare kan utfärda kommandon för detta plugin.");
            return true;
        }

        Player p = (Player) sender;

        if (command.getName().equalsIgnoreCase(main)) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.DARK_GREEN + "--- " + ChatColor.GREEN + "Spelare: " + p.getDisplayName() + ChatColor.DARK_GREEN + " ---");
                p.sendMessage(ChatColor.DARK_GREEN + "Spelat sedan: " + ChatColor.GREEN + "PLACEHOLDER");
                if (p.isOnline()) { p.sendMessage(ChatColor.DARK_GREEN + "Status: " + ChatColor.GREEN + "Online, sedan " + "PLACEHOLDER"); }
                else { p.sendMessage(ChatColor.DARK_GREEN + "Status: " + ChatColor.GREEN + "Online, sedan " + "PLACEHOLDER"); }
                p.sendMessage(ChatColor.DARK_GREEN + "Senaste positionen: " + ChatColor.GREEN + p.getLocation());
                double money = SQLGetter.getBalance(p.getUniqueId());
                String dmoney = String.format("%,.0f",money);
                String fmoney = String.valueOf(dmoney).replace(","," ");
                p.sendMessage(ChatColor.DARK_GREEN + "Plånbok: " + ChatColor.GREEN + fmoney + " minemynt");
                p.sendMessage(ChatColor.DARK_GREEN + "Stadsmedlemskap: " + ChatColor.GREEN + "PLACEHOLDER, PLACEHOLDER2 (assistent) & PLACEHOLDER3 (borgmästare)");
                return false;
            }

            SubCommand target = this.get(args[0]);

            if (this.get(args[0]) == null) {
                p.sendMessage("§7Okänt kommando. Skriv §n/player help§r§7 för en lista på kommandon.");
                return true;
            }

            ArrayList<String> arrayList = new ArrayList<String>();

            arrayList.addAll(Arrays.asList(args));
            arrayList.remove(0);

            try {
                target.onCommand(p,args);
            } catch (Exception e) {
                p.sendMessage(ChatColor.RED + "Ett fel har uppstått.");

                e.printStackTrace();
            }
        }
        return true;
    }

    private SubCommand get(String name) {
        Iterator<SubCommand> subcommands = this.commands.iterator();

        while (subcommands.hasNext()) {
            SubCommand sc = (SubCommand) subcommands.next();

            if (sc.name().equalsIgnoreCase(name)) {
                return sc;
            }
            String[] aliases;
            int length = (aliases = sc.aliases()).length;

            for (int var5 = 0; var5 < length; ++ var5) {
                String alias = aliases[var5];
                if (name.equalsIgnoreCase(alias)) {
                    return sc;
                }
            }
        }
        return null;
    }

}
