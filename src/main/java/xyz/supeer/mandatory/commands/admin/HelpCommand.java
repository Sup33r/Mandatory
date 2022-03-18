package xyz.supeer.mandatory.commands.admin;

import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

public class HelpCommand extends SubCommand{

    private final Main plugin = Main.getInstance();

    @Override
    public void onCommand(Player p, String[] args) {
        p.sendMessage("§2--- §aHjälp: /admin §2---");
        p.sendMessage("§2/admin help");
        p.sendMessage("§2/admin set §a§nfirstspawn");
        p.sendMessage("§2/admin set §a§nspawn");
        p.sendMessage("§2/admin set §a§nafk");
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
