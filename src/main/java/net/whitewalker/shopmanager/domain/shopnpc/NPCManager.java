package net.whitewalker.shopmanager.domain.shopnpc;

import net.rayze.core.spigot.algorithms.kdtree.KDTree;
import net.rayze.core.spigot.algorithms.kdtree.exception.KeyDuplicateException;
import net.rayze.core.spigot.algorithms.kdtree.exception.KeyMissingException;
import net.rayze.core.spigot.algorithms.kdtree.exception.KeySizeException;
import net.whitewalker.shopmanager.domain.Main;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.*;

public class NPCManager {

    private final Main plugin;
    private final Map<String, KDTree<String>> npcTreeMap = new HashMap<>();
    private final Set<ShopNPC> shopNPCS = new HashSet<>();

    NPCManager(Main plugin) {
        this.plugin = plugin;
        shopNPCS.addAll(plugin.getPersistenceService().findAllShopNPCs());

        for (ShopNPC shopNPC : shopNPCS) {
            addToTree(shopNPC);
        }
    }

    public ShopNPC findNPC(Location loc) {
        KDTree<String> tree = getTree(loc);
        if (tree == null) {
            return null;
        }
        try {
            String shopName = tree.search(locationToPoint(loc));
            if (shopName == null) {
                return null;
            }
            for (ShopNPC shopNPC : shopNPCS) {
                if (shopNPC.getUUIDStr().equals(shopName)) {
                    return shopNPC;
                }
            }
        } catch (KeySizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createNpc(String name, Location loc, EntityType entityType, String shopName) {
        ShopNPC npc = new ShopNPC(UUID.randomUUID().toString(), entityType, loc, name, shopName);
        plugin.getPersistenceService().insertNPC(npc);
        shopNPCS.add(npc);
        addToTree(npc);
    }

    public void deleteNpc(ShopNPC shopNPC) {
        KDTree<String> tree = getTree(shopNPC.getLocation());
        if (tree != null) {
            try {
                tree.delete(locationToPoint(shopNPC.getLocation()));
            } catch (KeySizeException | KeyMissingException e) {
                e.printStackTrace();
            }
        }
        shopNPCS.remove(shopNPC);
        plugin.getPersistenceService().deleteNPC(shopNPC);
    }


    private KDTree<String> getTree(Location loc) {
        String worldName = loc.getWorld().getName();
        return npcTreeMap.get(worldName);
    }

    private void addToTree(ShopNPC npc) {
        if (npc.getLocation() == null) {
            return;
        }
        String worldName = npc.getLocation().getWorld().getName();
        if (getTree(npc.getLocation()) == null) {
            npcTreeMap.put(worldName, new KDTree<>(3));
        }
        try {
            npcTreeMap.get(worldName).insert(locationToPoint(npc.getLocation()), npc.getUUIDStr());
        } catch (KeySizeException | KeyDuplicateException e) {
            e.printStackTrace();
        }
    }

    private int[] locationToPoint(Location loc) {
        return new int[]{(int) loc.getX(), (int) loc.getY(), (int) loc.getZ()};
    }

    public ShopNPC findNearest(Location loc) {
        KDTree<String> tree = npcTreeMap.get(loc.getWorld().getName());
        if (tree == null) {
            return null;
        }
        try {
            String found = tree.nearest(locationToPoint(loc));
            if (found == null) {
                return null;
            }
            for (ShopNPC shopNPC : shopNPCS) {
                if (shopNPC.getUUIDStr().equals(found)) {
                    return shopNPC;
                }
            }
        } catch (KeySizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<ShopNPC> getShopNPCS() {
        return shopNPCS;
    }

}
