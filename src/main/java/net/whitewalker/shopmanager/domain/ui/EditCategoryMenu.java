package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.Menu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.ShopCategory;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EditCategoryMenu extends EditComponentMenu {

    private final ShopCategory shopCategory;

    public EditCategoryMenu(ShopCategory component, IComponentContainer compContainer, Consumer<Player> updateStrategy) {
        super(component, compContainer, updateStrategy);
        this.shopCategory = component;
    }

    @Override
    public void constructMenu() {
        super.constructMenu();

        menu.setElement(3, new SimpleMenuElement(new ItemBuilder(Material.CHEST).setName(Chat.MENU_ITEM + "Add components").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add components").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                new ShopManageMenu(shopCategory.getMenuSize(), shopCategory).withCloseStrategy(subCloseStrategy).open(member);
                return true;
            }
        });

        menu.setElement(5, new SimpleMenuElement(new ItemBuilder(Material.ANVIL).setName(Chat.MENU_ITEM + "Set size").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto change the menu size").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                Menu menu = new DynamicMenu("Set size", MenuSize.ONE_LINE).withCloseStrategy(subCloseStrategy);

                for (MenuSize menuSize : MenuSize.values()) {
                    menu.addElement(new SimpleMenuElement(new ItemBuilder(Material.IRON_INGOT).setName(Chat.MENU_ITEM + menuSize.getSize() + " slots").build()) {
                        @Override
                        public boolean onClick(Member member, ClickType click) {
                            if (!shopCategory.getComponents().stream().filter(component -> component.getIndex() > menuSize.getSize()).collect(Collectors.toList()).isEmpty()) {
                                member.message("§7Some components have an higher index than the new menu size, remove those components and try again.");
                                return false;
                            }

                            shopCategory.setMenuSize(menuSize);
                            shopCategory.updateItems();
                            member.message("§7Slots updated to " + Chat.PRIM + menuSize.getSize() + "§7 slots.");
                            open(member);
                            return true;
                        }
                    });
                }

                menu.open(member);
                return true;
            }
        });
    }

}

