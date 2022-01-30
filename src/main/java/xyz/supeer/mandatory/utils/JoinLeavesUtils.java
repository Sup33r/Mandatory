package xyz.supeer.mandatory.utils;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.supeer.mandatory.listeners.JoinLeaveListener;

public final class JoinLeavesUtils extends JavaPlugin {

    @Override
    public void onEnable() {

    getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

    }

}
