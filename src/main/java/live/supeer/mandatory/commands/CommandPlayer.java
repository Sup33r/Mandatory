package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.annotation.Subcommand;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.apied.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Arrays;

@CommandAlias("player|p")
public class CommandPlayer extends BaseCommand {

    @Default
    public void onPlayer(Player player, @Optional String playerName) {
        if (playerName == null) {
            playerName = player.getName();
        }
        MPlayer mPlayer = ApiedAPI.getPlayer(playerName);
        if (mPlayer == null) {
            Mandatory.sendMessage(player,"messages.error.playerNotFound");
            return;
        }
        Mandatory.sendMessage(player, "messages.player.info.header", "%name%", mPlayer.getName());
        Mandatory.sendMessage(player, "messages.player.info.playedSince", "%date%", DateUtil.formatDate(mPlayer.getFirstJoin()));
        if (MPlayerManager.getPlayers().contains(mPlayer)) {
            Mandatory.sendMessage(player, "messages.player.info.status.online", "%playedSince%", DateUtil.formatDateDiff(mPlayer.getLatestJoin()));
        } else {
            Mandatory.sendMessage(player, "messages.player.info.status.offline", "%playedLatest%", DateUtil.formatDateDiff(mPlayer.getLatestLeave()));
        }
        if (player.hasPermission("mandatory.command.player.info.location") || mPlayer.getUuid().equals(player.getUniqueId())) {
            if (MPlayerManager.getPlayers().contains(mPlayer)) {
                Player target = Mandatory.getInstance().getServer().getPlayer(mPlayer.getUuid());
                if (target == null) {
                    Mandatory.sendMessage(player,"messages.error.playerNotFound");
                    return;
                }
                Mandatory.sendMessage(player, "messages.player.info.latestPosition", "%location%", LocationUtil.formatLocation(target.getLocation()));
            } else {
                Mandatory.sendMessage(player, "messages.player.info.latestPosition", "%location%", LocationUtil.formatLocation(mPlayer.getLastLocation()));
            }
        }
        Mandatory.sendMessage(player, "messages.player.info.wallet", "%amount%", Utilities.formattedMoney(mPlayer.getBalance()));

    }

    @Subcommand("toggle")
    public class Toggle extends BaseCommand {

        @Subcommand("chestmessages")
        public void onChestMessages(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.chestmessages")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;

            String currentFlags = new String(mPlayer.getFlags());

            if (mPlayer.hasFlag('m')) {
                currentFlags = currentFlags.replace("m", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.chestmessages.ioff");
            } else {
                currentFlags += "m";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.chestmessages.ion");
            }
        }

        @Subcommand("chatcolor")
        public void onChatColor(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.chatcolor")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }

            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;

            String currentFlags = new String(mPlayer.getFlags());

            if (mPlayer.hasFlag('c')) {
                currentFlags = currentFlags.replace("c", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.chatcolor.ioff");
            } else {
                currentFlags += "c";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.chatcolor.ion");
            }
        }


        @Subcommand("externalprefixes")
        public void onExternalPrefixes(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.externalprefixes")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }

            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;

            String currentFlags = new String(mPlayer.getFlags());

            if (mPlayer.hasFlag('e')) {
                currentFlags = currentFlags.replace("e", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.externalprefixes.ioff");
            } else {
                currentFlags += "e";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.externalprefixes.ion");
            }
        }

        @Subcommand("greeting")
        public void onGreeting(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.greeting")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }

            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;

            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('g')) {
                currentFlags = currentFlags.replace("g", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.greeting.ioff");
            } else {
                currentFlags += "g";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.greeting.ion");
            }
        }

        @Subcommand("insecureconnections")
        public void onInsecureConnections(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.insecureconnections")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('i')) {
                currentFlags = currentFlags.replace("i", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.insecureconnections.ioff");
            } else {
                currentFlags += "i";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.insecureconnections.ion");
            }
        }

        @Subcommand("joinleaves")
        public void onJoinLeaves(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.joinleaves")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('j')) {
                currentFlags = currentFlags.replace("j", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.joinleaves.ioff");
            } else {
                currentFlags += "j";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.joinleaves.ion");
            }
        }

        @Subcommand("payments")
        public void onPayments(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.payments")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('p')) {
                currentFlags = currentFlags.replace("p", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.payments.ioff");
            } else {
                currentFlags += "p";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.payments.ion");
            }
        }

        @Subcommand("teleport")
        public void onTeleport(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.teleport")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('t')) {
                currentFlags = currentFlags.replace("t", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.teleport.ioff");
            } else {
                currentFlags += "t";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.teleport.ion");
            }
        }

        @Subcommand("invites")
        public void onInvites(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.invites")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;
            String currentFlags = new String(mPlayer.getFlags());
            if (mPlayer.hasFlag('v')) {
                currentFlags = currentFlags.replace("v", "");
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.invites.ioff");
            } else {
                currentFlags += "v";
                mPlayer.setFlags(currentFlags);
                Mandatory.sendMessage(player, "messages.player.toggle.invites.ion");
            }
        }
    }
}
