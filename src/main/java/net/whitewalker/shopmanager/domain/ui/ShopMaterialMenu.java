package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.MultiMenu;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.utils.BlockUtils;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

public abstract class ShopMaterialMenu extends MultiMenu {

    ShopMaterialMenu() {
        this("");
    }

    ShopMaterialMenu(String filterStr) {
        super(Chat.MENU_TITLE + "Item Menu");
        createMenu(filterStr);
    }

    private void createMenu(String filterStr) {
        filterStr = filterStr.toLowerCase();
        for (Material material : Material.values()) {
            if (!filterStr.isEmpty() && !material.toString().toLowerCase().contains(filterStr)) {
                continue;
            }

            if (BlockUtils.isItem(material)) {
                addElement(new SimpleMenuElement(new ItemBuilder(material).setName(Chat.MENU_ITEM + material.toString()).build()) {
                    @Override
                    public boolean onClick(Member member, ClickType click) {
                        onMaterialClick(material);
                        return true;
                    }
                });
            }
        }
    }

    public abstract void onMaterialClick(Material material);

}
