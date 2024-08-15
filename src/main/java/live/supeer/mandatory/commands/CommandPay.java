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

import java.lang.reflect.Array;
import java.util.Set;

@CommandAlias("pay")
public class CommandPay extends BaseCommand {

    @Default
    public void onPay(Player player, String target, int amount, String reason) {
        if (!player.hasPermission("mandatory.command.pay")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }

        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;

        MPlayer targetPlayer = MPlayerManager.getPlayerFromName(target);
        assert targetPlayer != null;
        if (mPlayer.getUuid().equals(targetPlayer.getUuid())) {
            Mandatory.sendMessage(player, "messages.pay.cantPaySelf");
            return;
        }
        if (mPlayer.getBalance() < amount) {
            Mandatory.sendMessage(player, "messages.pay.notEnoughMoney", "%amount%", Utilities.formattedMoney(amount));
            return;
        }
        if (reason.split(" ").length > 2) {
            Mandatory.sendMessage(player, "messages.pay.reasonInvalid");
            return;
        }
        targetPlayer.addBalance(amount,"{ \"type\": \"player\", \"subtype\": \"receive\", \"playerUUID\": \"" + player.getUniqueId() + "\", \"reason\": \"" + reason + "\"}");
        mPlayer.removeBalance(amount,"{ \"type\": \"player\", \"subtype\": \"send\", \"playerUUID\": \"" + targetPlayer.getUuid().toString() + "\", \"reason\": \"" + reason + "\"}");
        Mandatory.sendMessage(player, "messages.pay.success", "%target%", target, "%amount%", Utilities.formattedMoney(amount), "%reason%", reason);
        Player targetPlayerPlayer = Bukkit.getServer().getPlayer(targetPlayer.getUuid());
        if (targetPlayerPlayer != null) {
            Mandatory.sendMessage(targetPlayerPlayer, "messages.pay.received", "%player%", player.getName(), "%amount%", Utilities.formattedMoney(amount), "%reason%", reason);
        }
    }
}
