package live.supeer.mandatory;

import live.supeer.mandatory.utils.DateUtil;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class MandatoryListerner implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (Mandatory.configuration.getDisabledNames().contains(event.getPlayer().getName())) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Mandatory.getMessageComponent("messages.disabled.name", "%name%", event.getPlayer().getName())));
            MandatoryChat.modChatAnnouncement(Mandatory.getMessage("messages.modchat.messages.disabledUsername", "%player%", event.getPlayer().getName()));
            return;
        }
        MPlayer player = MPlayerManager.getPlayerFromUUID(event.getPlayer().getUniqueId());
        if (player == null) {
            player = MPlayerManager.createPlayer(event.getPlayer());
            Mandatory.firstTimePlayer.add(event.getPlayer());
        }
        if (player.hasFlag('s')) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Mandatory.getMessageComponent("messages.disabled.other")));
            MandatoryChat.modChatAnnouncement(Mandatory.getMessage("messages.modchat.messages.disabledOther", "%player%", event.getPlayer().getName()));
        }
        Mandatory.startTime.put(player.getUuid(), MPlayerManager.createLoginLog(player.getUuid(), event.getRealAddress().getHostAddress()));
        MPlayerManager.addPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(event.getPlayer());
        assert mPlayer != null;
        mPlayer.setLatestJoin(DateUtil.getTimestamp());
        if (mPlayer.getBackLocation() != null) {
            Mandatory.backLocations.put(event.getPlayer(), mPlayer.getBackLocation());
        }
        if (!mPlayer.hasFlag('g')) {
            event.getPlayer().sendTitlePart(TitlePart.TITLE, Objects.requireNonNull(Mandatory.getMessageComponent("messages.chat.greeting.title", "%player%", event.getPlayer().getName())));
            event.getPlayer().sendTitlePart(TitlePart.SUBTITLE, Objects.requireNonNull(Mandatory.getMessageComponent("messages.chat.greeting.subtitle")));
        }
        if (!event.getPlayer().hasPermission("mandatory.hide.join")) {
            if (Mandatory.firstTimePlayer.contains(event.getPlayer())) {
                event.joinMessage(Mandatory.getMessageComponent("messages.chat.joinFirstTime", "%player%", event.getPlayer().getName()));
                Mandatory.firstTimePlayer.remove(event.getPlayer());
            } else if (!Objects.equals(mPlayer.getName(), event.getPlayer().getName())) {
                event.joinMessage(Mandatory.getMessageComponent("messages.chat.joinNameChange", "%name%", event.getPlayer().getName(), "%oldname%", mPlayer.getName()));
                mPlayer.changeName(event.getPlayer().getName());
            } else {
                event.joinMessage(Mandatory.getMessageComponent("messages.chat.join", "%player%", event.getPlayer().getName()));
            }
        } else {
            event.joinMessage(Mandatory.getMessageComponent("messages.chat.hidden"));
            // TODO: Logga till console att saker händer!
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(event.getPlayer());
        assert mPlayer != null;
        if (Mandatory.backLocations.containsKey(event.getPlayer())) {
            mPlayer.setBackLocation(Mandatory.backLocations.get(event.getPlayer()));
        }
        mPlayer.setLastLocation(event.getPlayer().getLocation());
        mPlayer.setLatestLeave(DateUtil.getTimestamp());
        MPlayerManager.updateLoginLogPlaytime(mPlayer.getUuid(), Mandatory.startTime.get(mPlayer.getUuid()), DateUtil.getTimestamp() - Mandatory.startTime.get(mPlayer.getUuid()));
        MPlayerManager.removePlayer(mPlayer);
        if (!event.getPlayer().hasPermission("mandatory.hide.quit")) {
            if (Mandatory.kickedPlayers.contains(event.getPlayer())) {
                event.quitMessage(Mandatory.getMessageComponent("messages.chat.kicked", "%player%", event.getPlayer().getName()));
                Mandatory.kickedPlayers.remove(event.getPlayer());
            } else {
                event.quitMessage(Mandatory.getMessageComponent("messages.chat.quit", "%player%", event.getPlayer().getName()));
            }
        } else {
            event.quitMessage(Mandatory.getMessageComponent("messages.chat.hidden"));
        }
        Mandatory.firstTimePlayer.remove(event.getPlayer());
        Mandatory.promptedPayment.remove(event.getPlayer());
        Mandatory.afkPlayers.remove(event.getPlayer());
        Mandatory.replyPlayers.remove(event.getPlayer());
        Mandatory.backLocations.remove(event.getPlayer());
        Mandatory.startTime.remove(mPlayer.getUuid());
    }

    @EventHandler
    public void onCrafterEvent(CrafterCraftEvent event) {
        ItemStack itemStack = event.getResult();
        if (CopyrightManager.hasCopyright(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() != InventoryType.SlotType.RESULT) {
            return;
        }
        event.getWhoClicked().sendMessage(event.getAction().name());
        event.getWhoClicked().sendMessage(event.getClick().name());

        if (event.getAction() != InventoryAction.PICKUP_ALL &&
                event.getAction() != InventoryAction.PICKUP_HALF &&
                event.getAction() != InventoryAction.PICKUP_ONE &&
                event.getAction() != InventoryAction.PICKUP_SOME &&
                event.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
                event.getAction() != InventoryAction.DROP_ONE_SLOT &&
                event.getAction() != InventoryAction.DROP_ALL_SLOT &&
                event.getAction() != InventoryAction.HOTBAR_SWAP) {
            return;
        }

        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) {
            return;
        }

        if (!CopyrightManager.hasCopyright(itemStack)) {
            return;
        }

        UUID copyrighterUUID = CopyrightManager.getCopyrighter(itemStack);
        UUID playerUUID = event.getWhoClicked().getUniqueId();
        if (copyrighterUUID.equals(playerUUID)) {
            return;
        }

        event.setCancelled(true);
        Mandatory.sendMessage(event.getWhoClicked(), "messages.copyrighted");
    }

    @EventHandler
    public void onInventoryAction(InventoryInteractEvent event) {

    }

}
