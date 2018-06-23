package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.ShopCategory;
import net.whitewalker.shopmanager.domain.components.ShopCategoryItem;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ComponentChooseMenu extends DynamicMenu {

    public ComponentChooseMenu(int index, IComponentContainer compContainer, Consumer<Player> updateStrategy) {
        super(Chat.MENU_TITLE + "Choose menu", MenuSize.THREE_LINE);

        setElement(11, new SimpleMenuElement(new ItemBuilder(Material.IRON_INGOT).setName(Chat.MENU_ITEM + "Item").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                ShopComponent component = new ShopCategoryItem(index, new ItemStack(Material.DIRT), -1, -1);
                compContainer.getComponents().add(component);
                updateStrategy.accept(member.getPlayer());
                return true;
            }
        });

        setElement(14, new SimpleMenuElement(new ItemBuilder(Material.BOOK_AND_QUILL).setName(Chat.MENU_ITEM + "Category").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                ShopComponent component = new ShopCategory(index, new ItemStack(Material.DIRT), new ArrayList<>(), MenuSize.THREE_LINE);
                compContainer.getComponents().add(component);
                updateStrategy.accept(member.getPlayer());
                return true;
            }
        });
    }

}
