package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuCloseStrategy;
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

import java.util.HashMap;
import java.util.Map;

public class ShopManageMenu extends DynamicMenu {

    ShopManageMenu(MenuSize menuSize, IComponentContainer compContainer) {
        super(Chat.MENU_TITLE + "Manage menu", menuSize);
        MenuCloseStrategy subCloseStrategy = pl -> reOpen(menuSize, compContainer, pl);

        Map<SimpleMenuElement, Integer> elementMap = new HashMap<>();
        MenuUpdateStrategy strategy = (player) -> reOpen(menuSize, compContainer, player);

        for (int i = 0; i < getSize().getSize(); i++) {
            SimpleMenuElement element = new SimpleMenuElement(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15))
                    .setName(Chat.MENU_ITEM + "New component").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add a new component").build()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    new ComponentChooseMenu(elementMap.get(this), compContainer, strategy).withCloseStrategy(subCloseStrategy).open(member);
                    return true;
                }
            };

            setElement(i, element);
            elementMap.put(element, i);
        }

        for (ShopComponent shopComponent : compContainer.getComponents()) {
            if (shopComponent instanceof ShopCategory) {
                ItemBuilder builder = new ItemBuilder(shopComponent.getItemWithManageLore());
                builder.addLore(Chat.MENU_LORE_PRIM + "<Click> §fto edit category");

                setElement(shopComponent.getIndex(), new SimpleMenuElement(builder.build()) {
                    @Override
                    public boolean onClick(Member member, ClickType click) {
                        new EditCategoryMenu((ShopCategory) shopComponent, compContainer, strategy).withCloseStrat(subCloseStrategy).open(member);
                        return true;
                    }
                });
            } else {
                ItemBuilder builder = new ItemBuilder(shopComponent.getItemWithManageLore());
                builder.addLore(Chat.MENU_LORE_PRIM + "<Click> §fto edit item");

                setElement(shopComponent.getIndex(), new SimpleMenuElement(builder.build()) {
                    @Override
                    public boolean onClick(Member member, ClickType click) {
                        new EditCategoryItemMenu((ShopCategoryItem) shopComponent, compContainer, strategy).withCloseStrat(subCloseStrategy).open(member);
                        return true;
                    }
                });
            }
        }
    }

    private void reOpen(MenuSize menuSize, IComponentContainer compContainer, Player player) {
        this.destroy();
        compContainer.updateItems();
        new ShopManageMenu(menuSize, compContainer).withCloseStrategy(closeStrategy).open(player);
    }

}
