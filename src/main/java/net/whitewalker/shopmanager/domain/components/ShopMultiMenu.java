package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.IMenuElement;
import net.rayze.core.spigot.menu.MultiMenu;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.whitewalker.shopmanager.domain.ui.EditComponentMenu;
import net.whitewalker.shopmanager.domain.ui.EditMultiMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ShopMultiMenu extends ShopComponent implements IComponentContainer {

    private final String title;
    private final List<ShopComponent> shopItems;
    private MultiMenu subMenu;

    public ShopMultiMenu(int index, ItemStack item, List<ShopComponent> shopItems) {
        super(index, item);
        this.title = getItem().getItemMeta().hasDisplayName() ? getItem().getItemMeta().getDisplayName() : "§7MultiMenu";
        this.shopItems = shopItems;
        updateItems();
    }

    @Override
    public List<ShopComponent> getComponents() {
        return shopItems;
    }

    @Override
    public void updateItems() {
        if (subMenu != null) {
            subMenu.destroy();
        }
        subMenu = new MultiMenu(title);
        for (IMenuElement element : shopItems.stream().filter(ShopComponent::isValidComponent)
                .map(item -> createElement(subMenu, item)).collect(Collectors.toList())) {
            subMenu.addElement(element);
        }
    }

    private IMenuElement createElement(MultiMenu subMenu, ShopComponent item) {
        item.setCloseStrategy(subMenu::open);
        return new SimpleMenuElement(item.getItemWithShopLore()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                return item.onClick(member, click);
            }
        };
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
        if (getComponents().isEmpty()) {
            member.message("§7No items found.");
            return false;
        }

        if (getCloseStrategy() != null) {
            subMenu.withCloseStrategy(getCloseStrategy());
        }
        subMenu.open(member);
        return true;
    }

    @Override
    public EditComponentMenu getEditMenu(IComponentContainer container, Consumer<Player> updateStrategy) {
        return new EditMultiMenu(this, container, updateStrategy);
    }

    @Override
    public String getTypeName() {
        return "MultiMenu";
    }

}
