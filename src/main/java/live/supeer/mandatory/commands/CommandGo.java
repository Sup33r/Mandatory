package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.*;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.TeleportUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("go")
public class CommandGo extends BaseCommand {
    public static Mandatory plugin;

    //     - required.command.go
    //     - required.command.go.delete.own
    //     - required.command.go.give.own
    //     - required.command.go.list
    //     - required.command.go.set.displayname.own
    //     - required.command.go.set.price.own
    //     - required.command.go.toggle.open.own
    //     - required.command.go.use
    //     - required.command.buy.go

    //set (displayname, location, price), info

    @Default
    public void onGo(Player player, String[] args) {
        if (!player.hasPermission("mandatory.command.go")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (args.length == 0) {
            showList(player, 1, GoManager.getGoList(player));
        }
        if (args.length == 1) {
            if (args[0].matches("[0-9]+")) {
                int page = Integer.parseInt(args[0]);
                showList(player, page, GoManager.getGoList(player));
                return;
            }
            if (args[0].endsWith("!")) {
                String name = args[0].substring(0, args[0].length() - 1);
                if (GoManager.goExists(name)) {
                    Go go = GoManager.getGo(name);
                    if (go.getLocation() == null) {
                        plugin.sendMessage(player, "messages.go.locationNotSet");
                        return;
                    }
                    TeleportUtil.teleportGo(player, go, false, true);
                    return;
                }
                plugin.sendMessage(player, "messages.go.notFound");
                return;
            }
            if (GoManager.goExists(args[0])) {
                Go go = GoManager.getGo(args[0]);
                if (go.getLocation() == null) {
                    plugin.sendMessage(player, "messages.go.locationNotSet");
                    return;
                }
                TeleportUtil.teleportGo(player, go, false, false);
                return;
            }
            plugin.sendMessage(player, "messages.go.notFound");
            return;
        }
        if (args.length == 2) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                plugin.sendMessage(player, "messages.go.notFound");
                return;
            }

            switch (args[1]) {
                case "delete" -> {
                    if (!player.hasPermission("mandatory.command.go.delete") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.delete.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    GoManager.deleteGo(go);
                    plugin.sendMessage(player, "messages.go.deleted", "%name%", go.getName());
                    return;
                }
                case "whitelist" -> {
                    if (!player.hasPermission("mandatory.command.go.whitelist") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.whitelist.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    List<String> whitelistedPlayers = MPlayerManager.getPlayerNamesFromUUIDs(go.getWhitelistPlayers());
                    plugin.sendMessage(player, "messages.go.whitelist.list", "%name%", go.getName(), "%count%", String.valueOf(whitelistedPlayers.size()), "%players%", Utilities.formatStringList(whitelistedPlayers));
                    return;
                }
                case "info" -> {
                    if (!player.hasPermission("mandatory.command.go.info")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    plugin.sendMessage(player, "messages.go.info.header", "%name%", go.getName());
                    if (go.getDisplayname() != null && !go.getDisplayname().isEmpty()) {
                        plugin.sendMessage(player, "messages.go.info.displayname", "%displayname%", go.getDisplayname());
                    }
                    plugin.sendMessage(player, "messages.go.info.owner", "%owner%", MPlayerManager.getPlayerFromUUID(go.getOwnerUUID()).getName());
                    plugin.sendMessage(player, "messages.go.info.open.open", "%open%", go.isOpen() ? plugin.getMessage("messages.go.info.open.true") : plugin.getMessage("messages.go.info.open.false"));
                    plugin.sendMessage(player, "messages.go.info.created", "%created%", DateUtil.formatDate(go.getCreateDate()));
                    plugin.sendMessage(player, "messages.go.info.latestUse", "%latestUse%", DateUtil.formatDate(GoManager.getLatestUse(go)));
                    String price = go.getChargeAmount() == 0 ? plugin.getMessage("messages.free") : go.getChargeAmount() + " encoins";
                    plugin.sendMessage(player, "messages.go.info.price", "%price%", price);
                    return;
                }
            }
            plugin.sendMessage(player, "messages.go.syntax");
            return;
        }
        if (args.length == 3) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                plugin.sendMessage(player, "messages.go.notFound");
                return;
            }

            switch (args[1]) {
                case "toggle" -> {
                    if (args[2].equalsIgnoreCase("open")) {
                        if (!player.hasPermission("mandatory.command.go.toggle.open") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.toggle.open.others")) {
                            plugin.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        go.setOpen(!go.isOpen());
                        plugin.sendMessage(player, go.isOpen() ? "messages.go.opened" : "messages.go.closed", "%name%", go.getName());
                        return;
                    }
                    if (args[2].equalsIgnoreCase("whitelistcharge")) {
                        if (!player.hasPermission("mandatory.command.go.toggle.whitelistcharge") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.toggle.whitelistcharge.others")) {
                            plugin.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        go.setWhitelistcharge(!go.isWhitelistcharge());
                        plugin.sendMessage(player, go.isWhitelistcharge() ? "messages.go.whitelistcharge.enabled" : "messages.go.whitelistcharge.disabled", "%name%", go.getName());
                        return;
                    }
                    plugin.sendMessage(player, "messages.go.syntax");
                    return;
                }
                case "whitelist" -> {
                    if (!player.hasPermission("mandatory.command.go.whitelist") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.whitelist.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[2].substring(1));
                    if (mPlayer == null) {
                        plugin.sendMessage(player, "messages.error.playerNotFound");
                        return;
                    }
                    if (args[2].startsWith("+")) {
                        if (go.isWhitelisted(mPlayer.getUuid())) {
                            plugin.sendMessage(player, "messages.go.whitelist.alreadyAdded", "%name%", go.getName(), "%player%", mPlayer.getName());
                            return;
                        }
                        go.addToWhitelist(mPlayer.getUuid());
                        plugin.sendMessage(player, "messages.go.whitelist.added", "%name%", go.getName(), "%player%", mPlayer.getName());
                        return;
                    }
                    if (args[2].startsWith("-")) {
                        if (!go.isWhitelisted(mPlayer.getUuid())) {
                            plugin.sendMessage(player, "messages.go.whitelist.alreadyRemoved", "%name%", go.getName(), "%player%", mPlayer.getName());
                            return;
                        }
                        go.removeFromWhitelist(mPlayer.getUuid());
                        plugin.sendMessage(player, "messages.go.whitelist.removed", "%name%", go.getName(), "%player%", mPlayer.getName());
                        return;
                    }
                    plugin.sendMessage(player, "messages.go.syntax");
                    return;
                }
                case "give" -> {
                    if (!player.hasPermission("mandatory.command.go.give") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.give.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[2].substring(1));
                    if (mPlayer == null) {
                        plugin.sendMessage(player, "messages.error.playerNotFound");
                        return;
                    }
                    if (mPlayer.getUuid().equals(player.getUniqueId())) {
                        plugin.sendMessage(player, "messages.go.give.self");
                        return;
                    }
                    go.setOwnerUUID(mPlayer.getUuid());
                    plugin.sendMessage(player, "messages.go.give.given", "%name%", go.getName(), "%player%", mPlayer.getName());
                    return;
                }
                case "set" -> {
                    if (args[2].equalsIgnoreCase("location")) {
                        if (!player.hasPermission("mandatory.command.go.set.location") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.location.others")) {
                            plugin.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        if (!(go.getLocation() == null) && !player.hasPermission("mandatory.command.go.set.location.force")) {
                            plugin.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        Location location = LocationUtil.stringToLocation(args[3]);
                        go.setLocation(location);
                        plugin.sendMessage(player, "messages.go.set.location", "%name%", go.getName());
                        return;
                    }
                }
            }
            plugin.sendMessage(player, "messages.go.syntax");
            return;
        }
        if (args.length == 4) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                plugin.sendMessage(player, "messages.go.notFound");
                return;
            }
            if (!args[1].equalsIgnoreCase("set")) {
                plugin.sendMessage(player, "messages.go.syntax");
                return;
            }
            switch (args[2]) {
                case "displayname" -> {
                    if (!player.hasPermission("mandatory.command.go.set.displayname") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.displayname.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    if (args[3].equals("-")) {
                        go.setDisplayName(null);
                        plugin.sendMessage(player, "messages.go.set.displayname.removed", "%name%", go.getName());
                        return;
                    }
                    if (args[3].length() > Mandatory.configuration.getGoDisplayNameLimit()) {
                        plugin.sendMessage(player, "messages.go.set.displayname.tooLong", "%max%", String.valueOf(Mandatory.configuration.getGoDisplayNameLimit()));
                        return;
                    }
                    go.setDisplayName(args[3]);
                    plugin.sendMessage(player, "messages.go.set.displayname.success", "%name%", go.getName());
                }
                case "price" -> {
                    if (!player.hasPermission("mandatory.command.go.set.price") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.price.others")) {
                        plugin.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    if (args[3].equals("-")) {
                        go.setChargeAmount(0);
                        plugin.sendMessage(player, "messages.go.set.chargeamount.removed", "%name%", go.getName());
                        return;
                    }
                    if (!args[3].matches("[0-9]+")) {
                        plugin.sendMessage(player, "messages.go.syntax");
                        return;
                    }
                    int chargeAmount = Integer.parseInt(args[3]);
                    if (chargeAmount > Mandatory.configuration.getGoChargeAmount()) {
                        plugin.sendMessage(player, "messages.go.set.chargeamount.tooMuch", "%max%", String.valueOf(Mandatory.configuration.getGoChargeAmount()));
                        return;
                    }
                    go.setChargeAmount(chargeAmount);
                    plugin.sendMessage(player, "messages.go.set.chargeamount.set", "%name%", go.getName());
                }
            }
            plugin.sendMessage(player, "messages.go.syntax");
            return;
        }
    }

    public void showList(Player player, int pageStart, List<Go> list) {
        StringBuilder tmpMessage = new StringBuilder();
        int itemsPerPage = 25;
        int start = (pageStart * itemsPerPage) - itemsPerPage;
        int stop = pageStart * itemsPerPage;
        if (list.isEmpty()) {
            plugin.sendMessage(player, "messages.go.list.empty");
            return;
        }
        if(start >= list.size()) {
            plugin.sendMessage(player, "messages.go.list.notEnoughPages");
            return;
        }

        for (int i = start; i < stop; i++) {
            tmpMessage.append(list.get(i).getName()).append(", ");
        }
        plugin.sendMessage(player, "messages.go.list.list", "%start%", String.valueOf(start), "%end%", String.valueOf(((int) Math.ceil(((double) list.size()) / ((double) itemsPerPage)))));
        player.sendMessage("§2" + tmpMessage.substring(0, tmpMessage.length() - 2));
    }

}
