package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.tr7zw.changeme.nbtapi.NBTItem;
import live.supeer.mandatory.*;
import live.supeer.mandatory.utils.PlayerHeadUtil;
import live.supeer.mandatory.utils.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

@CommandAlias("buy")
public class CommandBuy extends BaseCommand {
    public static Mandatory plugin;

    @Default
    public void onBuy(Player player) {
        if (!player.hasPermission("mandatory.command.buy")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        plugin.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("broadcast")
    public void onBroadcast(Player player, String message) {
        if (!player.hasPermission("mandatory.command.buy.broadcast")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        if (mPlayer.getBalance() < Mandatory.configuration.getBroadcastPrice()) {
            plugin.sendMessage(player, "messages.buy.broadcast.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getBroadcastPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getBroadcastPrice(),"{ \"type\": \"buy\", \"subtype\": \"broadcast\", \"message\": \"" + message + "\"}");
        plugin.sendMessage(player, "messages.buy.broadcast.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getBroadcastPrice()));
        MandatoryChat.sendPublicBroadcast(player, message);
    }

    @Subcommand("go")
    public void onGo(Player player, String goName) {
        if (!player.hasPermission("mandatory.command.buy.go")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (goName.split("\\s+").length > 1) {
            plugin.sendMessage(player,"messages.buy.go.syntax");
            return;
        }
        if (GoManager.goExists(goName)) {
            plugin.sendMessage(player, "messages.buy.go.alreadyExists");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getGoPrice()) {
            plugin.sendMessage(player, "messages.buy.go.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getGoPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getGoPrice(),"{ \"type\": \"buy\", \"subtype\": \"go\", \"goName\": \"" + goName + "\"}");
        Go go = GoManager.createGo(goName, player);
        assert go != null;
        plugin.sendMessage(player, "messages.buy.go.success","%name%", go.getName(), "%price%", Utilities.formattedMoney(Mandatory.configuration.getGoPrice()));
    }

    @Subcommand("head")
    public void onHead(Player player) {
        if (!player.hasPermission("mandatory.command.buy.head")) {
            plugin.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (player.getInventory().firstEmpty() == -1) {
            plugin.sendMessage(player, "messages.buy.head.noSpace");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getHeadPrice()) {
            plugin.sendMessage(player, "messages.buy.head.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getHeadPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getHeadPrice(),"{ \"type\": \"buy\", \"subtype\": \"head\"}");
        plugin.sendMessage(player, "messages.buy.head.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getHeadPrice()));
        player.getInventory().addItem(PlayerHeadUtil.getPlayerHead(player));
    }

    @Subcommand("copyright")
    public void onCopyright(Player player) {
        if (!player.hasPermission("mandatory.command.buy.copyright")) {
            plugin.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getCopyrightPrice()) {
            plugin.sendMessage(player, "messages.buy.copyright.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getCopyrightPrice()));
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            plugin.sendMessage(player, "messages.buy.copyright.noItemInHand");
            return;
        }
        if (item.getType() != Material.FILLED_MAP || item.getType() != Material.WRITTEN_BOOK || item.getItemMeta().hasLore()) {
            plugin.sendMessage(player, "messages.buy.copyright.notCopyrightable");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§dKopieringsskyddad av " + player.getDisplayName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTItem nbti = new NBTItem(item);

        nbti.setString("copyrighter", player.getUniqueId().toString());
        item = nbti.getItem();
        nbti.getKeys();
        player.getInventory().getItemInMainHand().setAmount(0);
        player.getInventory().addItem(item);
        mPlayer.removeBalance(Mandatory.configuration.getCopyrightPrice(),"{ \"type\": \"buy\", \"subtype\": \"copyright\"}");
        plugin.sendMessage(player, "messages.buy.copyright.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getCopyrightPrice()));
    }
}
