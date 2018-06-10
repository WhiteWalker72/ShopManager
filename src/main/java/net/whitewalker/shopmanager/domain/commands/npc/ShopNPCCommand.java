package net.whitewalker.shopmanager.domain.commands.npc;

import net.rayze.core.spigot.commands.base.Command;
import net.whitewalker.shopmanager.domain.Main;
import net.whitewalker.shopmanager.domain.ShopManager;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPCFacade;

public class ShopNPCCommand extends Command {

    private final Main plugin;

    public ShopNPCCommand(Main plugin) {
        super("shopnpc", "View all shopnpc commands", "sr_mod", "npcshop");
        this.plugin = plugin;

        new CreateNPCCommand(this);
        new DeleteNPCCommand(this);
        new ListNPCCommand(this, plugin);
    }

    ShopNPCFacade getShopNPCFacade() {
        return plugin.getShopNPCFacade();
    }

    ShopManager getShopManager() {
        return plugin.getShopManager();
    }

}
