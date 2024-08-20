package live.supeer.mandatory;

import io.papermc.paper.event.player.AsyncChatEvent;
import live.supeer.apied.ApiedAPI;
import live.supeer.apied.ChestManager;
import live.supeer.apied.ChestType;
import live.supeer.apied.MPlayer;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.mandatory.utils.LocationUtil;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.city.Role;
import live.supeer.metropolis.event.CityCreationEvent;
import live.supeer.metropolis.event.CityDeletionEvent;
import live.supeer.metropolis.event.CityJoinEvent;
import live.supeer.metropolis.event.CityLeaveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Material;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
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
        if (!mPlayer.isBanned()) {
            mPlayer.setLastLocation(event.getPlayer().getLocation());
        }
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
        MPlayer player = ApiedAPI.getPlayer(event.getPlayer());
        assert player != null;
        if (player.isBanned()) {
            Mandatory.sendMessage(event.getPlayer(), "messages.ban.chatNotAllowed");
            return;
        }
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
        Player player = event.getWhoClicked() instanceof Player ? (Player) event.getWhoClicked() : null;
        if (player != null && Mandatory.mapOverride.contains(player)) {
            return;
        }

        event.setCancelled(true);
        Mandatory.sendMessage(event.getWhoClicked(), "messages.copyrighted");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        MPlayer player = ApiedAPI.getPlayer(event.getPlayer());
        assert player != null;
        if (player.isBanned()) {
            event.setCancelled(true);
            Mandatory.sendMessage(event.getPlayer(), "messages.ban.commandNotAllowed");
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST) || block.getType().equals(Material.BARREL) || block.getType().equals(Material.SHULKER_BOX)) {
            live.supeer.apied.Chest chest1;
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                return;
            }
            if (block.getState() instanceof Chest chest) {
                if (chest.getInventory() instanceof DoubleChestInventory doubleChestInventory) {
                    chest1 = ChestManager.getClaimedChest(doubleChestInventory.getLeftSide().getLocation());
                    if (doubleChestInventory.getLeftSide().getLocation().equals(block.getLocation())) {
                        if (chest1 != null) {
                            if (chest1.getOwnerUUID().equals(player.getUniqueId())) {
                                chest1.setLocation(doubleChestInventory.getRightSide().getLocation());
                                chest1.setType(ChestType.SINGLE);
                                if (!mPlayer.hasFlag('m')) {
                                    Mandatory.sendMessage(player, "messages.chest.shrink.self");
                                }
                                return;
                            } else {
                                if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                                    event.setCancelled(true);
                                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                    return;
                                } else if (ChestManager.chestOverride.contains(player)) {
                                    chest1.setLocation(doubleChestInventory.getRightSide().getLocation());
                                    chest1.setType(ChestType.SINGLE);
                                    Mandatory.sendMessage(player, "messages.chest.shrink.override", "%owner%", ApiedAPI.getPlayer(chest1.getOwnerUUID()).getName());
                                    return;
                                } else {
                                    event.setCancelled(true);
                                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                    return;
                                }
                            }
                        }
                    } else {
                        if (chest1 != null) {
                            if (chest1.getOwnerUUID().equals(player.getUniqueId())) {
                                if (!mPlayer.hasFlag('m')) {
                                    Mandatory.sendMessage(player, "messages.chest.shrink.self");
                                    return;
                                }
                            } else {
                                if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                                    event.setCancelled(true);
                                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                    return;
                                } else if (ChestManager.chestOverride.contains(player)) {
                                    Mandatory.sendMessage(player, "messages.chest.shrink.override", "%owner%", ApiedAPI.getPlayer(chest1.getOwnerUUID()).getName());
                                    return;
                                } else {
                                    event.setCancelled(true);
                                    Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                    return;
                                }
                            }
                        }
                    }
                } else {
                    chest1 = ChestManager.getClaimedChest(block.getLocation());
                }
            } else {
                chest1 = ChestManager.getClaimedChest(block.getLocation());
            }
            if (chest1 != null) {
                if (chest1.getOwnerUUID().equals(player.getUniqueId())) {
                    ChestManager.removeChest(chest1.getLocation());
                    if (!mPlayer.hasFlag('m')) {
                        Mandatory.sendMessage(player, "messages.chest.remove.self");
                    }
                } else {
                    if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                        event.setCancelled(true);
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    } else if (ChestManager.chestOverride.contains(player)) {
                        ChestManager.removeChest(chest1.getLocation());
                        Mandatory.sendMessage(player, "messages.chest.remove.override", "%owner%", ApiedAPI.getPlayer(chest1.getOwnerUUID()).getName());
                    } else {
                        event.setCancelled(true);
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST) || block.getType().equals(Material.BARREL) || block.getType().equals(Material.SHULKER_BOX)) {
            String type = "";
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                return;
            }
            if (block.getState() instanceof Chest chest) {
                if (chest.getInventory() instanceof DoubleChestInventory doubleChestInventory) {
                    live.supeer.apied.Chest chest1 = ChestManager.getClaimedChest(doubleChestInventory.getLeftSide().getLocation());
                    if (chest1 != null) {
                        if (chest1.getOwnerUUID().equals(player.getUniqueId())) {
                            chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
                            chest1.setType(ChestType.DOUBLE);
                            if (!mPlayer.hasFlag('m')) {
                                Mandatory.sendMessage(player, "messages.chest.expand.self");
                            }
                        } else {
                            if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                                event.setCancelled(true);
                                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            } else if (ChestManager.chestOverride.contains(player)) {
                                chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
                                chest1.setType(ChestType.DOUBLE);
                                Mandatory.sendMessage(player, "messages.chest.expand.override", "%owner%", ApiedAPI.getPlayer(chest1.getOwnerUUID()).getName());
                            } else {
                                event.setCancelled(true);
                                Mandatory.sendMessage(player, "messages.error.permissionDenied");
                            }
                        }
                    }
                    return;
                }
                type = ChestType.SINGLE.getType();
            }
            if (block.getState() instanceof Barrel) {
                type = ChestType.BARREL.getType();
            }
            if (block.getState() instanceof ShulkerBox) {
                type = ChestType.SHULKER.getType();
            }
            ChestManager.newChest(block.getLocation(), player, type);
            if (!mPlayer.hasFlag('m')) {
                Mandatory.sendMessage(player, "messages.chest.create");
            }
        }
    }
}
