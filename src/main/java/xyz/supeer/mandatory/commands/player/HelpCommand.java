package xyz.supeer.mandatory.commands.player;

import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class HelpCommand extends SubCommand {

    private final Main plugin = Main.getInstance();

    @Override
    public void onCommand(Player p, String[] args) {
        p.sendMessage("§2--- §aHjälp: /player §2---");
        p.sendMessage("§2/player help");
    }

    @Override
    public String name() { return plugin.commandManagerAdmin.help; }

    @Override
    public String info() {
        return "";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

}
