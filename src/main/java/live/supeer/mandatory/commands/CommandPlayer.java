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
            if (mPlayer.hasFlag('m')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("m", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.chestmessages.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "m");
                Mandatory.sendMessage(player, "messages.player.toggle.chestmessages.on");
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
            if (mPlayer.hasFlag('c')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("c", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.chatcolor.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "c");
                Mandatory.sendMessage(player, "messages.player.toggle.chatcolor.on");
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
            if (mPlayer.hasFlag('e')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("e", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.externalprefixes.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "e");
                Mandatory.sendMessage(player, "messages.player.toggle.externalprefixes.on");
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
            if (mPlayer.hasFlag('g')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("g", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.greeting.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "g");
                Mandatory.sendMessage(player, "messages.player.toggle.greeting.on");
            }
        }

        @Subcommand("insecureconnections")
        public void onInsecureConnections(Player player) {
            if (!player.hasPermission("mandatory.command.player.toggle.insecureconnections")) {
                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                return;
            }

            MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
            assert mPlayer != null;
            if (mPlayer.hasFlag('i')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("i", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.insecureconnections.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "i");
                Mandatory.sendMessage(player, "messages.player.toggle.insecureconnections.on");
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
            if (mPlayer.hasFlag('j')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("j", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.joinleaves.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "j");
                Mandatory.sendMessage(player, "messages.player.toggle.joinleaves.on");
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
            if (mPlayer.hasFlag('p')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("p", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.payments.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "p");
                Mandatory.sendMessage(player, "messages.player.toggle.payments.on");
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
            if (mPlayer.hasFlag('t')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("t", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.teleport.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "t");
                Mandatory.sendMessage(player, "messages.player.toggle.teleport.on");
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
            if (mPlayer.hasFlag('v')) {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()).replace("v", ""));
                Mandatory.sendMessage(player, "messages.player.toggle.invites.off");
            } else {
                mPlayer.setFlags(Arrays.toString(mPlayer.getFlags()) + "v");
                Mandatory.sendMessage(player, "messages.player.toggle.invites.on");
            }
        }
    }
}
