package net.whitewalker.shopmanager.domain;

import net.rayze.core.spigot.menu.MenuSize;
import net.whitewalker.shopmanager.domain.components.Shop;

import java.util.ArrayList;
import java.util.List;

public class ShopManager {

    private final Main plugin;
    private final List<Shop> shops = new ArrayList<>();

    ShopManager(Main plugin) {
        this.plugin = plugin;
        shops.addAll(plugin.getPersistenceService().findAllShops());
    }

    public Shop getShop(String shopName) {
        for (Shop shop : shops) {
            if (shop.getShopName().equals(shopName)) {
                return shop;
            }
        }
        return null;
    }

    public boolean shopExists(String shopName) {
        return getShop(shopName) != null;
    }

    public boolean updateShop(String shopName) {
        Shop shop = getShop(shopName);
        return shop != null && plugin.getPersistenceService().updateShop(shop);
    }

    public boolean deleteShop(String shopName) {
        Shop shop = getShop(shopName);
        if (shop == null) {
            return false;
        }

        boolean deleted = plugin.getPersistenceService().deleteShop(shop);
        if (deleted) {
            shops.remove(shop);
        }
        return deleted;
    }

    public List<Shop> getAllShops() {
        return shops;
    }

    public boolean createShop(String shopName, String permission, MenuSize menuSize) {
        if (shopExists(shopName)) {
            return false;
        }

        Shop shop = new Shop(shopName, permission != null ? permission : "", menuSize, new ArrayList<>());
        plugin.getPersistenceService().insertShop(shop);
        shops.add(shop);
        return true;
    }

    public void updateAll() {
        shops.forEach(shop -> plugin.getPersistenceService().updateShop(shop));
    }

}
