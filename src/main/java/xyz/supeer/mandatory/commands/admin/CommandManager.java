package xyz.supeer.mandatory.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.supeer.mandatory.Main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CommandManager implements CommandExecutor {



    private ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
    private Main plugin = Main.getInstance();

    public CommandManager() {
    }

    // Subcommands
    public String main = "admin";
    public String help = "help";

    public void setup() {
        plugin.getCommand(main).setExecutor(this);

        this.commands.add(new HelpCommand());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!sender.hasPermission("mandatory.command.admin")) {
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
                p.sendMessage("§2--- §aHjälp: /admin §2---");
                p.sendMessage("§2/admin help");
            }

            SubCommand target = this.get(args[0]);

            if (this.get(args[0]) == null) {
                p.sendMessage("§7Okänt kommando. Skriv §n/admin help§r§7 för en lista på kommandon.");
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
