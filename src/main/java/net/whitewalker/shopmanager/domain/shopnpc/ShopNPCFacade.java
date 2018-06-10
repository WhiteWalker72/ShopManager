package net.whitewalker.shopmanager.domain.shopnpc;

import net.whitewalker.shopmanager.domain.Main;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Set;

public class ShopNPCFacade {

    private final NPCManager NPCManager;

    public ShopNPCFacade(Main main) {
        this.NPCManager = new NPCManager(main);
    }

    public void createNpc(String name, Location loc, EntityType entityType, String shopName) {
        NPCManager.createNpc(name, loc, entityType, shopName);
    }

    public void deleteNpc(ShopNPC shopNPC) {
        NPCManager.deleteNpc(shopNPC);
    }

    public ShopNPC findNPC(Location loc) {
        return NPCManager.findNPC(loc);
    }

    public ShopNPC findNearest(Location loc) {
        return NPCManager.findNearest(loc);
    }

    public Set<ShopNPC> getAllShopNPCS() {
        return NPCManager.getShopNPCS();
    }

}
