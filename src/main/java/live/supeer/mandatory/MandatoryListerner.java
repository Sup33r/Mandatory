package live.supeer.mandatory;

import io.papermc.paper.event.player.AsyncChatEvent;
import live.supeer.apied.*;
import live.supeer.mandatory.utils.DateUtil;
import live.supeer.mandatory.utils.Utilities;
import live.supeer.metropolis.Metropolis;
import live.supeer.metropolis.MetropolisAPI;
import live.supeer.metropolis.event.CityCreationEvent;
import live.supeer.metropolis.event.CityDeletionEvent;
import live.supeer.metropolis.event.CityJoinEvent;
import live.supeer.metropolis.event.CityLeaveEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.CrafterCraftEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MandatoryListerner implements Listener {

    private final List<String> joinMessages = new ArrayList<>();
    private final List<String> quitMessages = new ArrayList<>();

    public MandatoryListerner() {
        startMessageBroadcastTask();
    }

    private void startMessageBroadcastTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                broadcastGroupedMessages();
            }
        }.runTaskTimer(Mandatory.getInstance(), 0, 100); // Adjust the delay and period as needed
    }

    private void broadcastGroupedMessages() {
        if (!joinMessages.isEmpty()) {
            String joinMessage;
            if (joinMessages.size() > 30) {
                joinMessage = Mandatory.getMessage("messages.chat.groupedJoinMany", "%count%", Integer.toString(joinMessages.size()));
            } else {
                joinMessage = Mandatory.getMessage("messages.chat.groupedJoin", "%players%", String.join(", ", joinMessages));
            }
            MandatoryChat.sendJoinLeaveMessage(joinMessage);
            joinMessages.clear();
        }
        if (!quitMessages.isEmpty()) {
            String quitMessage;
            if (quitMessages.size() > 30) {
                quitMessage = Mandatory.getMessage("messages.chat.groupedQuitMany", "%count%", Integer.toString(quitMessages.size()));
            } else {
                quitMessage = Mandatory.getMessage("messages.chat.groupedQuit", "%players%", String.join(", ", quitMessages));
            }
            MandatoryChat.sendJoinLeaveMessage(quitMessage);
            quitMessages.clear();
        }
    }

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
            Metropolis.backLocations.put(event.getPlayer(), mPlayer.getBackLocation());
        }
        if (!mPlayer.hasFlag('g')) {
            event.getPlayer().sendTitlePart(TitlePart.TITLE, Objects.requireNonNull(Mandatory.getMessageComponent("messages.chat.greeting.title", "%player%", event.getPlayer().getName())));
            event.getPlayer().sendTitlePart(TitlePart.SUBTITLE, Objects.requireNonNull(Mandatory.getMessageComponent("messages.chat.greeting.subtitle")));
        }
        if (!event.getPlayer().hasPermission("mandatory.hide.join")) {
            if (Mandatory.firstTimePlayer.contains(event.getPlayer())) {
                event.joinMessage(null);
                MandatoryChat.sendJoinLeaveMessage(Mandatory.getMessage("messages.chat.joinFirstTime", "%player%", event.getPlayer().getName()));
                Location firstSpawn = Mandatory.configuration.getFirstSpawnLocation();
                if (firstSpawn != null) {
                    event.getPlayer().teleport(firstSpawn);
                }
                Mandatory.firstTimePlayer.remove(event.getPlayer());
            } else if (!Objects.equals(mPlayer.getName(), event.getPlayer().getName())) {
                event.joinMessage(null);
                MandatoryChat.sendJoinLeaveMessage(Mandatory.getMessage("messages.chat.joinNameChange", "%name%", event.getPlayer().getName(), "%oldname%", mPlayer.getName()));
                mPlayer.changeName(event.getPlayer().getName());
            } else {
                joinMessages.add(event.getPlayer().getName());
                event.joinMessage(null);
            }
        } else {
            event.joinMessage(null);
            // TODO: Logga till console att saker händer!
        }
        Component prefix = MandatoryChat.getTABPrefix(event.getPlayer());
        if (prefix != null) {
            event.getPlayer().playerListName(prefix);
        }
        final Component header = Mandatory.getMessageComponent("messages.chat.tablist.header");
        final Component footer = Mandatory.getMessageComponent("messages.chat.tablist.footer");
        assert header != null;
        assert footer != null;
        event.getPlayer().sendPlayerListHeaderAndFooter(header, footer);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        MPlayer mPlayer = ApiedAPI.getPlayer(event.getPlayer());
        assert mPlayer != null;
        if (Metropolis.backLocations.containsKey(event.getPlayer())) {
            mPlayer.setBackLocation(Metropolis.backLocations.get(event.getPlayer()));
        }
        if (!mPlayer.isBanned()) {
            mPlayer.setLastLocation(event.getPlayer().getLocation());
        }
        mPlayer.setLatestLeave(DateUtil.getTimestamp());
        Database.updateLoginLogPlaytime(mPlayer.getUuid(), Mandatory.startTime.get(mPlayer.getUuid()), DateUtil.getTimestamp() - Mandatory.startTime.get(mPlayer.getUuid()));
        ApiedAPI.removePlayer(mPlayer);
        if (!event.getPlayer().hasPermission("mandatory.hide.quit")) {
            if (Mandatory.kickedPlayers.contains(event.getPlayer())) {
                event.quitMessage(null);
                MandatoryChat.sendJoinLeaveMessage(Mandatory.getMessage("messages.chat.kicked", "%player%", event.getPlayer().getName()));
                Mandatory.kickedPlayers.remove(event.getPlayer());
            } else if (Mandatory.bannedPlayers.contains(event.getPlayer())) {
                event.quitMessage(null);
                MandatoryChat.sendJoinLeaveMessage(Mandatory.getMessage("messages.chat.banned", "%player%", event.getPlayer().getName()));
                Mandatory.bannedPlayers.remove(event.getPlayer());
            } else {
                quitMessages.add(event.getPlayer().getName());
                event.quitMessage(null);
            }
        } else {
            event.quitMessage(null);
        }
        Mandatory.firstTimePlayer.remove(event.getPlayer());
        Mandatory.promptedPayment.remove(event.getPlayer());
        Metropolis.afkPlayers.remove(event.getPlayer().getUniqueId());
        Mandatory.replyPlayers.remove(event.getPlayer());
        Metropolis.backLocations.remove(event.getPlayer());
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
        Channel cityChannel = ChannelManager.getChannel(ChannelManager.parseCityChannelName(event.getCity().getCityName()));
        if (cityChannel != null) {
            cityChannel.removeMember(event.getMPlayer().getUuid());
            Player player = Bukkit.getPlayer(event.getMPlayer().getUuid());
            assert player != null;
            ChannelManager.setActiveChannelToOther(player);
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
        if (block.getState() instanceof Sign) {
            ShopManager.handleSignRemoval(block.getLocation());
            return;
        }
        if (Utilities.isBlockContainer(block.getType())) {
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
                                ShopManager.handleChestLocationChange(chest1.getLocation(), doubleChestInventory.getRightSide().getLocation());
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
                                    ShopManager.handleChestLocationChange(chest1.getLocation(), doubleChestInventory.getRightSide().getLocation());
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
                                ShopManager.handleChestLocationChange(chest1.getLocation(), doubleChestInventory.getLeftSide().getLocation());
                                chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
                                chest1.setType(ChestType.SINGLE);
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
                                    ShopManager.handleChestLocationChange(chest1.getLocation(), doubleChestInventory.getLeftSide().getLocation());
                                    chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
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
                    ShopManager.handleChestRemoval(chest1.getLocation());
                    if (!mPlayer.hasFlag('m')) {
                        Mandatory.sendMessage(player, "messages.chest.remove.self");
                    }
                } else {
                    if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                        event.setCancelled(true);
                        Mandatory.sendMessage(player, "messages.error.permissionDenied");
                    } else if (ChestManager.chestOverride.contains(player)) {
                        ChestManager.removeChest(chest1.getLocation());
                        ShopManager.handleChestRemoval(chest1.getLocation());
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
        if (Utilities.isBlockContainer(block.getType())) {
            MPlayer mPlayer = ApiedAPI.getPlayer(player);
            if (mPlayer == null) {
                return;
            }
            if (block.getState() instanceof org.bukkit.block.Chest) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (block.getState() instanceof Chest chest) {
                            if (chest.getInventory() instanceof DoubleChestInventory doubleChestInventory) {
                                live.supeer.apied.Chest chest1;
                                if (event.getBlock().getLocation().equals(doubleChestInventory.getLeftSide().getLocation())) {
                                    chest1 = ChestManager.getClaimedChest(doubleChestInventory.getRightSide().getLocation());
                                } else {
                                    chest1 = ChestManager.getClaimedChest(doubleChestInventory.getLeftSide().getLocation());
                                }
                                if (chest1 != null) {
                                    if (chest1.getOwnerUUID().equals(player.getUniqueId())) {
                                        ShopManager.handleChestLocationChange(chest1.getLocation(), block.getLocation());
                                        chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
                                        chest1.setType(ChestType.DOUBLE);
                                        if (!mPlayer.hasFlag('m')) {
                                            Mandatory.sendMessage(player, "messages.chest.expand.self");
                                        }
                                        return;
                                    } else {
                                        if (chest1.getSharedPlayers().contains(player.getUniqueId())) {
                                            event.setCancelled(true);
                                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                        } else if (ChestManager.chestOverride.contains(player)) {
                                            ShopManager.handleChestLocationChange(chest1.getLocation(), block.getLocation());
                                            chest1.setLocation(doubleChestInventory.getLeftSide().getLocation());
                                            chest1.setType(ChestType.DOUBLE);
                                            Mandatory.sendMessage(player, "messages.chest.expand.override", "%owner%", ApiedAPI.getPlayer(chest1.getOwnerUUID()).getName());
                                            return;
                                        } else {
                                            event.setCancelled(true);
                                            Mandatory.sendMessage(player, "messages.error.permissionDenied");
                                        }
                                    }
                                }
                            }
                        }
                        // If not a double chest, proceed to create a new chest entry
                        ChestManager.newChest(block.getLocation(), player, ChestType.SINGLE.getType());
                        if (!mPlayer.hasFlag('m')) {
                            Mandatory.sendMessage(player, "messages.chest.create");
                        }
                    }
                }.runTaskLater(Mandatory.getInstance(), 1L); // Delay of 1 tick
            } else {
                String type = "";
                if (block.getState() instanceof Barrel) {
                    type = ChestType.BARREL.getType();
                } else if (block.getState() instanceof ShulkerBox) {
                    type = ChestType.SHULKER.getType();
                }
                ChestManager.newChest(block.getLocation(), player, type);
                if (!mPlayer.hasFlag('m')) {
                    Mandatory.sendMessage(player, "messages.chest.create");
                }
            }
        }
    }



    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        EntityDamageEvent.DamageCause cause = event.getEntity().getLastDamageCause().getCause();
        if (cause.equals(EntityDamageEvent.DamageCause.VOID)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.void", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.CAMPFIRE)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.fire", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.explosion", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FALL)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.fall", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FIRE)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.fire", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FIRE_TICK)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.fire", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.LAVA)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.lava", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.DROWNING)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.drowning", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.LIGHTNING)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.lightning", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.MAGIC)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.magic", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.POISON)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.poison", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.WITHER)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.common", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.suffocation", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.STARVATION)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.starvation", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.CONTACT)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.contact", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.CRAMMING)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.cramming", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.CUSTOM)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.common", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.DRAGON_BREATH)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.dragon_breath", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.DRYOUT)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.dryout", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.falling_block", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.fly_into_wall", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.FREEZE)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.freeze", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.HOT_FLOOR)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.hot_floor", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.MELTING)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.melting", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.SONIC_BOOM)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.sonic_boom", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.SUICIDE)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.common", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.THORNS)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.thorns", "%player%", event.getPlayer().getName()));
        } else if (cause.equals(EntityDamageEvent.DamageCause.WORLD_BORDER)) {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.world_border", "%player%", event.getPlayer().getName()));
        }

        if (event.getEntity().getKiller() != null) {
            if (event.getEntity().getKiller().getInventory().getItemInMainHand().clone().isEmpty()) {
                event.deathMessage(Mandatory.getMessageComponent("messages.death.kill", "%player%", event.getPlayer().getName(), "%killer%", event.getEntity().getName()));
            } else {
                ItemStack itemStack = event.getEntity().getKiller().getInventory().getItemInMainHand().clone();
                String weaponName = "";
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                    TextComponent displayName = (TextComponent) itemStack.getItemMeta().displayName();
                    assert displayName != null;
                    weaponName = displayName.content();
                } else {
                    weaponName = itemStack.getType().isBlock()
                            ? "<lang:block.minecraft." + itemStack.getType().getKey().getKey() + ">"
                            : "<lang:item.minecraft." + itemStack.getType().getKey().getKey() + ">";
                }
                if (itemStack.getType() != Material.WRITTEN_BOOK || itemStack.getType() != Material.WRITABLE_BOOK) {
                    event.deathMessage(Mandatory.getMessageComponent("messages.death.killWeapon", "%player%", event.getPlayer().getName(), "%killer%", event.getEntity().getName(), "%weaponName%", weaponName, "%weapon%", Utilities.getHoverInfo(itemStack)));
                } else {
                event.deathMessage(Mandatory.getMessageComponent("messages.death.kill", "%player%", event.getPlayer().getName(), "%killer%", event.getEntity().getName()));
                }
            }
        } else {
            event.deathMessage(Mandatory.getMessageComponent("messages.death.common", "%player%", event.getPlayer().getName()));
        }
        Database.handleDeath(event.getPlayer(), event.getPlayer().getLocation().toBlockLocation(), event.getDeathMessage());
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (e.getCause().equals(PlayerKickEvent.Cause.FLYING_PLAYER) || e.getCause().equals(PlayerKickEvent.Cause.FLYING_VEHICLE)) {
            MandatoryChat.modChatAnnouncement(Mandatory.getRawMessage("messages.modchat.messages.flying", "%player%", e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Location location = Mandatory.configuration.getSpawnLocation();
        if (!e.isAnchorSpawn() || !e.isBedSpawn()) {
            if (location != null) {
                e.setRespawnLocation(location);
            }
        }
    }
}
