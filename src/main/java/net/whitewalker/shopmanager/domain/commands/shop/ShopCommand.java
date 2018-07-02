package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.Command;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.Main;
import net.whitewalker.shopmanager.domain.ShopManager;
import net.whitewalker.shopmanager.utils.Chat;

public class ShopCommand extends Command {

    private final ShopManager shopManager;
    private final Main plugin;

    public ShopCommand(ShopManager shopManager, Main plugin) {
        super("shop", "View all shop commands", "sr_mod");
        this.shopManager = shopManager;
        this.plugin = plugin;

        setDefaultSubCommand(new OpenShopCommand(this));
        new CreateShopCommand(this);
        new DeleteShopCommand(this);
        new ManageShopCommand(this);
        new OpenShopCommand(this);
        new RenameShopCommand(this);
        new SellCommand(this);
        new ShopListCommand(this);
        new ShopPermissionCommand(this);
        new ShopRemoveItemsCommand(this);
        new ShopUpdateCommand(this);
    }

    public boolean shopExistsTest(Member member, String shopName) {
        if (!shopManager.shopExists(shopName)) {
            member.message("§7There is no shop called '" + Chat.PRIM + shopName + "§7' type " + Chat.PRIM + "/shop list §7to view all shop names." );
            return false;
        }
        return true;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public Main getPlugin() {
        return plugin;
    }

}
