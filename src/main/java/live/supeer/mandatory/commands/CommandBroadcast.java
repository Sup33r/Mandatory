package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.mandatory.Mandatory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

@CommandAlias("broadcast|bc")
public class CommandBroadcast extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onBroadcast(Player player, String message) {
        if (!player.hasPermission("mandatory.command.broadcast")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        Component component = plugin.getMessageComponent("messages.broadcast", "%message%", message);
        assert component != null;
        plugin.getServer().sendMessage(component);
    }
}
