package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.DynamicMenu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class InvItemSelectMenu extends DynamicMenu {

    InvItemSelectMenu(Player player) {
        super(Chat.MENU_TITLE + "Your inv", MenuSize.FOUR_LINE);

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                addElement(new SimpleMenuElement(item) {
                    @Override
                    public boolean onClick(Member member, ClickType click) {
                        onItemClick(player, item);
                        return true;
                    }
                });
            }
        }
    }

    public abstract void onItemClick(Player player, ItemStack item);

}
