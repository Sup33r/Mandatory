package xyz.supeer.mandatory;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.supeer.mandatory.commands.*;
import org.bukkit.event.Listener;
import xyz.supeer.mandatory.commands.admin.CommandManager;
import xyz.supeer.mandatory.listeners.*;
import xyz.supeer.mandatory.sql.MySQL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main extends JavaPlugin implements Listener{
    public List<Player> afkPlayers = new ArrayList<Player>();
    public static Main plugin;
    private static Main instance;
    public CommandManager commandManager;

    @Override
    public void onEnable() {

        setInstance(this);
        commandManager = new CommandManager();
        commandManager.setup();

        MySQL.connect();

        plugin = this;
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
        new KickCommand(this);
        new ModchatCommand(this);
        new HelpopCommand(this);
        new MessageCommand(this);
        new ReplyCommand(this);
        new AFKCommand(this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new FireworkListener(), this);
        this.getServer().getPluginManager().registerEvents(new MessageListener(), this);
    }

    @Override
    public void onDisable() {

        MySQL.disconnect();

    }

    public static Main getInstance() {return instance; }
    public static void setInstance(Main instance) { Main.instance = instance;}

        }



