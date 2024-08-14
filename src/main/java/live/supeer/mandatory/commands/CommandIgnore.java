package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import live.supeer.mandatory.MPlayer;
import live.supeer.mandatory.MPlayerManager;
import live.supeer.mandatory.Mandatory;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("ignore")
public class CommandIgnore extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onIgnore(Player player,@Optional OnlinePlayer target) {
        if (!player.hasPermission("mandatory.command.ignore")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (target == null) {
            List<String> ignoringNames = MPlayerManager.getPlayerNamesFromUUIDs(mPlayer.getIgnores());
            List<String> ignoredNames = MPlayerManager.getIgnoredByPlayers(player.getUniqueId());
            if (ignoringNames.isEmpty()) {
                plugin.sendMessage(player, "messages.ignore.noIgnoring");
            } else {
                plugin.sendMessage(player, "messages.ignore.ignoring","%count%", String.valueOf(ignoringNames.size()), "%list%",Utilities.formatStringList(ignoringNames));
            }
            if (ignoredNames.isEmpty()) {
                plugin.sendMessage(player, "messages.ignore.noIgnores");
            } else {
                plugin.sendMessage(player, "messages.ignore.ignoredBy","%count%", String.valueOf(ignoredNames.size()), "%list%",Utilities.formatStringList(ignoredNames));
            }
            return;
        }
        if (target.getPlayer().hasPermission("mandatory.command.ignore.impossible")) {
            plugin.sendMessage(player, "messages.ignore.impossible", "%player%", target.getPlayer().getName());
            return;
        }
        if (mPlayer.getIgnores().contains(target.getPlayer().getUniqueId())) {
            mPlayer.removeIgnore(target.getPlayer().getUniqueId());
            plugin.sendMessage(player, "messages.ignore.stoppedIgnoring", "%player%", target.getPlayer().getName());
            return;
        }
        mPlayer.addIgnore(target.getPlayer().getUniqueId());
        plugin.sendMessage(player, "messages.ignore.ignored", "%player%", target.getPlayer().getName());
    }
}
