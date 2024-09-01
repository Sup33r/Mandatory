package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import live.supeer.apied.ShopManager;
import live.supeer.mandatory.Mandatory;
import org.bukkit.entity.Player;

@CommandAlias("shop")
public class CommandShop extends BaseCommand {

    @Default
    public void onShop(Player player) {
        Mandatory.sendMessage(player, "messages.shop.syntax");
    }

    @Subcommand("cancel")
    public void onCancel(Player player) {
        if (!player.hasPermission("mandatory.command.shop.cancel")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (ShopManager.isShopEventCounting(player.getUniqueId())) {
            ShopManager.stopShopEventCount(player.getUniqueId());
            Mandatory.sendMessage(player, "messages.shop.cancel");
        } else {
            Mandatory.sendMessage(player, "messages.shop.noEvent");
        }
    }

    @Subcommand("set")
    @CommandCompletion("@shopTypes @nothing användningar antal")
    public void onSet(Player player, String type, int uses, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.shop.set")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (count == null) {
            count = 1;
        }
        if (uses < 0 || count < 0) {
            Mandatory.sendMessage(player, "messages.shop.error.invalidAmount");
            return;
        }
        if (uses > 100000 || count > 100000) {
            Mandatory.sendMessage(player, "messages.shop.error.tooHigh");
            return;
        }
        if (type.equalsIgnoreCase("maxuses")) {
            ShopManager.startShopEventCount(player, "maxUses", uses, count);
            Mandatory.sendMessage(player, "messages.shop.set.maxuses");
        } else if (type.equalsIgnoreCase("maxdailyuses")) {
            ShopManager.startShopEventCount(player, "maxDailyUses", uses, count);
            Mandatory.sendMessage(player, "messages.shop.set.maxdailyuses");
        } else if (type.equalsIgnoreCase("maxDailyPlayerUses")) {
            ShopManager.startShopEventCount(player, "maxdailyplayeruses", uses, count);
            Mandatory.sendMessage(player, "messages.shop.set.maxdailyplayeruses");
        } else if (type.equalsIgnoreCase("maxPlayerUses")) {
            ShopManager.startShopEventCount(player, "maxPlayerUses", uses, count);
            Mandatory.sendMessage(player, "messages.shop.set.maxplayeruses");
        }
    }

    @Subcommand("toggle")
    @CommandCompletion("@shopToggles antal")
    public void onToggle(Player player, String toggle, @Optional Integer count) {
        if (!player.hasPermission("mandatory.command.shop.toggle")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (count == null) {
            count = 1;
        }
        if (count < 0) {
            Mandatory.sendMessage(player, "messages.shop.error.invalidAmount");
            return;
        }
        if (count > 100000) {
            Mandatory.sendMessage(player, "messages.shop.error.tooHigh");
            return;
        }
        if (toggle.equalsIgnoreCase("admin")) {
            ShopManager.startShopEventCount(player, "admin", 0, count);
            Mandatory.sendMessage(player, "messages.shop.toggle.admin");
        }
    }
}
