package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import live.supeer.apied.MPlayer;
import live.supeer.apied.MPlayerManager;
import live.supeer.mandatory.*;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.mandatory.utils.TeleportUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@CommandAlias("go")
public class CommandGo extends BaseCommand {

    @Default
    @CommandCompletion("@goNames @go1")
    public void onGo(Player player, String[] args) {
        if (!player.hasPermission("mandatory.command.go")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
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
                    assert go != null;
                    if (go.getLocation() == null) {
                        Mandatory.sendMessage(player, "messages.go.locationNotSet");
                        return;
                    }
                    TeleportUtil.teleportGo(player, go, false, true);
                    return;
                }
                Mandatory.sendMessage(player, "messages.go.notFound");
                return;
            }
            if (GoManager.goExists(args[0])) {
                Go go = GoManager.getGo(args[0]);
                assert go != null;
                if (go.getLocation() == null) {
                    Mandatory.sendMessage(player, "messages.go.locationNotSet");
                    return;
                }
                TeleportUtil.teleportGo(player, go, false, false);
                return;
            }
            Mandatory.sendMessage(player, "messages.go.notFound");
            return;
        }
        if (args.length == 2) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                Mandatory.sendMessage(player, "messages.go.notFound");
                return;
            }

            switch (args[1]) {
                case "delete" -> {
                    if (!player.hasPermission("mandatory.command.go.delete") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.delete.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    GoManager.deleteGo(go);
                    Mandatory.sendMessage(player, "messages.go.deleted", "%name%", go.getName());
                    return;
                }
                case "whitelist" -> {
                    if (!player.hasPermission("mandatory.command.go.whitelist") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.whitelist.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    List<String> whitelistedPlayers = MPlayerManager.getPlayerNamesFromUUIDs(go.getWhitelistPlayers());
                    Mandatory.sendMessage(player, "messages.go.whitelist.list", "%name%", go.getName(), "%count%", String.valueOf(whitelistedPlayers.size()), "%players%", Utilities.formatStringList(whitelistedPlayers));
                    return;
                }
                case "info" -> {
                    if (!player.hasPermission("mandatory.command.go.info")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    Mandatory.sendMessage(player, "messages.go.info.header", "%name%", go.getName());
                    if (go.getDisplayname() != null && !go.getDisplayname().isEmpty()) {
                        Mandatory.sendMessage(player, "messages.go.info.displayname", "%displayname%", go.getDisplayname());
                    }
                    Mandatory.sendMessage(player, "messages.go.info.owner", "%owner%", Objects.requireNonNull(MPlayerManager.getPlayerFromUUID(go.getOwnerUUID())).getName());
                    Mandatory.sendMessage(player, "messages.go.info.open.open", "%open%", go.isOpen() ? Mandatory.getMessage("messages.go.info.open.true") : Mandatory.getMessage("messages.go.info.open.false"));
                    Mandatory.sendMessage(player, "messages.go.info.created", "%created%", DateUtil.formatDate(go.getCreateDate()));
                    Mandatory.sendMessage(player, "messages.go.info.latestUse", "%latestUse%", DateUtil.formatDate(GoManager.getLatestUse(go)));
                    String price = go.getChargeAmount() == 0 ? Mandatory.getMessage("messages.free") : go.getChargeAmount() + " encoins";
                    Mandatory.sendMessage(player, "messages.go.info.price", "%price%", price);
                    return;
                }
            }
            Mandatory.sendMessage(player, "messages.go.syntax");
            return;
        }
        if (args.length == 3) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                Mandatory.sendMessage(player, "messages.go.notFound");
                return;
            }

            switch (args[1]) {
                case "toggle" -> {
                    if (args[2].equalsIgnoreCase("open")) {
                        if (!player.hasPermission("mandatory.command.go.toggle.open") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.toggle.open.others")) {
                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        go.setOpen(!go.isOpen());
                        Mandatory.sendMessage(player, go.isOpen() ? "messages.go.opened" : "messages.go.closed", "%name%", go.getName());
                        return;
                    }
                    if (args[2].equalsIgnoreCase("whitelistcharge")) {
                        if (!player.hasPermission("mandatory.command.go.toggle.whitelistcharge") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.toggle.whitelistcharge.others")) {
                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        go.setWhitelistcharge(!go.isWhitelistcharge());
                        Mandatory.sendMessage(player, go.isWhitelistcharge() ? "messages.go.whitelistcharge.enabled" : "messages.go.whitelistcharge.disabled", "%name%", go.getName());
                        return;
                    }
                    Mandatory.sendMessage(player, "messages.go.syntax");
                    return;
                }
                case "whitelist" -> {
                    if (!player.hasPermission("mandatory.command.go.whitelist") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.whitelist.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[2].substring(1));
                    if (mPlayer == null) {
                        Mandatory.sendMessage(player, "messages.error.playerNotFound");
                        return;
                    }
                    if (args[2].startsWith("+")) {
                        if (go.isWhitelisted(mPlayer.getUuid())) {
                            Mandatory.sendMessage(player, "messages.go.whitelist.alreadyAdded", "%name%", go.getName(), "%player%", mPlayer.getName());
                            return;
                        }
                        go.addToWhitelist(mPlayer.getUuid());
                        Mandatory.sendMessage(player, "messages.go.whitelist.added", "%name%", go.getName(), "%player%", mPlayer.getName());
                        return;
                    }
                    if (args[2].startsWith("-")) {
                        if (!go.isWhitelisted(mPlayer.getUuid())) {
                            Mandatory.sendMessage(player, "messages.go.whitelist.alreadyRemoved", "%name%", go.getName(), "%player%", mPlayer.getName());
                            return;
                        }
                        go.removeFromWhitelist(mPlayer.getUuid());
                        Mandatory.sendMessage(player, "messages.go.whitelist.removed", "%name%", go.getName(), "%player%", mPlayer.getName());
                        return;
                    }
                    Mandatory.sendMessage(player, "messages.go.syntax");
                    return;
                }
                case "give" -> {
                    if (!player.hasPermission("mandatory.command.go.give") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.give.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[2]);
                    if (mPlayer == null) {
                        Mandatory.sendMessage(player, "messages.error.playerNotFound");
                        return;
                    }
                    if (mPlayer.getUuid().equals(player.getUniqueId())) {
                        Mandatory.sendMessage(player, "messages.go.give.self");
                        return;
                    }
                    go.setOwnerUUID(mPlayer.getUuid());
                    Mandatory.sendMessage(player, "messages.go.give.given", "%name%", go.getName(), "%player%", mPlayer.getName());
                    if (Mandatory.getInstance().getServer().getPlayer(mPlayer.getUuid()) != null) {
                        Mandatory.sendMessage(Objects.requireNonNull(Mandatory.getInstance().getServer().getPlayer(mPlayer.getUuid())), "messages.go.give.received", "%name%", go.getName(), "%player%", player.getName());
                    }
                    return;
                }
                case "set" -> {
                    if (args[2].equalsIgnoreCase("location")) {
                        if (!player.hasPermission("mandatory.command.go.set.location") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.location.others")) {
                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        if (!(go.getLocation() == null) && !player.hasPermission("mandatory.command.go.set.location.force")) {
                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            return;
                        }
                        Location location = player.getLocation();
                        go.setLocation(location);
                        Mandatory.sendMessage(player, "messages.go.set.location", "%name%", go.getName());
                        return;
                    }
                }
            }
            Mandatory.sendMessage(player, "messages.go.syntax");
            return;
        }
        if (args.length == 4) {
            Go go = GoManager.getGo(args[0]);
            if (go == null) {
                Mandatory.sendMessage(player, "messages.go.notFound");
                return;
            }
            if (!args[1].equalsIgnoreCase("set")) {
                Mandatory.sendMessage(player, "messages.go.syntax");
                return;
            }
            switch (args[2]) {
                case "displayname" -> {
                    if (!player.hasPermission("mandatory.command.go.set.displayname") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.displayname.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    if (args[3].equals("-")) {
                        go.setDisplayName(null);
                        Mandatory.sendMessage(player, "messages.go.set.displayname.removed", "%name%", go.getName());
                        return;
                    }
                    if (args[3].length() > Mandatory.configuration.getGoDisplayNameLimit()) {
                        Mandatory.sendMessage(player, "messages.go.set.displayname.tooLong", "%max%", String.valueOf(Mandatory.configuration.getGoDisplayNameLimit()));
                        return;
                    }
                    go.setDisplayName(args[3]);
                    Mandatory.sendMessage(player, "messages.go.set.displayname.success", "%name%", go.getName());
                    return;
                }
                case "price" -> {
                    if (!player.hasPermission("mandatory.command.go.set.price") || !go.getOwnerUUID().equals(player.getUniqueId()) && !player.hasPermission("mandatory.command.go.set.price.others")) {
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                        return;
                    }
                    if (args[3].equals("-")) {
                        go.setChargeAmount(0);
                        Mandatory.sendMessage(player, "messages.go.set.chargeamount.removed", "%name%", go.getName());
                        return;
                    }
                    if (!args[3].matches("[0-9]+")) {
                        Mandatory.sendMessage(player, "messages.go.syntax");
                        return;
                    }
                    int chargeAmount = Integer.parseInt(args[3]);
                    if (chargeAmount > Mandatory.configuration.getGoChargeAmount()) {
                        Mandatory.sendMessage(player, "messages.go.set.chargeamount.tooMuch", "%max%", String.valueOf(Mandatory.configuration.getGoChargeAmount()));
                        return;
                    }
                    go.setChargeAmount(chargeAmount);
                    Mandatory.sendMessage(player, "messages.go.set.chargeamount.set", "%name%", go.getName());
                    return;
                }
            }
            Mandatory.sendMessage(player, "messages.go.syntax");
        }
    }

    public void showList(Player player, int pageStart, List<Go> list) {
        StringBuilder tmpMessage = new StringBuilder();
        int itemsPerPage = 25;
        int start = (pageStart * itemsPerPage) - itemsPerPage;
        int stop = Math.min(pageStart * itemsPerPage, list.size()); // Ensure stop does not exceed list size

        if (list.isEmpty()) {
            Mandatory.sendMessage(player, "messages.go.list.empty");
            return;
        }
        if (start >= list.size()) {
            Mandatory.sendMessage(player, "messages.go.list.notEnoughPages");
            return;
        }

        for (int i = start; i < stop; i++) {
            tmpMessage.append(list.get(i).getName()).append(", ");
        }
        int displayStart = start + 1;
        Mandatory.sendMessage(player, "messages.go.list.list", "%start%", String.valueOf(displayStart), "%end%", String.valueOf(((int) Math.ceil(((double) list.size()) / ((double) itemsPerPage)))), "%next%", String.valueOf(pageStart + 1), "%previous%", String.valueOf(pageStart - 1));
        player.sendMessage("§2" + tmpMessage.substring(0, tmpMessage.length() - 2));
    }

}
