package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@CommandAlias("broadcast|bc")
public class CommandBroadcast extends BaseCommand {

    @Default
    public static void onBroadcast(Player player, String message) {
        if (!player.hasPermission("mandatory.command.broadcast")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Component component = Mandatory.getMessageComponent("messages.broadcast", "%message%", message);
        assert component != null;
        Mandatory.getInstance().getServer().sendMessage(component);
    }
}
