package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.menu.anvil.AnvilGUI;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ItemChooseMenu extends DynamicMenu {

    ItemChooseMenu(ItemSelectAction selectAction) {
        super(Chat.MENU_TITLE + "Choose menu", MenuSize.THREE_LINE);
        Consumer<Player> closeStrategy = pl -> reOpen(pl, selectAction);

        setElement(10, new SimpleMenuElement(new ItemBuilder(Material.BOOK_AND_QUILL).setName(Chat.MENU_ITEM + "Search menu").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto search for an item").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                member.message("§7Insert a search term");

                new AnvilGUI(member.getPlayer(), "Term", (player, reply) -> {
                    ShopMaterialMenu menu = new ShopMaterialMenu(reply) {
                        @Override
                        public void onMaterialClick(Material material) {
                            selectAction.onSelect(member.getPlayer(), new ItemStack(material));
                        }
                    };

                    if (menu.isEmpty()) {
                        member.message("§7No items found for the '" + reply + "' search term.");
                        reOpen(member.getPlayer(), selectAction);
                        return reply;
                    }

                    menu.withCloseStrategy(closeStrategy).open(player);
                    return reply;
                }, closeStrategy);

                return true;
            }
        });

        setElement(13, new SimpleMenuElement(new ItemBuilder(Material.CHEST).setName(Chat.MENU_ITEM + "Choose menu").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto choose an item").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                ShopMaterialMenu menu = new ShopMaterialMenu() {
                    @Override
                    public void onMaterialClick(Material material) {
                        selectAction.onSelect(member.getPlayer(), new ItemStack(material));
                    }
                };
                menu.withCloseStrategy(closeStrategy).open(member.getPlayer());
                return true;
            }
        });

        setElement(16, new SimpleMenuElement(new ItemBuilder(Material.BLAZE_ROD).setName(Chat.MENU_ITEM + "Inv item").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto choose an item from your inv").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                new InvItemSelectMenu(member.getPlayer()) {
                    @Override
                    public void onItemClick(Player player, ItemStack item) {
                        selectAction.onSelect(player, item);
                    }
                }.withCloseStrategy(closeStrategy).open(member);

                return true;
            }
        });
    }

    private void reOpen(Player player, ItemSelectAction selectAction) {
        new ItemChooseMenu(selectAction).withCloseStrategy(closeStrategy).open(player);
    }

}
