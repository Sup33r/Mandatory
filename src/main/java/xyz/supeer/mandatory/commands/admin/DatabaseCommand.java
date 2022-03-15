package xyz.supeer.mandatory.commands.admin;

import org.bukkit.entity.Player;
import xyz.supeer.mandatory.Main;

import java.io.IOException;

public class DatabaseCommand extends SubCommand{
    private final Main plugin = Main.getInstance();
    @Override
    public void onCommand(Player p, String[] args) throws IOException {

    }

    @Override
    public String name() {
        return plugin.commandManager.database;
    }

    @Override
    public String info() {
        return null;
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }
}
