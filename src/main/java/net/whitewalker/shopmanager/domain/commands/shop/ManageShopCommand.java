package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.Menu;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.domain.ui.ComponentChooseMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


class ManageShopCommand extends SubCommand<ShopCommand> {

    ManageShopCommand(ShopCommand command) {
        super(command, "Manage shop items and categories", "<shopname>", "sr_mod", "manage");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        final String shopName = args[0];
        if (!getCommand().shopExistsTest(member, shopName))
            return;
        Consumer<Player> closeStrategy = pl -> execute(member, label, args);

        Shop shop = getCommand().getShopManager().getShop(shopName);

        Menu menu = new DynamicMenu(Chat.MENU_TITLE + shopName + " manage menu", shop.getMenuSize()).withCloseStrategy(pl -> shop.updateItems());
        Map<SimpleMenuElement, Integer> elementMap = new HashMap<>();

        Consumer<Player> updateStrategy = player -> execute(member, label, args);

        for (int i = 0; i < menu.getSize().getSize(); i++) {
            SimpleMenuElement element = new SimpleMenuElement(new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15))
                    .setName(Chat.MENU_ITEM + "New component").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto add a new component").build()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    Menu chooseMenu = new ComponentChooseMenu(elementMap.get(this), shop, updateStrategy).withCloseStrategy(closeStrategy);
                    chooseMenu.open(member);
                    return true;
                }
            };

            menu.setElement(i, element);
            elementMap.put(element, i);
        }

        for (ShopComponent shopComponent : shop.getComponents()) {
            ItemBuilder builder = new ItemBuilder(shopComponent.getItemWithManageLore());
            menu.setElement(shopComponent.getIndex(), new SimpleMenuElement(builder.build()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    shopComponent.getEditMenu(shop, updateStrategy).withCloseStrat(closeStrategy).open(member);
                    return true;
                }
            });
        }

        menu.open(member);
    }

}
