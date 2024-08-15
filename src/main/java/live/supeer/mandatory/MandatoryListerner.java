package live.supeer.mandatory;

import live.supeer.mandatory.utils.DateUtil;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;

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
        }
        if (player.hasFlag('s')) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Objects.requireNonNull(Mandatory.getMessageComponent("messages.disabled.other")));
            MandatoryChat.modChatAnnouncement(Mandatory.getMessage("messages.modchat.messages.disabledOther", "%player%", event.getPlayer().getName()));
        }
        MPlayerManager.addPlayer(player);
        Mandatory.firstTimePlayer.add(event.getPlayer());
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
            event.getPlayer().sendTitlePart(TitlePart.TITLE, Objects.requireNonNull(Mandatory.getMessageComponent("messages.chat.greeting.title")));
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
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer mPlayer = MPlayerManager.getPlayerFromPlayer(event.getPlayer());
        assert mPlayer != null;
        mPlayer.setBackLocation(Mandatory.backLocations.get(event.getPlayer()));
        mPlayer.setLastLocation(event.getPlayer().getLocation());
        mPlayer.setLatestLeave(DateUtil.getTimestamp());
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
    }

}
