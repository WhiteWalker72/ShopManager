package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.ShopCategoryItem;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.domain.components.ShopMultiMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditMultiMenu extends EditComponentMenu {

    private final ShopMultiMenu multiMenu;

    public EditMultiMenu(ShopMultiMenu component, IComponentContainer compContainer, Consumer<Player> updateStrategy) {
        super(component, compContainer, updateStrategy);
        this.multiMenu = component;
    }

    @Override
    public void constructMenu() {
        super.constructMenu();

        menu.setElement(3, new SimpleMenuElement(new ItemBuilder(Material.CHEST).setName(Chat.MENU_ITEM + "Add Items").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add shop items").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                List<ShopComponent> components = multiMenu.getComponents().stream().filter(shopComponent -> shopComponent instanceof ShopCategoryItem && ((ShopCategoryItem) shopComponent).getCost() <= 0).collect(Collectors.toList());
                new ItemAddMenu(multiMenu, components).withCloseStrategy(subCloseStrategy).open(member);
                return true;
            }
        });

        menu.setElement(5, new SimpleMenuElement(new ItemBuilder(Material.BARRIER).setName(Chat.MENU_ITEM + "Remove Items").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto remove shop items").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                if (multiMenu.getComponents().isEmpty()) {
                    member.message("§7No items found.");
                    return false;
                }

                new ItemRemoveMenu(multiMenu).withCloseStrategy(subCloseStrategy).open(member);
                return true;
            }
        });
    }

}
