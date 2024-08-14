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

    public static String formatStringList(List<String> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }

        int size = items.size();

        if (size == 1) {
            return items.get(0);
        }

        if (size == 2) {
            return String.format("%s & %s", items.get(0), items.get(1));
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            result.append(items.get(i));
            if (i < size - 2) {
                result.append(", ");
            } else {
                result.append(" & ");
            }
        }
        result.append(items.get(size - 1));

        return result.toString();
    }
}
