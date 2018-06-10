package net.whitewalker.shopmanager.domain.events;

import net.rayze.core.spigot.utils.Event;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PlayerSellEvent extends Event {

    private final Player player;
    private final List<ItemStack> items;
    private final double value;

    public PlayerSellEvent(Player player, List<ItemStack> items, double value) {
        this.player = player;
        this.items = items;
        this.value = value;
    }

    public Player getPlayer() {
        return player;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public double getValue() {
        return value;
    }

}
