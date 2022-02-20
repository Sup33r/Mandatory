package xyz.supeer.mandatory.commands.admin;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.io.IOException;

public class SetCommands extends SubCommand {
    private Main plugin = Main.getInstance();

    @Override
    public void onCommand(Player p, String[] args) throws IOException {

        if (args.length == 1) {
            p.sendMessage("§7Syntax /admin set spawn");
            p.sendMessage("§7Syntax /admin set firstspawn");
            return;
        }

        if (args[1].equalsIgnoreCase("spawn")) {

            if (p.hasPermission("mandatory.command.admin.set.spawn")) {
                Location location = p.getLocation();
                plugin.getCustomConfig().set("spawn", location);
                plugin.saveCustomConfig();
                p.sendMessage("§aStartpunkten har nu sparats!");
                return;
            }

        }

        if (args[1].equalsIgnoreCase("firstspawn")) {
            if (p.hasPermission("mandatory.command.admin.set.firstspawn")) {
                Location location = p.getLocation();
                plugin.getCustomConfig().set("firstspawn", location);
                plugin.saveCustomConfig();
                p.sendMessage("§aFörsta startpunkten har nu sparats!");
            }
        }

    }

    @Override
    public String name() {
        return plugin.commandManager.set;
    }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
