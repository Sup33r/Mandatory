package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.ChestManager;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;

@CommandAlias("admin|ad")
@CommandPermission("mandatory.command.admin")
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
    @CommandCompletion("@players @adminToggleable")
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
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('c')) {
                currentFlags = currentFlags.replace("c", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.admin.toggle.chatcolor.removed", "%player%", mPlayer.getName());
            } else {
                currentFlags += "c";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.chestmessages.added", "%player%", mPlayer.getName());
            }
            return;
        }
        if (action.equalsIgnoreCase("extprefix")) {
            String currentFlags = new String(mPlayer.getFlags());

            if (mPlayer.hasFlag('e')) {
                currentFlags = currentFlags.replace("e", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.admin.toggle.externalprefix.removed", "%player%", mPlayer.getName());
            } else {
                currentFlags += "e";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.admin.toggle.externalprefix.added", "%player%", mPlayer.getName());
            }
        }
    }

    @Subcommand("set")
    @CommandCompletion("@adminSettable")
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
                if (args[1].equalsIgnoreCase("-")) {
                    mPlayer.removeChatprefix();
                    Mandatory.sendMessage(player,"messages.admin.set.chatprefix.removed", "%player%", mPlayer.getName());
                    return;
                }
                //make a string of all the args after the first one
                StringBuilder prefix = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    prefix.append(args[i]).append(" ");
                }
                mPlayer.setChatprefix(prefix.toString());
                Mandatory.sendMessage(player,"messages.admin.set.chatprefix.success", "%player%", mPlayer.getName());
            }
        }
    }

    @Subcommand("chat")
    public class ChatAdmin extends BaseCommand {
        @Subcommand("force")
        @CommandCompletion("@players @channels")
        public void onForce(Player admin, OnlinePlayer target, String channelName) {
            if (!admin.hasPermission("mandatory.command.admin.chat.force")) {
                Mandatory.sendMessage(admin, "messages.error.permissionDenied");
                return;
            }
            Channel channel = ChannelManager.getChannel(channelName);
            if (channel == null) {
                Mandatory.sendMessage(admin,"messages.error.channelNotFound");
                return;
            }
            ChannelManager.setActiveChannel(target.getPlayer(), channel.getName());
            ChannelManager.joinChannel(target.getPlayer(), channel.getName());
            Mandatory.sendMessage(admin,"messages.admin.chat.force.success", "%player%", target.getPlayer().getName(), "%channel%", channel.getName());
        }

        @Subcommand("new")
        @CommandCompletion("@nothing @channelTypes")
        public void onNew(Player player, String channelName, String type) {
            if (!player.hasPermission("mandatory.command.admin.chat.new")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            ChannelManager.createChannel(channelName, ChannelType.getByName(type.toLowerCase()));
            Mandatory.sendMessage(player,"messages.admin.chat.new.success", "%channel%", channelName);
        }

        @Subcommand("delete")
        @CommandCompletion("@channels")
        public void onDelete(Player player, String channelName) {
            if (!player.hasPermission("mandatory.command.admin.chat.delete")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            Channel channel = ChannelManager.getChannel(channelName);
            if (channel == null) {
                Mandatory.sendMessage(player,"messages.error.channelNotFound");
                return;
            }
            ChannelManager.deleteChannel(channel.getName());
            Mandatory.sendMessage(player,"messages.admin.chat.delete.success", "%channel%", channelName);
        }

        @Subcommand("disable")
        @CommandCompletion("@channels")
        public void onDisable(Player player, String channelName) {
            if (!player.hasPermission("mandatory.command.admin.chat.disable")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            Channel channel = ChannelManager.getChannel(channelName);
            if (channel == null) {
                Mandatory.sendMessage(player,"messages.error.channelNotFound");
                return;
            }
            if (channel.isDisabled()) {
                channel.setDisabled(false);
                Mandatory.sendMessage(player,"messages.admin.chat.disabled", "%channel%", channelName);
                return;
            }
            channel.setDisabled(true);
            Mandatory.sendMessage(player,"messages.admin.chat.enabled", "%channel%", channelName);
        }

        @Subcommand("override")
        @CommandCompletion("@players")
        public void onOverride(Player player, @Optional OnlinePlayer target) {
            if (!player.hasPermission("mandatory.command.admin.chat.override")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            if (target == null) {
                if (Mandatory.chatOverride.contains(player)) {
                    Mandatory.chatOverride.remove(player);
                    Mandatory.sendMessage(player, "messages.admin.chat.override.disabled.self");
                } else {
                    Mandatory.chatOverride.add(player);
                    Mandatory.sendMessage(player, "messages.admin.chat.override.enabled.self");
                }
            } else {
                if (Mandatory.chatOverride.contains(target.getPlayer())) {
                    Mandatory.chatOverride.remove(target.getPlayer());
                    Mandatory.sendMessage(player, "messages.admin.chat.override.disabled.others", "%player%", target.getPlayer().getName());
                } else {
                    Mandatory.chatOverride.add(target.getPlayer());
                    Mandatory.sendMessage(player, "messages.admin.chat.override.enabled.others", "%player%", target.getPlayer().getName());
                }
            }
        }
    }

    @Subcommand("bannedwords")
    public void onBannedWords(Player player) {
        if (!player.hasPermission("mandatory.command.admin.bannedwords.list")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Mandatory.sendMessage(player,"messages.admin.bannedwords.list", "%words%", String.join(", ", Mandatory.configuration.getBannedWords()));
    }

    @Subcommand("bannedwords")
    public class BannedWords extends BaseCommand {
        @Subcommand("add")
        public void onAdd(Player player, String word) {
            if (!player.hasPermission("mandatory.command.admin.bannedwords.add")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            Mandatory.configuration.addBannedWord(word);
            Mandatory.sendMessage(player,"messages.admin.bannedwords.added", "%word%", word);
        }

        @Subcommand("remove")
        @CommandCompletion("@bannedwords")
        public void onRemove(Player player, String word) {
            if (!player.hasPermission("mandatory.command.admin.bannedwords.remove")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            Mandatory.configuration.removeBannedWord(word);
            Mandatory.sendMessage(player,"messages.admin.bannedwords.removed", "%word%", word);
        }

        @Subcommand("list")
        public void onList(Player player) {
            if (!player.hasPermission("mandatory.command.admin.bannedwords.list")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            Mandatory.sendMessage(player,"messages.admin.bannedwords.list", "%words%", String.join(", ", Mandatory.configuration.getBannedWords()));
        }
    }
}
