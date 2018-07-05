package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.rayze.core.spigot.utils.ItemUtils;
import net.whitewalker.shopmanager.domain.ui.EditComponentMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class ShopComponent {

    private int index;
    private ItemStack item;
    private String displayName;
    private Consumer<Player> closeStrategy;

    public ShopComponent(int index, ItemStack item) {
        this(index, item, "");
    }

    public ShopComponent(int index, ItemStack item, String displayName) {
        this.index = index;
        this.displayName = displayName;
        setItem(item);
    }

    public ShopComponent withDisplayName(String name) {
        setDisplayName(name);
        return this;
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
        this.item = !displayName.isEmpty() ? new ItemBuilder(item).setName(displayName).build() : item;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack getItemWithManageLore() {
        return new ItemBuilder(getItem().clone()).addLore("§7Type: " + getTypeName()).build();
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        setItem(item);
    }

    public String getName() {
        if (displayName != null) {
            return displayName;
        }
        if (ItemUtils.hasDisplayName(getItem())) {
            return ChatColor.stripColor(getItem().getItemMeta().getDisplayName());
        }
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract ItemStack getItemWithShopLore();

    public abstract boolean isValidComponent();

    public abstract boolean onClick(Member member, ClickType clickType);

    public abstract EditComponentMenu getEditMenu(IComponentContainer container, Consumer<Player> updateStrategy);

    public abstract String getTypeName();

}
