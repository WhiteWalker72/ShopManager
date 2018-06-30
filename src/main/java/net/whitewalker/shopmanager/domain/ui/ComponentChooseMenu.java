package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.*;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class ComponentChooseMenu extends DynamicMenu {

    public ComponentChooseMenu(int index, IComponentContainer compContainer, Consumer<Player> updateStrategy) {
        super(Chat.MENU_TITLE + "Choose menu", MenuSize.THREE_LINE);

        BiPredicate<ShopComponent, Member> clickAction = (component, member) -> {
            compContainer.getComponents().add(component);
            updateStrategy.accept(member.getPlayer());
            return true;
        };

        setElement(10, new SimpleMenuElement(new ItemBuilder(Material.IRON_INGOT).setName(Chat.MENU_ITEM + "Item").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                return clickAction.test(new ShopCategoryItem(index, new ItemStack(Material.DIRT), -1, -1), member);
            }
        });

        setElement(13, new SimpleMenuElement(new ItemBuilder(Material.BOOK_AND_QUILL).setName(Chat.MENU_ITEM + "Category").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                return clickAction.test(new ShopCategory(index, new ItemStack(Material.DIRT), new ArrayList<>(), MenuSize.THREE_LINE), member);
            }
        });

        setElement(16, new SimpleMenuElement(new ItemBuilder(Material.CHEST).setName(Chat.MENU_ITEM + "MultiMenu").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                return clickAction.test(new ShopMultiMenu(index, new ItemStack(Material.DIRT), new ArrayList<>()), member);
            }
        });
    }

}
