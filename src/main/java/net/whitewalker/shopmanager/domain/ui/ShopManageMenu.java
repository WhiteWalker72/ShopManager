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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ShopManageMenu extends DynamicMenu {

    ShopManageMenu(MenuSize menuSize, IComponentContainer compContainer) {
        super(Chat.MENU_TITLE + "Manage menu", menuSize);

        Map<SimpleMenuElement, Integer> elementMap = new HashMap<>();
        Consumer<Player> strategy = (player) -> reOpen(menuSize, compContainer, player);

        for (int i = 0; i < getSize().getSize(); i++) {
            SimpleMenuElement element = new SimpleMenuElement(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15))
                    .setName(Chat.MENU_ITEM + "New component").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add a new component").build()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    new ComponentChooseMenu(elementMap.get(this), compContainer, strategy).withCloseStrategy(strategy).open(member);
                    return true;
                }
            };

            setElement(i, element);
            elementMap.put(element, i);
        }

        for (ShopComponent shopComponent : compContainer.getComponents()) {
            setElement(shopComponent.getIndex(), new SimpleMenuElement(shopComponent.getItemWithManageLore()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    shopComponent.getEditMenu(compContainer, strategy).withCloseStrat(strategy).open(member);
                    return true;
                }
            });
        }
    }

    private void reOpen(MenuSize menuSize, IComponentContainer compContainer, Player player) {
        this.destroy();
        compContainer.updateItems();
        new ShopManageMenu(menuSize, compContainer).withCloseStrategy(closeStrategy).open(player);
    }

}
