package live.supeer.mandatory.utils;

import live.supeer.apied.Apied;
import live.supeer.apied.Home;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.text.NumberFormat;
import java.util.*;

public class Utilities {

    public static String formattedMoney(Integer money) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setGroupingUsed(true);
        return formatter.format(money).replace(",", " ");
    }

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
            return items.getFirst();
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

    public static String formatHomeList(List<Home> homes) {
        if (homes == null || homes.isEmpty()) {
            return "";
        }

        int size = homes.size();

        if (size == 1) {
            return String.format("<click:run_command:/home %s>%s</click>", homes.get(0).getName(), homes.get(0).getName());
        }

        if (size == 2) {
            return String.format("<click:run_command:/home %s>%s</click> & <click:run_command:/home %s>%s</click>",
                    homes.get(0).getName(), homes.get(0).getName(), homes.get(1).getName(), homes.get(1).getName());
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            result.append(String.format("<click:run_command:/home %s>%s</click>", homes.get(i).getName(), homes.get(i).getName()));
            if (i < size - 2) {
                result.append(", ");
            } else {
                result.append(" & ");
            }
        }
        result.append(String.format("<click:run_command:/home %s>%s</click>", homes.get(size - 1).getName(), homes.get(size - 1).getName()));

        return result.toString();
    }
    public static boolean isBlockContainer(Material material) {
        return material == Material.CHEST
                || material == Material.TRAPPED_CHEST
                || material == Material.BARREL
                || material == Material.SHULKER_BOX
                || material == Material.BLACK_SHULKER_BOX
                || material == Material.BLUE_SHULKER_BOX
                || material == Material.BROWN_SHULKER_BOX
                || material == Material.CYAN_SHULKER_BOX
                || material == Material.GRAY_SHULKER_BOX
                || material == Material.GREEN_SHULKER_BOX
                || material == Material.LIGHT_BLUE_SHULKER_BOX
                || material == Material.LIGHT_GRAY_SHULKER_BOX
                || material == Material.LIME_SHULKER_BOX
                || material == Material.MAGENTA_SHULKER_BOX
                || material == Material.ORANGE_SHULKER_BOX
                || material == Material.PINK_SHULKER_BOX
                || material == Material.PURPLE_SHULKER_BOX
                || material == Material.RED_SHULKER_BOX
                || material == Material.WHITE_SHULKER_BOX
                || material == Material.YELLOW_SHULKER_BOX;
    }

    public static String getHoverInfo(ItemStack item) {
        String itemId = item.getType().getKey().toString().toLowerCase();
        if (Apied.configuration.getBannedPreviewItems().contains(itemId)) {
            return "<hover:show_item:'" + itemId + "':" + 1 + ">";
        }
        String hoverString = Apied.componentToMiniMessage(Component.text().hoverEvent(item).asComponent());
        return hoverString.replaceAll(":(\\d+):", ":1:");
    }
}
