package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.whitewalker.shopmanager.domain.ComponentSearcher;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.domain.components.ShopMultiMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

class ShopUpdateCommand extends SubCommand<ShopCommand> {

    ShopUpdateCommand(ShopCommand command) {
        super(command, "Load updates from the config", "", "dev", "update");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        FileConfiguration config = getCommand().getPlugin().getUpdatesFile();
        if (!config.contains("shop_name")) {
            member.message("§7The config doesn't contain a shop name.");
            return;
        }

        String shopName = config.getString("shop_name");
        Shop shop = getCommand().getShopManager().getShop(shopName);
        if (shop == null) {
            member.message("§7Shop " + Chat.PRIM + shopName + "§7 doesn't exist.");
            return;
        }

        Set<String> categoryNames = config.getKeys(false);
        categoryNames.remove("shop_name");

        Bukkit.getScheduler().runTaskAsynchronously(getCommand().getPlugin(), () -> {
            for (String categoryName : categoryNames) {
                List<String> itemList = config.getStringList(categoryName);
                if (itemList != null && !itemList.isEmpty()) {
                    ShopMultiMenu multiMenu = new ComponentSearcher<ShopMultiMenu>().findComponent(categoryName.replaceAll("_", " "), shop, ShopMultiMenu.class);
                    if (multiMenu != null) {
                        for (String line : itemList) {
                            if (!line.contains("=")) {
                                continue;
                            }
                            String materialData = line.substring(0, line.indexOf("="));
                            String[] ids = materialData.split(":");

                            int type = Integer.parseInt(ids[0]);
                            short damage = ids.length > 1 ? Short.parseShort(ids[1]) : 0;
                            ItemStack item = new ItemStack(type, 1);
                            item.setDurability(damage);
                            ItemStack namedItem = new ItemBuilder(item).setName("§f" + getItemName(item)).build();

                            String costStr = line.substring(line.indexOf("=") + 1);
                            try {
                                Double cost = Double.parseDouble(costStr);
                                multiMenu.addOrUpdateItem(namedItem, cost);
                            } catch (Exception ignored) {

                            }
                        }
                        multiMenu.updateItems();
                        member.message("§7Items updated.");

                    } else {
                        member.message(Chat.PRIM + categoryName + "§7 category does not exist in the shop.");
                    }
                } else {
                    member.message("§7No list found for the " + Chat.PRIM + categoryName + "§7 category.");
                }
            }
        });
    }

    private String getItemName(ItemStack item){
        return CraftItemStack.asNMSCopy(item).getName();
    }

    private ShopMultiMenu findMultiMenu(String name, Shop shop) {
        for (ShopComponent component : shop.getComponents()) {
            ShopMultiMenu comp = findMultiMenu(name, component);
            if (comp != null) {
                return comp;
            }
        }
        return null;
    }

    private ShopMultiMenu findMultiMenu(String name, ShopComponent shopComponent) {
        if (shopComponent.getName() != null && shopComponent.getName().equals(name) && shopComponent instanceof ShopMultiMenu) {
            return (ShopMultiMenu) shopComponent;
        }

        if (shopComponent instanceof IComponentContainer) {
            for (ShopComponent component : ((IComponentContainer) shopComponent).getComponents()) {
                ShopMultiMenu comp = findMultiMenu(name, component);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }

}
