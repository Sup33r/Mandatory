package live.supeer.mandatory;

import co.aikar.commands.PaperCommandManager;
import co.aikar.idb.DB;
import com.google.common.collect.ImmutableList;
import live.supeer.mandatory.commands.*;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public final class Mandatory extends JavaPlugin {

    public Logger logger = null;
    public static MandatoryConfiguration configuration;
    @Getter
    private static Mandatory plugin;
    private LanguageManager languageManager;

    public HashMap<Player, Location> backLocations = new HashMap<>();
    public HashMap<Player, List<Component>> afkPlayers = new HashMap<>();
    public HashMap<Player, Player> replyPlayers = new HashMap<>();
    public HashMap<Player, Player> teleportRequests = new HashMap<>();


    @Override
    public void onEnable() {
        plugin = this;
        CommandSpawn.plugin = this;
        CommandAFK.plugin = this;
        CommandBack.plugin = this;
        CommandBroadcast.plugin = this;
        CommandFeed.plugin = this;
        CommandFly.plugin = this;
        CommandGamemode.plugin = this;
        CommandRepair.plugin = this;
        CommandSpeed.plugin = this;
        CommandVision.plugin = this;
        this.logger = getLogger();
        configuration = new MandatoryConfiguration(this);

        this.languageManager = new LanguageManager(this, "sv_se");

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("brigadier");

        manager.registerCommand(new CommandSpawn());
        manager.registerCommand(new CommandAFK());
        manager.registerCommand(new CommandBack());
        manager.registerCommand(new CommandBroadcast());
        manager.registerCommand(new CommandFeed());
        manager.registerCommand(new CommandFly());
        manager.registerCommand(new CommandGamemode());
        manager.registerCommand(new CommandRepair());
        manager.registerCommand(new CommandSpeed());
        manager.registerCommand(new CommandVision());

        registerCompletions(manager);

//        Database.initialize();
    }

    @Override
    public void onDisable() {
//        DB.close();
    }

    public void sendMessage(
            @NotNull CommandSender sender, @NotNull String key, String... replacements) {
        String message = this.languageManager.getValue(key, getLocale(sender), replacements);

        if (message != null && !message.isEmpty()) {
            Component component = languageManager.getMiniMessage().deserialize(message);
            sender.sendMessage(component);
        }
    }

    public String getMessage(@NotNull String key, String... replacements) {
        String message = this.languageManager.getValue(key, "sv_se", replacements);

        if (message != null && !message.isEmpty()) {
            // Deserialize MiniMessage to a Component
            Component component = languageManager.getMiniMessage().deserialize(message);
            // Convert the Component to a legacy formatted string
            return LegacyComponentSerializer.legacySection().serialize(component);
        }
        return null;
    }

    public Component getMessageComponent(@NotNull String key, String... replacements) {
        String message = this.languageManager.getValue(key, "sv_se", replacements);

        if (message != null && !message.isEmpty()) {
            return languageManager.getMiniMessage().deserialize(message);
        }
        return null;
    }

    private @NotNull String getLocale(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return ((Player) sender).getLocale();
        } else {
            return this.getConfig().getString("settings.locale", "sv_se");
        }
    }

    public static void registerCompletions(PaperCommandManager manager) {
        manager.getCommandCompletions().registerCompletion("gamemode", c -> ImmutableList.of("SURVIVAL", "CREATIVE", "ADVENTURE", "SPECTATOR", "0", "1", "2", "3"));
    }
}
