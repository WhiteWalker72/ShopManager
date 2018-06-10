package net.whitewalker.shopmanager.domain.ui;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.*;
import net.rayze.core.spigot.menu.anvil.AnvilGUI;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.rayze.core.spigot.utils.ItemUtils;
import net.rayze.core.spigot.utils.ServerUtils;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditComponentMenu {

    private final ShopComponent component;
    private final IComponentContainer compContainer;
    private final MenuUpdateStrategy updateStrategy;

    private MenuCloseStrategy closeStrat;
    protected final MenuCloseStrategy subCloseStrategy;
    protected Menu menu;

    EditComponentMenu(ShopComponent component, IComponentContainer compContainer, MenuUpdateStrategy updateStrategy) {
        this.component = component;
        this.compContainer = compContainer;
        this.updateStrategy = updateStrategy;
        this.subCloseStrategy = this::openMenu;
    }

    public void open(Member member) {
        openMenu(member.getPlayer());
    }

    public EditComponentMenu withCloseStrat(MenuCloseStrategy closeStrat) {
        this.closeStrat = closeStrat;
        return this;
    }

    public void constructMenu() {
        menu = new DynamicMenu(Chat.MENU_TITLE + "Component edit menu", MenuSize.FIVE_LINE).withCloseStrategy(closeStrat);

        menu.setElement(10, new SimpleMenuElement(new ItemBuilder(Material.BRICK).setName(Chat.MENU_ITEM + "Set item").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto change the item (material)").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                new ItemChooseMenu(((player, item) -> {
                    if (item.getAmount() > 1) {
                        item.setAmount(1);
                    }
                    component.setItem(item);
                    openMenu(player);
                })).withCloseStrategy(subCloseStrategy).open(member);
                return true;
            }
        });

        menu.setElement(16, new SimpleMenuElement(new ItemBuilder(Material.PAPER).setName(Chat.MENU_ITEM + "Item name").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item name").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                ItemMeta meta = component.getItem().getItemMeta();
                String name = meta.hasDisplayName() ? meta.getDisplayName() : ItemUtils.getName(component.getItem());

                new AnvilGUI(member.getPlayer(), name, (player, reply) -> {
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', reply));
                    component.getItem().setItemMeta(meta);
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });

        menu.setElement(22, new SimpleMenuElement(new ItemBuilder(component.getItemWithManageLore()).addLore(Chat.PRIM + "<Middle Click> §7to delete this component").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                if (click == ClickType.MIDDLE) {
                    compContainer.getComponents().remove(component);
                    compContainer.updateItems();

                    member.message("§7Component deleted.");
                    updateStrategy.onUpdate(member.getPlayer());
                    return true;
                }
                return false;
            }
        });

        menu.setElement(28, new SimpleMenuElement(new ItemBuilder(Material.BOOK_AND_QUILL).setName(Chat.MENU_ITEM + "Item lore").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item lore").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                member.message("§7Splitting new lines on every §a;");
                ItemMeta meta = component.getItem().getItemMeta();

                List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                StringBuilder loreStrBuilder = new StringBuilder();
                for (String s : lore) {
                    loreStrBuilder.append(s).append(";");
                }

                new AnvilGUI(member.getPlayer(), loreStrBuilder.length() > 0 ? loreStrBuilder.toString() : "lore", (player, reply) -> {
                    List<String> newLore = reply == null || reply.isEmpty() ? new ArrayList<>()
                            : Arrays.stream(ChatColor.translateAlternateColorCodes('&', reply).split(";")).map(part -> {
                        if (part.startsWith(" ")) {
                            part = part.replaceFirst(" ", "");
                        }
                        return part;
                    }).collect(Collectors.toList());

                    meta.setLore(newLore);

                    component.getItem().setItemMeta(meta);
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });

        menu.setElement(34, new SimpleMenuElement(new ItemBuilder(new ItemStack(Material.WOOL, 1, (byte) 5))
                .setName(Chat.MENU_ITEM + "Save and close").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto save the component").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                compContainer.updateItems();
                updateStrategy.onUpdate(member.getPlayer());
                return true;
            }
        });

        menu.setElement(39, new SimpleMenuElement(new ItemBuilder(Material.ANVIL).setName(Chat.MENU_ITEM + "Item nbt").addLore(Chat.MENU_LORE_PRIM + "<Click> §fto set the item nbt").build()) {
            @Override
            public boolean onClick(Member member, ClickType click) {
                String nbt = ServerUtils.NMS_HANDLER.getNBT(component.getItem());

                new AnvilGUI(member.getPlayer(), nbt == null ? "NBT" : nbt, (player, reply) -> {
                    component.setItem(ServerUtils.NMS_HANDLER.setNBT(component.getItem(), reply));
                    openMenu(member.getPlayer());
                    return reply;
                }, subCloseStrategy);
                return true;
            }
        });
    }

    public void openMenu(Player player) {
        constructMenu();
        menu.open(player);
    }

}
