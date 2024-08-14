package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

public class CommandBalance extends BaseCommand {

    public static Mandatory plugin;



    @Default
    public void onBalance(Player player, String[] args) {

        if (args.length == 0) {
            if (!player.hasPermission("mandatory.command.balance")) {
                plugin.sendMessage(player,"messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromUUID(player.getUniqueId());
            plugin.sendMessage(player, "messages.balance.own", "%balance%", Utilities.formattedMoney(mPlayer.getBalance()));
            return;
        }
        if (args.length == 1) {
            if (!player.hasPermission("mandatory.command.balance.others")) {
                plugin.sendMessage(player,"messages.error.permissionDenied");
                return;
            }
            MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[0]);
            if (mPlayer == null) {
                plugin.sendMessage(player, "messages.playerNotFound");
                return;
            }
            if (mPlayer.getUuid().equals(player.getUniqueId())) {
                plugin.sendMessage(player, "messages.balance.own", "%balance%", Utilities.formattedMoney(mPlayer.getBalance()));
                return;
            }
            plugin.sendMessage(player, "messages.balance.others", "%balance%", Utilities.formattedMoney(mPlayer.getBalance()), "%player%", mPlayer.getName());
            return;
        }
        if (args.length == 2) {
            if (!player.hasPermission("mandatory.command.balance.modify")) {
                plugin.sendMessage(player,"messages.error.permissionDenied");
                return;
            }
            plugin.sendMessage(player, "messages.balance.modify.syntax");
            return;
        }
        if (!player.hasPermission("mandatory.command.balance.modify")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromName(args[0]);
        if (mPlayer == null) {
            plugin.sendMessage(player, "messages.playerNotFound");
            return;
        }
        String reason = "";
        for (int i = 2; i < args.length; i++) {
            reason = reason + args[i] + " ";
        }
        if (args[1].equals("all")) {
            mPlayer.setBalance(0,"{ \"type\": \"confiscation\", \"reason\": \"" + reason + "\"}");
            plugin.sendMessage(player, "messages.balance.confiscate.all", "%player%", mPlayer.getName(), "%reason%", reason);
        }
        if (!args[1].substring(1).matches("[0-9]+")) {
            plugin.sendMessage(player, "messages.balance.modify.syntax");
            return;
        }
        if (args[1].startsWith("-")) {
            int amount = Integer.parseInt(args[1].substring(1));
            if (mPlayer.getBalance() == 0) {
                plugin.sendMessage(player, "messages.balance.confiscate.none");
                return;
            }
            if (amount > mPlayer.getBalance()) {
                plugin.sendMessage(player, "messages.balance.confiscate.notEnough");
                return;
            }
            mPlayer.removeBalance(amount, "{ \"type\": \"confiscation\", \"reason\": \"" + reason + "\"}");
            plugin.sendMessage(player, "messages.balance.confiscate.success", "%player%", mPlayer.getName(), "%amount%", Utilities.formattedMoney(amount), "%reason%", reason);


        }
        if (args[1].startsWith("+")) {
            int amount = Integer.parseInt(args[1].substring(1));
            mPlayer.addBalance(amount, "{ \"type\": \"reimbursement\", \"reason\": \"" + reason + "\"}");
            plugin.sendMessage(player, "messages.balance.reimburse", "%player%", mPlayer.getName(), "%amount%", Utilities.formattedMoney(amount), "%reason%", reason);
        }
    }
}
