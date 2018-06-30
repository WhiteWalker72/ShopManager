package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.Menu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.whitewalker.shopmanager.domain.ui.EditCategoryMenu;
import net.whitewalker.shopmanager.domain.ui.EditComponentMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class ShopCategory extends ShopComponent implements IComponentContainer {

    private Menu subMenu;
    private final String title;
    private final List<ShopComponent> categoryItems;
    private MenuSize menuSize;

    public ShopCategory(int index, ItemStack item, List<ShopComponent> categoryItems, MenuSize menuSize) {
        super(index, item);
        this.title = getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : "§7Category";
        this.categoryItems = categoryItems;
        this.menuSize = menuSize;
        updateItems();
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
    public EditComponentMenu getEditMenu(IComponentContainer container, Consumer<Player> updateStrategy) {
        return new EditCategoryMenu(this, container, updateStrategy);
    }

    @Override
    public String getTypeName() {
        return "Category";
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
        Consumer<Player> subCloseStrategy = pl -> subMenu.open(pl);

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
