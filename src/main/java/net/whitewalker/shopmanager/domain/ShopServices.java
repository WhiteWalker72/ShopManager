package net.whitewalker.shopmanager.domain;

import net.rayze.core.spigot.menu.MenuSize;
import net.whitewalker.shopmanager.domain.components.IMoneyStrategy;
import net.whitewalker.shopmanager.domain.components.Shop;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ShopServices {

    private ShopManager shopManager;
    private static ShopServices instance;
    private IMoneyStrategy moneyStrategy;

    private ShopServices() {
        shopManager = JavaPlugin.getPlugin(Main.class).getShopManager();

        setMoneyStrategy(new IMoneyStrategy() {
            @Override
            public void takeMoney(Player player, double money) {
                System.out.println("Warning!! No money strategy found. Can't take money.");
            }

            @Override
            public void giveMoney(Player player, double money) {
                System.out.println("Warning!! No money strategy found. Can't give money.");
            }

            @Override
            public boolean hasEnoughMoney(Player player, double money) {
                return false;
            }
        });
    }

    public Shop getShop(String shopName) {
        return shopManager.getShop(shopName);
    }

    public boolean shopExists(String shopName) {
        return shopManager.shopExists(shopName);
    }

    public boolean updateShop(String shopName) {
        return shopManager.updateShop(shopName);
    }

    public boolean deleteShop(String shopName) {
        return shopManager.deleteShop(shopName);
    }

    public boolean createShop(String shopName, String permission, MenuSize menuSize) {
        return shopManager.createShop(shopName, permission, menuSize);
    }

    public List<Shop> getAllShops() {
        return shopManager.getAllShops();
    }

    public void updateAll() {
        shopManager.updateAll();
    }

    public IMoneyStrategy getMoneyStrategy() {
        return moneyStrategy;
    }

    public void setMoneyStrategy(IMoneyStrategy moneyStrategy) {
        this.moneyStrategy = moneyStrategy;
    }

    public static ShopServices getInstance() {
        if (instance == null)
            instance = new ShopServices();
        return instance;
    }
}
