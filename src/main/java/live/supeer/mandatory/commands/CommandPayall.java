package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@CommandAlias("payall")
public class CommandPayall extends BaseCommand {

    @Default
    public void onPayall(Player player, int amount, String reason) {
        if (!player.hasPermission("mandatory.command.payall")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }

        if (reason.split(" ").length > 2) {
            Mandatory.sendMessage(player, "messages.pay.reasonInvalid");
            return;
        }

        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        List<MPlayer> players = new ArrayList<>(MPlayerManager.getPlayers());
        if (players.size() == 1) {
            Mandatory.sendMessage(player, "messages.pay.noPlayers");
            return;
        }
        if (players.size() == 2) {
            Mandatory.sendMessage(player, "messages.pay.onlyOnePlayer");
            return;
        }

        if (mPlayer.getBalance() < amount*players.size()-1) {
            Mandatory.sendMessage(player, "messages.pay.notEnoughMoney", "%amount%", Utilities.formattedMoney(amount));
            return;
        }

        for (MPlayer mPlayer1 : players) {
            if (mPlayer1.getUuid().equals(player.getUniqueId())) {
                continue;
            }
            mPlayer1.addBalance(amount, "{ \"type\": \"player\", \"subtype\": \"receive\", \"playerUUID\": \"" + player.getUniqueId() + "\", \"reason\": \"" + reason + "\"}");
            mPlayer.removeBalance(amount, "{ \"type\": \"player\", \"subtype\": \"send\", \"playerUUID\": \"" + mPlayer1.getUuid() + "\", \"reason\": \"" + reason + "\"}");
            Player targetPlayer = Bukkit.getPlayer(mPlayer1.getUuid());
            if (targetPlayer != null) {
                Mandatory.sendMessage(targetPlayer, "messages.pay.received", "%player%", player.getName(), "%amount%", Utilities.formattedMoney(amount), "%reason%", reason);
            }
        }
    }
}
