package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.ChestManager;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.*;
import org.bukkit.entity.Player;

import java.util.Arrays;

@CommandAlias("admin|ad")
public class CommandAdmin extends BaseCommand {

    @Default
    public void onAdmin(Player player) {
        if (!player.hasPermission("mandatory.command.admin")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Mandatory.sendMessage(player,"messages.admin.syntax");
    }

    @Subcommand("override")
    @CommandCompletion("@overrides")
    public void onOverride(Player player, String action) {
        if (!player.hasPermission("mandatory.command.admin.override")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (action.equalsIgnoreCase("chest")) {
            if (ChestManager.chestOverride.contains(player)) {
                ChestManager.chestOverride.remove(player);
                Mandatory.sendMessage(player, "messages.chest.override.disabled");
            } else {
                ChestManager.chestOverride.add(player);
                Mandatory.sendMessage(player, "messages.chest.override.enabled");
            }
        }
        if (action.equalsIgnoreCase("map")) {
            if (Mandatory.mapOverride.contains(player)) {
                Mandatory.mapOverride.remove(player);
                Mandatory.sendMessage(player, "messages.map.override.disabled");
            } else {
                Mandatory.mapOverride.add(player);
                Mandatory.sendMessage(player, "messages.map.override.enabled");
            }
        }
    }

    @Subcommand("disablename")
    public void onDisablename(Player player, String name) {
        if (!player.hasPermission("mandatory.command.admin.disablename")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Mandatory.configuration.addDisabledName(name);
        Mandatory.sendMessage(player,"messages.admin.disablename.success", "%name%", name);
    }

    @Subcommand("enable")
    public void onEnable(Player player, String name) {
        if (!player.hasPermission("mandatory.command.admin.enable")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(name);
        if (mPlayer == null) {
            Mandatory.sendMessage(player,"messages.error.playerNotFound");
            return;
        }
        if (!mPlayer.hasFlag('s')) {
            Mandatory.sendMessage(player, "messages.admin.enable.alreadyEnabled");
        }
        mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "s");
        Mandatory.sendMessage(player,"messages.admin.enable.success", "%player%", mPlayer.getName());
    }

    @Subcommand("disable")
    public void onDisable(Player player, String name) {
        if (!player.hasPermission("mandatory.command.admin.disable")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(name);
        if (mPlayer == null) {
            Mandatory.sendMessage(player,"messages.error.playerNotFound");
            return;
        }
        if (mPlayer.hasFlag('s')) {
            Mandatory.sendMessage(player, "messages.admin.disable.alreadyDisabled");
        }
        mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("s", ""));
        Mandatory.sendMessage(player,"messages.admin.disable.success", "%player%",mPlayer.getName());
    }

    // c = chatcolor, d = deathmessage, e = externalprefix, g = greeting, i = insecureconnections, j = joinleaves, m = chestmessages, p = payments, t = teleport, v = vipprefi, s = stadsprefix | [GRÅ[Anka] GRÖN[B]] GUL[VIP] BLÅ[ADMIN]

    @Subcommand("toggle")
    @CommandCompletion("@players @toggleable")
    public void onToggle(Player player, String target, String action) {
        if (!player.hasPermission("mandatory.command.admin.toggle")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(target);
        if (mPlayer == null) {
            Mandatory.sendMessage(player,"messages.error.playerNotFound");
            return;
        }
        if (action.equalsIgnoreCase("chatcolor")) {
            if (mPlayer.hasFlag('c')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("c", ""));
                Mandatory.sendMessage(player,"messages.admin.toggle.chatcolor.removed", "%player%", mPlayer.getName());
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "c");
                Mandatory.sendMessage(player,"messages.admin.toggle.chatcolor.added", "%player%", mPlayer.getName());
            }
            return;
        }
        if (action.equalsIgnoreCase("extprefix")) {
            if (mPlayer.hasFlag('e')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("e", ""));
                Mandatory.sendMessage(player,"messages.admin.toggle.externalprefix.removed", "%player%", mPlayer.getName());
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "e");
                Mandatory.sendMessage(player,"messages.admin.toggle.externalprefix.added", "%player%", mPlayer.getName());
            }
        }
    }

    @Subcommand("set")
    public void onSet(Player player, String[] args) {
        if (!player.hasPermission("mandatory.command.admin.set")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (args.length == 0 || args.length == 2) {
            Mandatory.sendMessage(player,"messages.admin.set.syntax");
            return;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("afk")) {
                if (!player.hasPermission("mandatory.command.admin.set.afk")) {
                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    return;
                }
                Mandatory.configuration.setAfkLocation(player.getLocation());
                Mandatory.sendMessage(player,"messages.admin.set.afk");
            }
            if (args[0].equalsIgnoreCase("spawn")) {
                if (!player.hasPermission("mandatory.command.admin.set.spawn")) {
                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    return;
                }
                Mandatory.configuration.setSpawnLocation(player.getLocation());
                Mandatory.sendMessage(player,"messages.admin.set.spawn");
            }
            if (args[0].equalsIgnoreCase("firstspawn")) {
                if (!player.hasPermission("mandatory.command.admin.set.firstspawn")) {
                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    return;
                }
                Mandatory.configuration.setFirstSpawnLocation(player.getLocation());
                Mandatory.sendMessage(player,"messages.admin.set.firstspawn");
            }
            return;
        }
        if (args.length == 3) {
            MPlayer mPlayer = ApiedAPI.getPlayer(args[1]);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            if (args[0].equalsIgnoreCase("chatprefix")) {
                if (!player.hasPermission("mandatory.command.admin.set.chatprefix")) {
                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    return;
                }
                mPlayer.setChatprefix(args[2]);
                Mandatory.sendMessage(player,"messages.admin.set.chatprefix.success", "%player%", mPlayer.getName());
            }
        }
    }

    @Subcommand("chat")
    public class ChatAdmin extends BaseCommand {

        @Subcommand("force")
        public void onForce(Player admin, OnlinePlayer target, String channelName) {
            // Implement forcing a player into a channel
        }

        @Subcommand("new")
        public void onNew(Player player, String channelName, String type) {
            if (!player.hasPermission("mandatory.command.admin.chat.new")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            ChannelManager.createChannel(channelName, ChannelType.getByName(type.toLowerCase()));
            Mandatory.sendMessage(player,"messages.admin.chat.new.success", "%channel%", channelName);
        }

        // Other admin chat commands
    }
}
