package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
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
        MPlayer mPlayer = MPlayerManager.getPlayerFromName(name);
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
        MPlayer mPlayer = MPlayerManager.getPlayerFromName(name);
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

    @Subcommand("toggle")
    public void onToggle(Player player, String target, String action) {
        if (!player.hasPermission("mandatory.command.admin.toggle")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromName(target);
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
            MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[1]);
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
}
