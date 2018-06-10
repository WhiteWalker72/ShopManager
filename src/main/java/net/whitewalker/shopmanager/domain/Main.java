package net.whitewalker.shopmanager.domain;

import net.milkbowl.vault.economy.Economy;
import net.whitewalker.shopmanager.domain.commands.npc.ShopNPCCommand;
import net.whitewalker.shopmanager.domain.commands.shop.ShopCommand;
import net.whitewalker.shopmanager.domain.components.IMoneyStrategy;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPCFacade;
import net.whitewalker.shopmanager.persistence.PersistenceService;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private ShopManager shopManager;
    private ShopNPCFacade shopNPCFacade;
    private PersistenceService persistenceService;
    private Economy econ = null;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        persistenceService = new PersistenceService(getConfig());

        shopManager = new ShopManager(this);
        shopNPCFacade = new ShopNPCFacade(this);
        new ShopCommand(shopManager, this);
        new ShopNPCCommand(this);

        if (setupEconomy()) {
            ShopServices.getInstance().setMoneyStrategy(new IMoneyStrategy() {
                @Override
                public void takeMoney(Player player, double money) {
                    econ.withdrawPlayer(player, money);
                }

                @Override
                public void giveMoney(Player player, double money) {
                    econ.depositPlayer(player, money);
                }

                @Override
                public boolean hasEnoughMoney(Player player, double money) {
                    return (econ.getBalance(player) - money) > 0;
                }
            });
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    @Override
    public void onDisable() {
        saveConfig();
        ShopServices.getInstance().updateAll();
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public ShopNPCFacade getShopNPCFacade() {
        return shopNPCFacade;
    }

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

}
