package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.Menu;
import net.rayze.core.spigot.menu.MenuCloseStrategy;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopCategory extends ShopComponent implements IComponentContainer {

    private Menu subMenu;
    private String title;
    private List<ShopComponent> categoryItems;
    private MenuSize menuSize;

    public ShopCategory(int index, ItemStack item, List<ShopComponent> categoryItems, MenuSize menuSize) {
        super(index, item);
        this.title = getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : "§7Category";
        this.categoryItems = categoryItems;
        this.menuSize = menuSize;
        updateItems();
    }

    @Override
    public ItemStack getItemWithManageLore() {
        return new ItemBuilder(getItem().clone()).addLore("§7Type: category").build();
    }

    @Override
    public ItemStack getItemWithShopLore() {
        return getItem();
    }

    @Override
    public boolean isValidComponent() {
        return true;
    }

    @Override
    public boolean onClick(Member member, ClickType clickType) {
        if (getCloseStrategy() != null) {
            subMenu.withCloseStrategy(getCloseStrategy());
        }
        subMenu.open(member);
        return true;
    }

    @Override
    public List<ShopComponent> getComponents() {
        return categoryItems;
    }

    @Override
    public void updateItems() {
        if (subMenu != null) {
            subMenu.destroy();
        }
        subMenu = new Menu(title, menuSize);
        MenuCloseStrategy subCloseStrategy = pl -> subMenu.open(pl);

        categoryItems.stream().filter(ShopComponent::isValidComponent).forEach(shopComponent -> {
            shopComponent.setCloseStrategy(subCloseStrategy);
            subMenu.setElement(shopComponent.getIndex(), new SimpleMenuElement(shopComponent.getItemWithShopLore()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    return shopComponent.onClick(member, click);
                }
            });
        });
    }

    public MenuSize getMenuSize() {
        return menuSize;
    }

    public void setMenuSize(MenuSize menuSize) {
        this.menuSize = menuSize;
    }

}
