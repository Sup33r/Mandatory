package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import live.supeer.apied.ShopManager;
import org.bukkit.entity.Player;

@CommandAlias("online")
public class CommandOnline extends BaseCommand {

    @Default
    public void onOnline(Player player) {
    }
}
