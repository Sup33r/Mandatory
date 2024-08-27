package live.supeer.mandatory;

import co.aikar.commands.PaperCommandManager;
import co.aikar.idb.DB;
import com.google.common.collect.ImmutableList;
import de.tr7zw.changeme.nbtapi.NBT;
import live.supeer.mandatory.commands.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public final class Mandatory extends JavaPlugin {

    public Logger logger = null;
    public static MandatoryConfiguration configuration;
    @Getter
    private static Mandatory plugin;
    private static LanguageManager languageManager;

    public static HashMap<Player, Location> backLocations = new HashMap<>();
    public static HashMap<UUID, List<Component>> afkPlayers = new HashMap<>();
    public static List<Player> loveCooldown = new ArrayList<>();
    public static HashMap<Player, Player> replyPlayers = new HashMap<>();
    public static HashMap<Player, Player> teleportRequests = new HashMap<>();
    public static HashMap<Player, Go> promptedPayment = new HashMap<>();
    public static List<Player> firstTimePlayer = new ArrayList<>();
    public static List<Player> kickedPlayers = new ArrayList<>();
    public static List<Player> bannedPlayers = new ArrayList<>();
    public static List<Player> mapOverride = new ArrayList<>();
    public static List<Player> chatOverride = new ArrayList<>();
    public static HashMap<UUID, Long> startTime = new HashMap<>();
    public static HashMap<UUID, Channel> activeChannel = new HashMap<>();
    public static HashMap<UUID, List<Channel>> playerChannels = new HashMap<>();

    public static Mandatory getInstance() {
        return plugin;
    }
    @Override
    public void onEnable() {
        plugin = this;
        this.logger = getLogger();
        configuration = new MandatoryConfiguration(this);

        languageManager = new LanguageManager(this, "sv_se");

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("brigadier");

        manager.registerCommand(new CommandAdmin());
        manager.registerCommand(new CommandAFK());
        manager.registerCommand(new CommandBack());
        manager.registerCommand(new CommandBalance());
        manager.registerCommand(new CommandBan());
        manager.registerCommand(new CommandBroadcast());
        manager.registerCommand(new CommandBuy());
        manager.registerCommand(new CommandChat());
        manager.registerCommand(new CommandChest());
        manager.registerCommand(new CommandCityChat());
        manager.registerCommand(new CommandFeed());
        manager.registerCommand(new CommandFly());
        manager.registerCommand(new CommandGamemode());
        manager.registerCommand(new CommandGo());
        manager.registerCommand(new CommandHelpop());
        manager.registerCommand(new CommandHome());
        manager.registerCommand(new CommandIgnore());
        manager.registerCommand(new CommandKick());
        manager.registerCommand(new CommandLove());
        manager.registerCommand(new CommandMe());
        manager.registerCommand(new CommandMessage());
        manager.registerCommand(new CommandModchat());
        manager.registerCommand(new CommandNear());
        manager.registerCommand(new CommandOnline());
        manager.registerCommand(new CommandPay());
        manager.registerCommand(new CommandPayall());
        manager.registerCommand(new CommandPlayer());
        manager.registerCommand(new CommandRepair());
        manager.registerCommand(new CommandReply());
        manager.registerCommand(new CommandShop());
        manager.registerCommand(new CommandSpawn());
        manager.registerCommand(new CommandSpeed());
        manager.registerCommand(new CommandTeleportAccept());
        manager.registerCommand(new CommandTeleportAsk());
        manager.registerCommand(new CommandTeleportDeny());
        manager.registerCommand(new CommandUpdate());
        manager.registerCommand(new CommandVision());

        registerCompletions(manager);

        this.getServer().getPluginManager().registerEvents(new MandatoryListerner(), this);
        if (!NBT.preloadApi()) {
            getLogger().warning("NBT-API wasn't initialized properly, disabling the plugin");
            this.getServer().getPluginManager().disablePlugin(this);
        }
        Database.initialize();
        ChannelManager.loadChannels();

        if (getServer().getPluginManager().getPlugin("Metropolis") != null) {
            getLogger().info("Metropolis found!");
        } else {
            getLogger().warning("Metropolis not found. disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }

        if (getServer().getPluginManager().getPlugin("Apied") != null) {
            getLogger().info("Apied found!");
        } else {
            getLogger().warning("Apied not found. disabling plugin");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        DB.close();
    }
    public static void sendMessage(
            @NotNull CommandSender sender, @NotNull String key, String... replacements) {
        String message = languageManager.getValue(key, getLocale(sender), replacements);
        if (message == null || message.isEmpty()) {
            sender.sendMessage("§cMessage not found: " + key);
        }
        if (message != null && !message.isEmpty()) {
            Component component = languageManager.getMiniMessage().deserialize(message);
            sender.sendMessage(component);
        }
    }

    public static String getMessage(@NotNull String key, String... replacements) {
        String message = languageManager.getValue(key, "sv_se", replacements);

        if (message != null && !message.isEmpty()) {
            // Deserialize MiniMessage to a Component
            Component component = languageManager.getMiniMessage().deserialize(message);
            // Convert the Component to a legacy formatted string
            return LegacyComponentSerializer.legacySection().serialize(component);
        }
        return null;
    }

    public static String getRawMessage(@NotNull String key, String... replacements) {
        String message = languageManager.getValue(key, "sv_se", replacements);
        if (message != null && !message.isEmpty()) {
            return message;
        }
        return null;
    }

    public static Component getMessageComponent(@NotNull String key, String... replacements) {
        String message = languageManager.getValue(key, "sv_se", replacements);

        if (message != null && !message.isEmpty()) {
            return languageManager.getMiniMessage().deserialize(message);
        }
        return null;
    }

    public static Component stringToMiniMessage(String message) {
        return languageManager.getMiniMessage().deserialize(message);
    }

    private static @NotNull String getLocale(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).locale().toString();
        } else {
            return getInstance().getConfig().getString("settings.locale", "sv_se");
        }
    }

    public static void registerCompletions(PaperCommandManager manager) {
        manager.getCommandCompletions().registerAsyncCompletion("gamemode", c -> ImmutableList.of("SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR", "0", "1", "2", "3"));
        manager.getCommandCompletions().registerAsyncCompletion("goName", c -> {
            Player player = c.getPlayer();
            return new ArrayList<>(GoManager.getGoList(player).stream().map(Go::getName).toList());
        });
        manager.getCommandCompletions().registerAsyncCompletion("go1", c -> ImmutableList.of("set", "give", "toggle", "whitelist"));
        manager.getCommandCompletions().registerAsyncCompletion("shopTypes", c -> ImmutableList.of("maxuses", "maxdailyuses", "maxdailyplayeruses", "maxplayeruses"));
        manager.getCommandCompletions().registerAsyncCompletion("adminToggleable", c -> ImmutableList.of("chatcolor", "extprefix"));
        manager.getCommandCompletions().registerAsyncCompletion("adminSettable", c -> ImmutableList.of("afk", "spawn", "firstspawn"));
        manager.getCommandCompletions().registerCompletion("playerChannels", c -> ChannelManager.getPlayerChannels(c.getPlayer()));
        manager.getCommandCompletions().registerCompletion("channels", c -> ChannelManager.getChannelNames());
        manager.getCommandCompletions().registerCompletion("channelTypes", c -> ImmutableList.of("CITY", "GLOBAL", "MOD", "OTHER"));
    }
}
