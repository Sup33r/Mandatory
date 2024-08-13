package live.supeer.mandatory.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Utilities {

    public static List<UUID> stringToUUIDList(String playerString) {
        List<UUID> players = new ArrayList<>();
        if (playerString == null || playerString.isEmpty()) {
            return players;
        }
        String[] playerArray = playerString.split(",");
        for (String playerUUID : playerArray) {
            players.add(UUID.fromString(playerUUID));
        }
        return players;
    }


    public static String uuidListToString(List<UUID> players) {
        StringBuilder playerString = new StringBuilder();
        for (UUID player : players) {
            playerString.append(player.toString()).append(",");
        }
        return playerString.toString();
    }
}
