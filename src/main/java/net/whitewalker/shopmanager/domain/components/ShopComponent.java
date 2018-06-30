package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.ui.EditComponentMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class ShopComponent {

    private int index;
    private ItemStack item;
    private Consumer<Player> closeStrategy;

    public ShopComponent(int index, ItemStack item) {
        this.index = index;
        this.item = item;
    }

    public Consumer<Player> getCloseStrategy() {
        return closeStrategy;
    }

    public void setCloseStrategy(Consumer<Player> closeStrategy) {
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

    public ItemStack getItemWithManageLore() {
        return new ItemBuilder(getItem().clone()).addLore("§7Type: " + getTypeName()).build();
    }

    public abstract ItemStack getItemWithShopLore();

    public abstract boolean isValidComponent();

    public abstract boolean onClick(Member member, ClickType clickType);

    public abstract EditComponentMenu getEditMenu(IComponentContainer container, Consumer<Player> updateStrategy);

    public abstract String getTypeName();

}
