package net.whitewalker.shopmanager.domain.ui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ItemSelectAction {

    void onSelect(Player player, ItemStack item);

}
