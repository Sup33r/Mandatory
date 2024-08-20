package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import live.supeer.apied.*;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@CommandAlias("chest")
public class CommandChest extends BaseCommand {

    @Default
    public void onChest(Player player) {
        if (!player.hasPermission("mandatory.command.chest")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Mandatory.sendMessage(player, "messages.chest.syntax");
    }

    @Subcommand("override")
    public void onOverride(Player player) {
        if (!player.hasPermission("mandatory.command.chest.override")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (ChestManager.chestOverride.contains(player)) {
            ChestManager.chestOverride.remove(player);
            Mandatory.sendMessage(player, "messages.chest.override.disabled");
        } else {
            ChestManager.chestOverride.add(player);
            Mandatory.sendMessage(player, "messages.chest.override.enabled");
        }
    }

    @Subcommand("cancel")
    public void onCancel(Player player) {
        if (!player.hasPermission("mandatory.command.chest.cancel")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (ChestManager.isChestEventCounting(player.getUniqueId()) || ChestManager.chestInventories.containsKey(player.getUniqueId())) {
            ChestManager.chestInventories.remove(player.getUniqueId());
            ChestManager.stopChestEventCount(player.getUniqueId());
            Mandatory.sendMessage(player, "messages.chest.cancel");
        } else {
            Mandatory.sendMessage(player, "messages.chest.error.noEvent");
        }
    }

    @Subcommand("claim")
    public void onClaim(Player player) {
        if (!player.hasPermission("mandatory.command.chest.claim")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (ChestManager.hasArchivedItems(player.getUniqueId())) {
            int id = ChestManager.getFirstArchiveId(player.getUniqueId());
            ChestManager.chestInventories.put(player.getUniqueId(), id);
            player.sendMessage(id+ ": id");
            Mandatory.sendMessage(player, "messages.chest.claim");
        } else {
            Mandatory.sendMessage(player, "messages.chest.error.noItems");
        }
    }

    @Subcommand("info")
    public void onInfo(Player player, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.chest.info")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (count == null || count <= 0) {
            count = 1;
        }
        ChestManager.startChestEventCount(player.getUniqueId(), "info", count, null);
        Mandatory.sendMessage(player, "messages.chest.info.started", "%count%", String.valueOf(count));
    }

    @Subcommand("share")
    public void onShare(Player player, String target, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.chest.share")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(target);
        if (mPlayer == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (mPlayer.getUuid().equals(player.getUniqueId())) {
            Mandatory.sendMessage(player, "messages.chest.error.selfShare");
            return;
        }
        if (count == null || count <= 0) {
            count = 1;
        }
        ChestManager.startChestEventCount(player.getUniqueId(), "share", count, mPlayer.getUuid());
        Mandatory.sendMessage(player, "messages.chest.share.started", "%player%", target, "%count%", String.valueOf(count));
    }

    @Subcommand("give")
    public void onGive(Player player, String target, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.chest.give")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(target);
        if (mPlayer == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (mPlayer.getUuid().equals(player.getUniqueId())) {
            Mandatory.sendMessage(player, "messages.chest.error.selfGive");
            return;
        }
        if (count == null || count <= 0) {
            count = 1;
        }
        ChestManager.startChestEventCount(player.getUniqueId(), "give", count, mPlayer.getUuid());
        Mandatory.sendMessage(player, "messages.chest.give.started", "%player%", target, "%count%", String.valueOf(count));

    }

    @Subcommand("archive")
    public void onArchive(Player player, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.chest.archive")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (count == null || count <= 0) {
            count = 1;
        }
        ChestManager.startChestEventCount(player.getUniqueId(), "archive", count, null);
        Mandatory.sendMessage(player, "messages.chest.archive.started", "%count%", String.valueOf(count));
    }


}
