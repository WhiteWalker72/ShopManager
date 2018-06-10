package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.rayze.core.spigot.utils.ServerUtils;
import net.rayze.core.utils.MathUtils;
import net.whitewalker.shopmanager.domain.Main;
import net.whitewalker.shopmanager.domain.ShopServices;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.events.PlayerSellEvent;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class SellShop implements Listener {

    private Inventory inv;
    private final Player player;
    private final Shop shop;
    private double sellTotal = 0;
    private final ItemStack sellChest = new ItemBuilder(Material.CHEST).setName(Chat.MENU_ITEM + "Sell for: $0").build();
    private JavaPlugin plugin;

    public SellShop(Player player, Shop shop) {
        this.player = player;
        this.shop = shop;
        this.inv = Bukkit.createInventory(player, MenuSize.FIVE_LINE.getSize(), Chat.MENU_TITLE + "Sell Shop");
        inv.setItem(40, sellChest);

        player.openInventory(inv);
        ServerUtils.registerListener(this);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR || !(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();

        if (item.equals(sellChest)) {
            event.setCancelled(true);

            if (countInvItems() == 0) {
                player.sendMessage("§7You didn't sell any items.");
            } else {
                player.sendMessage("§7You sold your items for $" + Chat.PRIM + sellTotal + "§7.");
                ShopServices.getInstance().getMoneyStrategy().giveMoney(player, sellTotal);
                Bukkit.getPluginManager().callEvent(new PlayerSellEvent(player, getInvItems(), sellTotal));
            }

            inv = null;
            player.closeInventory();
            ServerUtils.unregisterListener(this);
            return;
        }
        refreshSellTotal();
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Inventory clickedInv = event.getInventory();
        if (inv == null || clickedInv == null || !clickedInv.equals(inv)) {
            return;
        }
        for (ItemStack item : getInvItems()) {
            event.getPlayer().getInventory().addItem(item);
        }
        ServerUtils.unregisterListener(this);
    }

    private int countInvItems() {
        return getInvItems().size();
    }

    private void refreshSellTotal() {
        InventoryView openInv = player.getOpenInventory();
        if (openInv == null || !openInv.getTopInventory().equals(inv)) {
            ServerUtils.unregisterListener(this);
            return;
        }

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> {
            double value = 0;
            for (ItemStack item : getInvItems()) {
                value += getValue(item);
            }
            sellTotal = MathUtils.trim(value);

            ItemMeta itemMeta = sellChest.getItemMeta();
            itemMeta.setDisplayName(Chat.MENU_ITEM + "Sell for: $" + sellTotal);
            sellChest.setItemMeta(itemMeta);
            inv.setItem(40, sellChest);
        }, 1);
    }

    private List<ItemStack> getInvItems() {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() != Material.AIR && item.getType() != Material.CHEST) {
                items.add(item);
            }
        }
        return items;
    }

    private double getValue(ItemStack item) {
        if (shop != null) {
            return shop.getSellValue(item.getType(), item.getData().getData()) * item.getAmount();
        }
        for (Shop loopShop : ShopServices.getInstance().getAllShops()) {
            double value = loopShop.getSellValue(item.getType(), item.getData().getData()) * item.getAmount();
            if (value > 0) {
                return value;
            }
        }
        return 0;
    }

    private JavaPlugin getPlugin() {
        if (plugin == null)
            plugin = JavaPlugin.getPlugin(Main.class);
        return plugin;
    }

}
