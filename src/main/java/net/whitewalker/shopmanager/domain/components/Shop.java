package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.Menu;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.menu.SimpleMenuElement;
import net.rayze.core.utils.Pair;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shop implements IComponentContainer {

    private String shopName;
    private String permission;
    private Menu shopMenu;
    private final MenuSize menuSize;
    private final List<ShopComponent> shopComponents;
    private Map<Pair<Material, Byte>, Double> buyValueMap = new HashMap<>();
    private Map<Pair<Material, Byte>, Double> sellValueMap = new HashMap<>();

    public Shop(String shopName, String permission, MenuSize menuSize, List<ShopComponent> shopComponents) {
        this.shopName = shopName;
        this.permission = permission;
        this.menuSize = menuSize;
        this.shopComponents = shopComponents;


        updateItems();
    }

    @Override
    public void updateItems() {
        buyValueMap = new HashMap<>();
        sellValueMap = new HashMap<>();
        if (shopMenu != null) {
            shopMenu.destroy();
        }
        shopMenu = new Menu(Chat.MENU_TITLE + shopName, menuSize);

        for (ShopComponent shopComponent : shopComponents) {
            if (!shopComponent.isValidComponent()) {
                continue;
            }
            shopComponent.setCloseStrategy(pl -> open(pl));

            shopMenu.setElement(shopComponent.getIndex(), new SimpleMenuElement(shopComponent.getItemWithShopLore()) {
                @Override
                public boolean onClick(Member member, ClickType click) {
                    return shopComponent.onClick(member, click);
                }
            });
            addComponentToMaps(shopComponent);
        }
    }

    private void addComponentToMaps(ShopComponent shopComponent) {
        if (shopComponent instanceof ShopCategoryItem) {
            ItemStack item = shopComponent.getItem();
            buyValueMap.put(new Pair<>(item.getType(), item.getData().getData()), ((ShopCategoryItem) shopComponent).getSellValue());
            sellValueMap.put(new Pair<>(item.getType(), item.getData().getData()), ((ShopCategoryItem) shopComponent).getSellValue());
            return;
        }
        if (shopComponent instanceof ShopCategory) {
            for (ShopComponent component : ((ShopCategory) shopComponent).getComponents()) {
                addComponentToMaps(component);
            }
        }
    }

    public void open(Member member) {
        open(member.getPlayer());
    }

    public void open(Player player) {
        if (permission != null && !permission.equals("") && !player.hasPermission(permission)) {
            player.sendMessage("§7You don't have " + Chat.PRIM + "permission §7to open this menu.");
            return;
        }
        shopMenu.open(player);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public MenuSize getMenuSize() {
        return menuSize;
    }

    public Double getBuyValue(Material material, byte data) {
        for (Map.Entry<Pair<Material, Byte>, Double> entry : buyValueMap.entrySet()) {
            if (entry.getKey().getLeft().equals(material) && entry.getKey().getRight().equals(data))
                return entry.getValue();
        }
        return 0.0;
    }

    public Double getSellValue(Material material, byte data) {
        for (Map.Entry<Pair<Material, Byte>, Double> entry : sellValueMap.entrySet()) {
            if (entry.getKey().getLeft().equals(material) && entry.getKey().getRight().equals(data))
                return entry.getValue();
        }
        return 0.0;
    }

    @Override
    public List<ShopComponent> getComponents() {
        return shopComponents;
    }

}
