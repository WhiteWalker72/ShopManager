package net.whitewalker.shopmanager.domain.events;

import net.rayze.core.spigot.utils.Event;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerItemBuyEvent extends Event {

    private final Player player;
    private final ItemStack item;
    private final double cost;

    public PlayerItemBuyEvent(Player player, ItemStack item, double cost) {
        this.player = player;
        this.item = item;
        this.cost = cost;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public double getCost() {
        return cost;
    }

}
