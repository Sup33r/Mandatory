package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import live.supeer.apied.Apied;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("sign")
public class CommandSign extends BaseCommand {

    @Default
    @CommandPermission("mandatory.command.sign")
    public void onSign(Player player, @Optional Integer row) {
        if (row == null) {
            Mandatory.sendMessage(player, "messages.sign.syntax");
            return;
        }
        if (row < 1 || row > 4) {
            Mandatory.sendMessage(player, "messages.sign.invalidRow");
            return;
        }
        Apied.signEdit.put(player, row);
        Mandatory.sendMessage(player, "messages.sign.editing", "%row%", row + "");
    }

    @CommandAlias("cancel")
    @CommandPermission("mandatory.command.sign")
    public void onCancel(Player player) {
        Apied.signEdit.remove(player);
        Mandatory.sendMessage(player, "messages.sign.cancel");
    }
}
