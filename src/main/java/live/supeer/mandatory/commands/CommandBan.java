package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Default;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.apied.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.metropolis.acf.annotation.CommandPermission;
import live.supeer.metropolis.utils.DateUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;

@CommandAlias("ban")
public class CommandBan extends BaseCommand {

    @Default
    @CommandCompletion("@players @nothing")
    @CommandPermission("mandatory.command.ban")
    public void onBan(Player player, String target, String arguments) {
        MPlayer targetPlayer = ApiedAPI.getPlayer(target);
        if (targetPlayer == null) {
            Mandatory.sendMessage(player, "messages.error.playerNotFound");
            return;
        }
        if (targetPlayer.isBanned()) {
            Mandatory.sendMessage(player, "messages.ban.alreadyBanned");
            return;
        }
        long length = DateUtil.parseDateDiff(arguments, true);
        String reason = DateUtil.removeTimePattern(arguments);
        boolean noReason = reason.isEmpty();
        if (noReason) {
            Mandatory.sendMessage(player, "messages.ban.noReason");
            return;
        }
        String expiryDate = DateUtil.formatDateDiff(length);
        MPlayerManager.placeBan(targetPlayer, player, reason, length/1000L);
        Player targetOnline = Bukkit.getPlayer(targetPlayer.getUuid());
        if (targetOnline != null) {
            Mandatory.bannedPlayers.add(targetOnline);
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ban");
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(reason);
            player.sendPluginMessage(Mandatory.getInstance(), "proxie:proxie", out.toByteArray());
        }
        Mandatory.sendMessage(player, "messages.ban.success", "%player%", targetPlayer.getName(), "%length%", expiryDate, "%reason%", reason);
    }
}
