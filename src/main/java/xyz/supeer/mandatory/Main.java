package xyz.supeer.mandatory;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.supeer.mandatory.commands.*;
import org.bukkit.event.Listener;
import xyz.supeer.mandatory.commands.admin.CommandManagerAdmin;

import xyz.supeer.mandatory.commands.player.CommandManagerPlayer;
import xyz.supeer.mandatory.listeners.*;
import xyz.supeer.mandatory.sql.MySQL;
import xyz.supeer.mandatory.sql.SQLGetter;
import xyz.supeer.mandatory.tabcompletion.GoCommandTabCompleter;
import xyz.supeer.mandatory.utils.TeleportUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class Main extends JavaPlugin implements Listener{
    public HashMap<Player, List<String>> afkPlayers = new HashMap<Player, List<String>>();
    public HashMap<Player, List<String>> kickedPlayers = new HashMap<Player, List<String>>();
    private HashMap<Player, Player> tpa = new HashMap<Player, Player>();
    public static Main plugin;
    private static Main instance;
    public CommandManagerAdmin commandManagerAdmin;
    public CommandManagerPlayer commandManagerPlayer;
    private File customConfigFile;
    private FileConfiguration customConfig;
    private Main main;


    @Override
    public void onEnable() {

        createCustomConfig();
        setInstance(this);
        commandManagerAdmin = new CommandManagerAdmin();
        commandManagerAdmin.setup();
        commandManagerPlayer = new CommandManagerPlayer();
        commandManagerAdmin.setup();
        plugin = this;
        MySQL.connect();
        SQLGetter.createGoTable();
        SQLGetter.createLoveTable();
        SQLGetter.createPlayerTable();
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
        new LoveCommand(this);
        new KickCommand(this);
        new ModchatCommand(this);
        new HelpopCommand(this);
        new MessageCommand(this);
        new ReplyCommand(this);
        new AFKCommand(this);
        new BuyCommand(this);
        new GoCommand(this);
        new SpawnCommand(this);
        new TeleportAcceptCommand(this);
        new TeleportAskCommand(this);
        new TeleportDenyCommand(this);
        this.getServer().getPluginManager().registerEvents(new JoinLeaveListener(plugin), this);
        this.getServer().getPluginManager().registerEvents(new FireworkListener(), this);
        this.getServer().getPluginManager().registerEvents(new MessageListener(), this);
        this.getServer().getPluginManager().registerEvents(new TeleportUtil(instance), this);
        this.getServer().getPluginManager().registerEvents(new SpawnListeners(this), this);
        this.getServer().getPluginManager().registerEvents(new KickCommand(this), this);
        this.getServer().getPluginManager().registerEvents(new CopyrightListener(), this);
        getCommand("go").setTabCompleter(new GoCommandTabCompleter());
    }


    @Override
    public void onDisable() {

        MySQL.disconnect();

        getConfig().options().copyDefaults(false);
        saveConfig();

    }


    public FileConfiguration getCustomConfig() {
        return customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "internals.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("internals.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    public void saveCustomConfig() throws IOException {
        if (!customConfigFile.exists()) {
            File file = new File(plugin.getDataFolder(), "internals.yml");
            customConfig.save(file);
        } else {
            this.getCustomConfig().save(this.customConfigFile);
        }
    }

    public static Main getInstance() {return instance; }
    public static void setInstance(Main instance) { Main.instance = instance;}


    public HashMap<Player, Player> getTpa() {
        return tpa;
    }


        }



