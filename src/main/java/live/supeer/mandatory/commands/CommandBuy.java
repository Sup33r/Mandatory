package live.supeer.mandatory.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import live.supeer.mandatory.*;
import live.supeer.mandatory.utils.PlayerHeadUtil;
import live.supeer.mandatory.utils.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

@CommandAlias("buy")
public class CommandBuy extends BaseCommand {

    @Default
    public void onBuy(Player player) {
        if (!player.hasPermission("mandatory.command.buy")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        Mandatory.sendMessage(player, "messages.buy.syntax");
    }

    @Subcommand("broadcast")
    public void onBroadcast(Player player, String message) {
        if (!player.hasPermission("mandatory.command.buy.broadcast")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getBroadcastPrice()) {
            Mandatory.sendMessage(player, "messages.buy.broadcast.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getBroadcastPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getBroadcastPrice(),"{ \"type\": \"buy\", \"subtype\": \"broadcast\", \"message\": \"" + message + "\"}");
        Mandatory.sendMessage(player, "messages.buy.broadcast.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getBroadcastPrice()));
        MandatoryChat.sendPublicBroadcast(player, message);
    }

    @Subcommand("go")
    public void onGo(Player player, String goName) {
        if (!player.hasPermission("mandatory.command.buy.go")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        if (goName.split("\\s+").length > 1) {
            Mandatory.sendMessage(player,"messages.buy.go.syntax");
            return;
        }
        if (GoManager.goExists(goName)) {
            Mandatory.sendMessage(player, "messages.buy.go.alreadyExists");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getGoPrice()) {
            Mandatory.sendMessage(player, "messages.buy.go.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getGoPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getGoPrice(),"{ \"type\": \"buy\", \"subtype\": \"go\", \"goName\": \"" + goName + "\"}");
        Go go = GoManager.createGo(goName, player);
        assert go != null;
        Mandatory.sendMessage(player, "messages.buy.go.success","%name%", go.getName(), "%price%", Utilities.formattedMoney(Mandatory.configuration.getGoPrice()));
    }

    @Subcommand("head")
    public void onHead(Player player) {
        if (!player.hasPermission("mandatory.command.buy.head")) {
            Mandatory.sendMessage(player, "messages.error.permissionDenied");
            return;
        }
        if (player.getInventory().firstEmpty() == -1) {
            Mandatory.sendMessage(player, "messages.buy.head.noSpace");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getHeadPrice()) {
            Mandatory.sendMessage(player, "messages.buy.head.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getHeadPrice()));
            return;
        }
        mPlayer.removeBalance(Mandatory.configuration.getHeadPrice(),"{ \"type\": \"buy\", \"subtype\": \"head\"}");
        Mandatory.sendMessage(player, "messages.buy.head.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getHeadPrice()));
        player.getInventory().addItem(PlayerHeadUtil.getPlayerHead(player));
    }

    @Subcommand("copyright")
    public void onCopyright(Player player) {
        if (!player.hasPermission("mandatory.command.buy.copyright")) {
            Mandatory.sendMessage(player,"messages.error.permissionDenied");
            return;
        }
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(player);
        assert mPlayer != null;
        if (mPlayer.getBalance() < Mandatory.configuration.getCopyrightPrice()) {
            Mandatory.sendMessage(player, "messages.buy.copyright.notEnoughMoney", "%price%", Utilities.formattedMoney(Mandatory.configuration.getCopyrightPrice()));
            return;
        }
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            Mandatory.sendMessage(player, "messages.buy.copyright.noItemInHand");
            return;
        }
        if (item.getType() != Material.FILLED_MAP || item.getType() != Material.WRITTEN_BOOK || item.getItemMeta().hasLore()) {
            Mandatory.sendMessage(player, "messages.buy.copyright.notCopyrightable");
            return;
        }

        ItemMeta meta = item.getItemMeta();
        TextComponent lore = Component.text("Kopieringsskyddad av: " + player.getName()).color(TextColor.fromCSSHexString("#ab8aff"));
        Objects.requireNonNull(meta.lore()).add(lore);
        item.setItemMeta(meta);
        ReadWriteNBT nbti = NBT.itemStackToNBT(item);
        nbti.setString("copyrighter", player.getUniqueId().toString());
        item = nbti.getItemStack(nbti.getString("tag"));
        nbti.getKeys();
        player.getInventory().getItemInMainHand().setAmount(0);
        assert item != null;
        player.getInventory().addItem(item);
        mPlayer.removeBalance(Mandatory.configuration.getCopyrightPrice(),"{ \"type\": \"buy\", \"subtype\": \"copyright\"}");
        Mandatory.sendMessage(player, "messages.buy.copyright.success", "%price%", Utilities.formattedMoney(Mandatory.configuration.getCopyrightPrice()));
    }
}
