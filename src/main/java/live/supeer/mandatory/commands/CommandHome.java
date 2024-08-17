package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.Home;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.TeleportUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("home")
public class CommandHome extends BaseCommand {

    //Syntax: /home player:name set/delete (if has permission others)
    //Syntax: /home name set/delete (if
    @Default
    public void onHome(Player player, String[] args) {
        if (!player.hasPermission("mandatory.command.home")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (args.length == 0) {
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            Home home = mPlayer.getHome("Hem");
            if (home == null) {
                Mandatory.sendMessage(player,"messages.home.none");
                return;
            }
            TeleportUtil.teleport(player, home.getLocation(),home.getName(),false);
        }
        if (args.length == 1) {
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                Home home = mPlayer.getHome("Hem");
                if (home == null) {
                    Mandatory.sendMessage(player,"messages.home.none");
                    return;
                }
                mPlayer.removeHome(home);
                Mandatory.sendMessage(player,"messages.home.deleted");
                return;
            }
            if (args[0].equalsIgnoreCase("set")) {
                Home home = mPlayer.getHome("Hem");
                if (home == null) {
                    mPlayer.newHome("Hem", player.getLocation());
                    Mandatory.sendMessage(player,"messages.home.saved");
                    return;
                }
                home.updateHomeLocation(player.getLocation());
                Mandatory.sendMessage(player,"messages.home.updated");
                return;
            }
            if (args[0].contains(":")) {
                if (!player.hasPermission("mandatory.command.home.others")) {
                    Mandatory.sendMessage(player,"messages.error.permissionDenied");
                    return;
                }
                String[] split = args[0].split(":");
                MPlayer target = ApiedAPI.getPlayer(split[0]);
                if (target == null) {
                    Mandatory.sendMessage(player,"messages.error.playerNotFound");
                    return;
                }
                if (split.length == 1) {
                    Home home = target.getHome("Hem");
                    if (home == null) {
                        Mandatory.sendMessage(player,"messages.home.doesntExist");
                        return;
                    }
                    TeleportUtil.teleport(player, home.getLocation(),home.getName(),false);
                    return;
                }
                Home home = mPlayer.getHome(split[1]);
                if (home == null) {
                    Mandatory.sendMessage(player,"messages.home.others.doesntExist", "%player%", target.getName());
                    return;
                }
                TeleportUtil.teleport(player, home.getLocation(),home.getName(),false);
                return;
            }
            Home home = mPlayer.getHome(args[0]);
            if (home == null) {
                Mandatory.sendMessage(player,"messages.home.doesntExist");
                return;
            }
            TeleportUtil.teleport(player, home.getLocation(),home.getName(),false);
        }
        if (args.length == 2) {
            MPlayer mPlayer;
            String homeName;

            if (args[0].contains(":")) {
                if (!player.hasPermission("mandatory.command.home.others")) {
                    Mandatory.sendMessage(player,"messages.error.permissionDenied");
                    return;
                }
                String[] split = args[0].split(":");
                mPlayer = ApiedAPI.getPlayer(split[0]);
                if (mPlayer == null) {
                    Mandatory.sendMessage(player,"messages.error.playerNotFound");
                    return;
                }
                homeName = split[1];
            } else {
                mPlayer = ApiedAPI.getPlayer(player);
                if (mPlayer == null) {
                    Mandatory.sendMessage(player,"messages.error.playerNotFound");
                    return;
                }
                homeName = args[0];
            }
            boolean mPlayerEqualsPlayer = mPlayer.getUuid().equals(player.getUniqueId());
            if (args[1].equalsIgnoreCase("set")) {
                Home home = mPlayer.getHome(homeName);
                if (home == null) {
                    mPlayer.newHome(homeName, player.getLocation());
                    if (!mPlayerEqualsPlayer) {
                        Mandatory.sendMessage(player,"messages.home.others.saved", "%name%", homeName, "%player%", mPlayer.getName());
                    } else {
                        Mandatory.sendMessage(player,"messages.home.name.saved", "%name%", homeName);
                    }
                } else {
                    home.updateHomeLocation(player.getLocation());
                    if (!mPlayerEqualsPlayer) {
                        Mandatory.sendMessage(player,"messages.home.others.updated", "%name%", homeName, "%player%", mPlayer.getName());
                    } else {
                        Mandatory.sendMessage(player,"messages.home.name.updated", "%name%", homeName);
                    }
                }
            } else if (args[1].equalsIgnoreCase("delete")) {
                Home home = mPlayer.getHome(homeName);
                if (home == null) {
                    Mandatory.sendMessage(player,"messages.home.doesntExist");
                } else {
                    if (!mPlayerEqualsPlayer) {
                        Mandatory.sendMessage(player,"messages.home.others.deleted", "%name%", homeName, "%player%", mPlayer.getName());
                    } else {
                        Mandatory.sendMessage(player,"messages.home.name.deleted", "%name%", homeName);
                    }
                    mPlayer.removeHome(home);
                }
            } else {
                Mandatory.sendMessage(player,"messages.home.syntax");
            }
        }
    }

    @CommandAlias("homes")
    public void onHomes(Player player, @Optional String name) {
        if (name == null) {
            if (!player.hasPermission("mandatory.command.homes")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            List<Home> homes = mPlayer.getHomes();
            if (homes.isEmpty()) {
                Mandatory.sendMessage(player,"messages.player.homes.none");
                return;
            }
            String names = Utilities.formatStringList(homes.stream().map(Home::getName).toList());
            String max;
            if (player.hasPermission("mandatory.command.homes.extra")) {
                max = String.valueOf(Mandatory.configuration.getMaxHomesExtra());
            } else {
                max = String.valueOf(Mandatory.configuration.getMaxHomes());
            }
            Mandatory.sendMessage(player,"messages.homes.own", "%count%", String.valueOf(homes.size()), "%max%", max, "%list%", names);
        } else {
            if (!player.hasPermission("mandatory.command.homes.others")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = ApiedAPI.getPlayer(name);
            if (mPlayer == null) {
                Mandatory.sendMessage(player,"messages.error.playerNotFound");
                return;
            }
            List<Home> homes = mPlayer.getHomes();
            if (homes.isEmpty()) {
                Mandatory.sendMessage(player,"messages.player.homes.none");
                return;
            }
            String names = Utilities.formatStringList(homes.stream().map(Home::getName).toList());
            String max = String.valueOf(Mandatory.configuration.getMaxHomes());
            Mandatory.sendMessage(player,"messages.homes.others","%player%", mPlayer.getName(), "%count%", String.valueOf(homes.size()), "%max%", max, "%list%", names);
        }
    }
}
