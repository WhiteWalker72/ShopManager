package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.domain.components.ShopMultiMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ItemAddMenu extends DynamicMenu {

    private final List<ShopComponent> components;

    ItemAddMenu(ShopMultiMenu container) {
        this(container, new ArrayList<>());
    }

    ItemAddMenu(ShopMultiMenu container, List<ShopComponent> components) {
        super(Chat.MENU_TITLE + "Item add menu", MenuSize.SIX_LINE);
        this.components = components;
        Consumer<Player> strategy = (player) -> reOpen(container, player);

        for (ShopComponent component : components) {
            addElement(new SimpleMenuElement(component.getItemWithManageLore()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    component.getEditMenu(container, strategy).withCloseStrat(strategy).open(member);
                    return true;
                }
            });
        }


        int index = getNextElementIndex();

        SimpleMenuElement element = new SimpleMenuElement(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15))
                .setName(Chat.MENU_ITEM + "New component").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add a new component").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                new ComponentChooseMenu(index, container, strategy).withCloseStrategy(strategy).open(member);
                return true;
            }
        };
        setElement(index, element);
    }

    private void reOpen(ShopMultiMenu compContainer, Player player) {
        this.destroy();
        compContainer.updateItems();

        if (!compContainer.getComponents().isEmpty()) {
            if (components.isEmpty()) {
                components.add(compContainer.getComponents().get(compContainer.getComponents().size() - 1));
            } else {
                ShopComponent lastComponent = compContainer.getComponents().get(compContainer.getComponents().size() - 1);
                if (!lastComponent.equals(components.get(components.size() - 1))) {
                    components.add(compContainer.getComponents().get(compContainer.getComponents().size() - 1));
                }
            }
        }

        new ItemAddMenu(compContainer, components).withCloseStrategy(closeStrategy).open(player);
    }

}
