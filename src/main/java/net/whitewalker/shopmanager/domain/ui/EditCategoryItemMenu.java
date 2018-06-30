package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.spigot.menu.anvil.AnvilGUI;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.rayze.core.utils.MathUtils;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.ShopCategoryItem;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.function.Consumer;

public class EditCategoryItemMenu extends EditComponentMenu {

    private final ShopCategoryItem categoryItem;

    public EditCategoryItemMenu(ShopCategoryItem component, IComponentContainer compContainer, Consumer<Player> updateStrategy) {
        super(component, compContainer, updateStrategy);
        this.categoryItem = component;
    }

    @Override
    public void constructMenu() {
        super.constructMenu();

        menu.setElement(3, new SimpleMenuElement(new ItemBuilder(Material.DIAMOND).setName(Chat.MENU_ITEM + "Item Cost").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item shop value").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                double cost = categoryItem.getCost();

                new AnvilGUI(member.getPlayer(), cost + "", (player, reply) -> {
                    if (!replyValidation(member, reply)) {
                        return reply;
                    }
                    double buyValue = Double.parseDouble(reply);
                    categoryItem.setCost(buyValue);
                    categoryItem.setSellValue(buyValue/3);
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });

        menu.setElement(5, new SimpleMenuElement(new ItemBuilder(Material.GOLD_INGOT).setName(Chat.MENU_ITEM + "Sell value").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item sell value").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                double sellValue = categoryItem.getSellValue();

                new AnvilGUI(member.getPlayer(), sellValue + "", (player, reply) -> {
                    if (!replyValidation(member, reply)) {
                        return reply;
                    }
                    categoryItem.setSellValue(Double.parseDouble(reply));
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });

        menu.setElement(41, new SimpleMenuElement(new ItemBuilder(Material.STONE).setName(Chat.MENU_ITEM + "Item data").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item data").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {

                new AnvilGUI(member.getPlayer(), 0 + "", (player, reply) -> {
                    if (!MathUtils.isInt(reply)) {
                        member.message("§7Error: input has to be a " + Chat.PRIM + "number§7.");
                        return reply;
                    }
                    int value = Integer.parseInt(reply);
                    if (value < 0) {
                        member.message("§7Error: input has to be " + Chat.PRIM + "higher §7than 0.");
                        return reply;
                    }
                    if (value > 20) {
                        member.message("§7Error: input is too " + Chat.PRIM + "high§7.");
                        return reply;
                    }
                    categoryItem.setItem(new ItemBuilder(categoryItem.getItem()).setData((byte) value).build());
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });
    }

    private boolean replyValidation(Member member, String reply) {
        if (!MathUtils.isDouble(reply)) {
            member.message("§7Error: input has to be a " + Chat.PRIM + "number§7.");
            return false;
        }
        double newValue = Double.parseDouble(reply);
        if (newValue < 0) {
            member.message("§7Error: input has to be " + Chat.PRIM + "higher §7than 0.");
            return false;
        }
        return true;
    }

}
