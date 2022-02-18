package xyz.supeer.mandatory.tabcompletion;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class GoCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> arguments = new ArrayList<>();
            arguments.add("_namn_");
            arguments.add("_sidnummer_");
            arguments.add("near");

            return arguments;
        }

        if (args.length == 2) {
            List<String> arguments = new ArrayList<>();
            arguments.add("delete");
            arguments.add("give");
            arguments.add("info");
            arguments.add("set");
            arguments.add("toggle");
            arguments.add("whitelist");
            return arguments;
        }

        return null;
    }
}
