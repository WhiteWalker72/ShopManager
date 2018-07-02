package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMultiMenu;
import net.rayze.core.spigot.menu.IMenuElement;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.domain.components.ShopMultiMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

class ItemRemoveMenu extends DynamicMultiMenu {

    private final ShopMultiMenu container;

    ItemRemoveMenu(ShopMultiMenu multiMenu) {
        super(Chat.MENU_TITLE + "Item remove menu", new ArrayList<>());
        container = multiMenu;

        List<IMenuElement> menuElements = new ArrayList<>();
        for (ShopComponent component : multiMenu.getComponents()) {
            ItemBuilder builder = new ItemBuilder(component.getItem()).addLore(Chat.MENU_LORE_PRIM + "<Click> §fto remove");
            IMenuElement element = new SimpleMenuElement(builder.build()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    menuElements.remove(this);
                    container.getComponents().remove(component);
                    container.updateItems();
                    reOpen(multiMenu, menuElements, member.getPlayer());
                    return true;
                }
            };
            menuElements.add(element);
        }
        for (IMenuElement element : menuElements) {
            addElement(element);
        }
    }

    ItemRemoveMenu(ShopMultiMenu multiMenu, List<IMenuElement> elements) {
        super(Chat.MENU_TITLE + "Item remove menu", elements);
        container = multiMenu;
    }

    private void reOpen(ShopMultiMenu compContainer, List<IMenuElement> elements, Player player) {
        this.destroy();
        new ItemRemoveMenu(compContainer, elements).withCloseStrategy(closeStrategy).open(player);
    }

}
