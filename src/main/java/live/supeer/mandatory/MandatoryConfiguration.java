package live.supeer.mandatory;

import lombok.Getter;
import org.bukkit.Location;

import java.util.List;

@Getter
public class MandatoryConfiguration {

    private final String sqlHost;
    private final int sqlPort;
    private final String sqlDatabase;
    private final String sqlUsername;
    private final String sqlPassword;

    private final int loveCooldown;

    private Location spawnLocation;
    private Location afkLocation;
    private Location firstSpawnLocation;

    private final List<String> allowedRepairItems;

    private final int goNameLimit;
    private final int goDisplayNameLimit;
    private final int maxHomesExtra;
    private final int maxHomes;

    private final int goChargeAmount;
    private final int broadcastPrice;
    private final int goPrice;
    private final int headPrice;
    private final int copyrightPrice;

    private final List<String> disabledNames;

    private final List<String> allowedCommands;

    private final List<String> bannedWords;

    MandatoryConfiguration(Mandatory plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        sqlHost = plugin.getConfig().getString("sql.host");
        sqlPort = plugin.getConfig().getInt("sql.port");
        sqlDatabase = plugin.getConfig().getString("sql.database");
        sqlUsername = plugin.getConfig().getString("sql.username");
        sqlPassword = plugin.getConfig().getString("sql.password");

        loveCooldown = plugin.getConfig().getInt("settings.limits.lovecooldown");

        spawnLocation = plugin.getConfig().getLocation("settings.locations.spawnlocation");
        afkLocation = plugin.getConfig().getLocation("settings.locations.afklocation");
        firstSpawnLocation = plugin.getConfig().getLocation("settings.locations.firstspawnlocation");

        allowedRepairItems = plugin.getConfig().getStringList("allowed_repair");
        allowedCommands = plugin.getConfig().getStringList("allowed_commands");

        goNameLimit = plugin.getConfig().getInt("settings.limits.goname");
        goDisplayNameLimit = plugin.getConfig().getInt("settings.limits.godisplayname");
        maxHomesExtra = plugin.getConfig().getInt("settings.limits.maxhomesextra");
        maxHomes = plugin.getConfig().getInt("settings.limits.maxhomes");

        goChargeAmount = plugin.getConfig().getInt("settings.limits.gochargeamount");
        broadcastPrice = plugin.getConfig().getInt("settings.price.broadcast");
        goPrice = plugin.getConfig().getInt("settings.price.go");
        headPrice = plugin.getConfig().getInt("settings.price.head");
        copyrightPrice = plugin.getConfig().getInt("settings.price.copyright");

        disabledNames = plugin.getConfig().getStringList("settings.disablednames");
        bannedWords = plugin.getConfig().getStringList("settings.bannedwords");
    }

    public void setFirstSpawnLocation(Location location) {
        this.firstSpawnLocation = location;
        Mandatory.getInstance().getConfig().set("settings.locations.firstspawnlocation", location);
        save();
    }

    public void setAfkLocation(Location location) {
        this.afkLocation = location;
        Mandatory.getInstance().getConfig().set("settings.locations.afklocation", location);
        save();
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        Mandatory.getInstance().getConfig().set("settings.locations.spawnlocation", location);
        save();
    }

    public void addDisabledName(String name) {
        disabledNames.add(name);
        Mandatory.getInstance().getConfig().set("settings.disablednames", disabledNames);
        save();
    }

    public void addBannedWord(String word) {
        bannedWords.add(word);
        Mandatory.getInstance().getConfig().set("settings.bannedwords", bannedWords);
        save();
    }

    public void save() {
        Mandatory.getInstance().saveConfig();
    }

}
