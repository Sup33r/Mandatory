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

    private final int teleportCooldown;

    private final Location spawnLocation;
    private final Location afkLocation;

    private final List<String> allowedRepairItems;

    private final int goNameLimit;
    private final int goDisplayNameLimit;
    private final int goChargeAmount;
    private final int loveCooldown;

    MandatoryConfiguration(Mandatory plugin) {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        sqlHost = plugin.getConfig().getString("sql.host");
        sqlPort = plugin.getConfig().getInt("sql.port");
        sqlDatabase = plugin.getConfig().getString("sql.database");
        sqlUsername = plugin.getConfig().getString("sql.username");
        sqlPassword = plugin.getConfig().getString("sql.password");

        teleportCooldown = plugin.getConfig().getInt("settings.teleportCooldown");

        spawnLocation = plugin.getConfig().getLocation("settings.locations.spawnLocation");
        afkLocation = plugin.getConfig().getLocation("settings.locations.spawnLocation");

        allowedRepairItems = plugin.getConfig().getStringList("allowed_repair");

        goNameLimit = plugin.getConfig().getInt("settings.limits.goname");
        goDisplayNameLimit = plugin.getConfig().getInt("settings.limits.godisplayname");
        goChargeAmount = plugin.getConfig().getInt("settings.limits.gochargeamount");

        loveCooldown = plugin.getConfig().getInt("settings.limits.lovecooldown");

    }
}
