package xyz.supeer.mandatory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.supeer.mandatory.commands.*;
import org.bukkit.event.Listener;
import xyz.supeer.mandatory.listeners.*;

public class Main extends JavaPlugin implements Listener{

    public static Main plugin;

    @Override
    public void onEnable() {
        plugin = this;
        new HejCommand(this);
        new HejdaCommand(this);
        new FlyCommand(this);
        new BroadcastCommand(this);
        new FeedCommand(this);
        new MeCommand(this);
        new VisionCommand(this);
        new GamemodeCommand(this);
        new PlayerDeath(this);
        new SpeedCommand(this);
        new RepairCommand(this);
        new BalanceCommand(this);
        new PlayerCommand(this);
        new LoveCommand(this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);

    }

    @EventHandler
    public void OnJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendTitle("Tjuhu, " + player.getDisplayName() + "!", "Välkommen till Kottcraft.", 0, 70,20);
    }



    @Override
    public void onDisable() {

    }



}
