package xyz.supeer.mandatory.commands;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTFile;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtinjector.NBTInjector;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CartographyInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.supeer.mandatory.Main;
import xyz.supeer.mandatory.sql.SQLGetter;
import xyz.supeer.mandatory.utils.PlayerHeadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class BuyCommand implements CommandExecutor {

    private Main plugin;

    public BuyCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("buy").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cEndast spelare kan utfärda detta kommando.");
            return false;
        }
        Player p = (Player) sender;

        if (!p.hasPermission("mandatory.command.buy")) {
            p.sendMessage("§cÅtkomst nekad.");
            return false;
        }

        if (args.length == 0) {
            p.sendMessage("§7Syntax: /buy broadcast §nmeddelande");
            p.sendMessage("§7Syntax: /buy go §nnamn");
            p.sendMessage("§7Syntax: /buy head");
            p.sendMessage("§7Syntax: /buy copyright");
            return false;
        }
        if (args.length == 1) {
            if ("help".equalsIgnoreCase(args[0])) {
                if (!p.hasPermission("mandatory.command.buy")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
                p.sendMessage("§2--- §aHjälp: /buy §2---");
                p.sendMessage("§2/buy broadcast §ameddelande");
                p.sendMessage("§2/buy go §anamn");
                p.sendMessage("§2/buy copyright");
                return false;
            }
            if ("go".equals(args[0])) {
                if (!p.hasPermission("mandatory.command.buy.go")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
                p.sendMessage("§7Syntax: /buy go §nnamn");
                return false;
            }
            if ("head".equalsIgnoreCase(args[0])) {
                if (!p.hasPermission("mandatory.command.buy.head")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
                if (SQLGetter.getBalance(p.getUniqueId()) >= 1000) {
                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage("§cDitt inventory saknar plats.");
                        return false;
                    }
                    SQLGetter.removeBalance(p.getUniqueId(), 1000);
                    p.getInventory().addItem(PlayerHeadUtil.getPlayerHead(p));
                    p.sendMessage("§aDu har köpt ett exemplar av ditt huvud för 1 000 minemynt.");
                    return false;
                } else if (SQLGetter.getBalance(p.getUniqueId()) <= 1000) {
                    p.sendMessage("§cDu har inte råd med 1 000 minemynt, som ett exemplar av ditt huvud kostar.");
                    return false;
                }
            }
            if ("copyright".equalsIgnoreCase(args[0])) {
                if (!p.hasPermission("mandatory.command.buy.copyright")) {
                    p.sendMessage("§cÅtkomst nekad.");
                    return false;
                }
                if (SQLGetter.getBalance(p.getUniqueId()) >= 10000) {
                    ItemStack item = p.getInventory().getItemInMainHand();
                    if (item.getType().isAir()) {
                        p.sendMessage("§cVänligen håll det du vill kopieringsskydda i handen.");
                        return false;
                    } else if (!item.getType().equals(Material.FILLED_MAP)) {
                        p.sendMessage("§cFöremålet du håller i handen kan inte kopieringsskyddas.");
                        return false;
                    }
                    if (item.getItemMeta().hasLore()) {
                        p.sendMessage("§cFöremålet du håller i handen kan inte kopieringsskyddas.");
                        return false;
                    }


                    ItemMeta meta = item.getItemMeta();
                    ArrayList<String> lore = new ArrayList<String>();
                    lore.add("§5Kopieringsskyddad av " + p.getDisplayName());
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    NBTItem nbti = new NBTItem(item);

                    nbti.setString("copyrighter", p.getUniqueId().toString());
                    item = nbti.getItem();
                    nbti.getKeys();
                    p.getInventory().getItemInMainHand().setAmount(0);
                    p.getInventory().addItem(item);
                    p.sendMessage("§aDu har köpt ett kopieringsskydd för 10 000 minemynt.");
                    return false;

                } else if (SQLGetter.getBalance(p.getUniqueId()) <= 10000) {
                    p.sendMessage("§cDu har inte råd med 10 000 minemynt, som ett kopieringsskydd kostar.");

                    return false;
                }

            }
            if (args.length == 2) {
                if ("go".equalsIgnoreCase(args[0])) {
                    if (!p.hasPermission("mandatory.command.buy.go")) {
                        p.sendMessage("§cÅtkomst nekad.");
                        return false;
                    }
                    String name = args[1].toLowerCase();
                    if (SQLGetter.goExists(name)) {
                        p.sendMessage("§cNamnet används av en annan teleporteringspunkt.");
                        return false;
                    }
                    Location loc = p.getLocation();
                    if (SQLGetter.getBalance(p.getUniqueId()) >= 15000) {
                        SQLGetter.removeBalance(p.getUniqueId(), 15000);
                        SQLGetter.addGo(loc.getWorld().getName(), name, loc.getX(), loc.getY(), loc.getZ(), loc.getPitch(), loc.getYaw(), p.getUniqueId());
                        p.sendMessage("§aDu har köpt en teleporteringspunkt för 15 000 minemynt.");
                        return false;
                    } else if (SQLGetter.getBalance(p.getUniqueId()) <= 15000) {
                        p.sendMessage("§cDu har inte råd med 15 000 minemynt, som en teleporteringspunkt kostar.");
                        return false;
                    }

                }
            }
            if ("head".equalsIgnoreCase(args[0])) {
                if (args.length > 1) {
                    p.sendMessage("§7Syntax: /buy head");
                    return false;
                }
            }
            return false;
        }
        return false;
    }
}