package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.MenuCloseStrategy;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ShopComponent {

    private int index;
    private ItemStack item;
    private MenuCloseStrategy closeStrategy;

    public ShopComponent(int index, ItemStack item) {
        this.index = index;
        this.item = item;
    }

    public MenuCloseStrategy getCloseStrategy() {
        return closeStrategy;
    }

    public void setCloseStrategy(MenuCloseStrategy closeStrategy) {
        this.closeStrategy = closeStrategy;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getIndex() {
        return index;
    }

    public abstract ItemStack getItemWithManageLore();

    public abstract ItemStack getItemWithShopLore();

    public abstract boolean isValidComponent();

    public abstract boolean onClick(Member member, ClickType clickType);

}
