package live.supeer.mandatory;

import io.papermc.paper.event.player.AsyncChatEvent;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.event.CityCreationEvent;
import live.supeer.metropolis.event.CityDeletionEvent;
import live.supeer.metropolis.event.CityJoinEvent;
import live.supeer.metropolis.event.CityLeaveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;
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
        MPlayer player = ApiedAPI.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) {
            player = ApiedAPI.createPlayer(event.getPlayer());
            Mandatory.firstTimePlayer.add(event.getPlayer());
        }
        if (player.hasFlag('s')) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Mandatory.getMessageComponent("messages.disabled.other")));
            MandatoryChat.modChatAnnouncement(Mandatory.getMessage("messages.modchat.messages.disabledOther", "%player%", event.getPlayer().getName()));
        }
        Mandatory.startTime.put(player.getUuid(), Database.createLoginLog(player.getUuid(), event.getRealAddress().getHostAddress()));
        ApiedAPI.addPlayer(player);
        ChannelManager.initiatePlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        MPlayer mPlayer = ApiedAPI.getPlayer(event.getPlayer());
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
        Component prefix = MandatoryChat.getTABPrefix(event.getPlayer());
        if (prefix != null) {
            event.getPlayer().playerListName(prefix);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer mPlayer = ApiedAPI.getPlayer(event.getPlayer());
        assert mPlayer != null;
        if (Mandatory.backLocations.containsKey(event.getPlayer())) {
            mPlayer.setBackLocation(Mandatory.backLocations.get(event.getPlayer()));
        }
        mPlayer.setLastLocation(event.getPlayer().getLocation());
        mPlayer.setLatestLeave(DateUtil.getTimestamp());
        Database.updateLoginLogPlaytime(mPlayer.getUuid(), Mandatory.startTime.get(mPlayer.getUuid()), DateUtil.getTimestamp() - Mandatory.startTime.get(mPlayer.getUuid()));
        ApiedAPI.removePlayer(mPlayer);
        if (!event.getPlayer().hasPermission("mandatory.hide.quit")) {
            if (Mandatory.kickedPlayers.contains(event.getPlayer())) {
                event.quitMessage(Mandatory.getMessageComponent("messages.chat.kicked", "%player%", event.getPlayer().getName()));
                Mandatory.kickedPlayers.remove(event.getPlayer());
            } else if (Mandatory.bannedPlayers.contains(event.getPlayer())) {
                event.quitMessage(Mandatory.getMessageComponent("messages.chat.banned", "%player%", event.getPlayer().getName()));
                Mandatory.bannedPlayers.remove(event.getPlayer());
            } else {
                event.quitMessage(Mandatory.getMessageComponent("messages.chat.quit", "%player%", event.getPlayer().getName()));
            }
        } else {
            event.quitMessage(Mandatory.getMessageComponent("messages.chat.hidden"));
        }
        Mandatory.firstTimePlayer.remove(event.getPlayer());
        Mandatory.promptedPayment.remove(event.getPlayer());
        Mandatory.afkPlayers.remove(event.getPlayer().getUniqueId());
        Mandatory.replyPlayers.remove(event.getPlayer());
        Mandatory.backLocations.remove(event.getPlayer());
        Mandatory.startTime.remove(mPlayer.getUuid());
        Mandatory.activeChannel.remove(mPlayer.getUuid());
        Mandatory.playerChannels.remove(mPlayer.getUuid());
    }

    @EventHandler
    public void onCityJoinEvent(CityJoinEvent event) {
        MPlayer player = ApiedAPI.getPlayer(event.getPlayer());
        assert player != null;
        Channel cityChannel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        if (cityChannel != null) {
            cityChannel.addMember(player.getUuid());
            ChannelManager.setActiveChannel(event.getPlayer(), cityChannel.getName().toLowerCase());
        }
    }

    @EventHandler
    public void onCityCreationEvent(CityCreationEvent event) {
        Channel cityChannel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        if (cityChannel == null) {
            ChannelManager.createChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()), ChannelType.CITY);
        }
    }

    @EventHandler
    public void onCityDeletionEvent(CityDeletionEvent event) {
        Channel cityChannel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        if (cityChannel != null) {
            ChannelManager.deleteChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        }
    }

    @EventHandler
    public void onCityLeaveEvent(CityLeaveEvent event) {
        MPlayer player = ApiedAPI.getPlayer(event.getPlayer());
        assert player != null;
        Channel cityChannel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        if (cityChannel != null) {
            cityChannel.removeMember(player.getUuid());
            ChannelManager.setActiveChannelToOther(event.getPlayer());
        }
    }

    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        event.setCancelled(true);
        if (MetropolisAPI.isPlayerInMeeting(event.getPlayer())) {
            MandatoryChat.sendMeetingMessage(event.getPlayer(), (TextComponent) event.message());
            return;
        }
        MandatoryChat.sendMessage(event.getPlayer(), (TextComponent) event.message());
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

}
