package net.whitewalker.shopmanager.domain.components;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.utils.ItemBuilder;
import net.rayze.core.spigot.utils.ItemUtils;
import net.rayze.core.utils.MathUtils;
import net.whitewalker.shopmanager.domain.ShopServices;
import net.whitewalker.shopmanager.domain.events.PlayerItemBuyEvent;
import net.whitewalker.shopmanager.domain.ui.EditCategoryItemMenu;
import net.whitewalker.shopmanager.domain.ui.EditComponentMenu;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ShopCategoryItem extends ShopComponent {

    private double cost;
    private double sellValue;

    public ShopCategoryItem(int index, ItemStack item, double cost) {
        this(index, item, cost, cost < 0 ? cost : (cost / 3));
    }

    public ShopCategoryItem(int index, ItemStack item, double cost, double sellValue) {
        super(index, item);
        this.cost = cost;
        this.sellValue = MathUtils.trim(sellValue);
    }

    @Override
    public ItemStack getItemWithManageLore() {
        return new ItemBuilder(getItem().clone()).addLore("§7Type: item", "§fCost: §6$" + cost, "§fSell value: §6$" + sellValue).build();
    }

    @Override
    public ItemStack getItemWithShopLore() {
        return new ItemBuilder(getItem().clone()).addLore("§7Left click for 1 for §6$" + getCost(),  "§7Middle click for 16 for §6$" + getCost(32), "§7Right click for 64 for §6$" + getCost(64)).build();
    }

    @Override
    public boolean isValidComponent() {
        return cost > 0;
    }

    @Override
    public boolean onClick(Member member, ClickType clickType) {
        Inventory inv = member.getPlayer().getInventory();
        if (inv.firstEmpty() == -1) {
            member.message("§7You don't have enough " + Chat.PRIM + "space §7in your inventory.");
            return false;
        }

        int amount = clickType == ClickType.RIGHT ? 64 : clickType == ClickType.MIDDLE ? 32 : 1;
        double buyCost = getCost(amount);

        IMoneyStrategy moneyStrategy = ShopServices.getInstance().getMoneyStrategy();
        if (!moneyStrategy.hasEnoughMoney(member.getPlayer(), buyCost)) {
            member.message("§7You don't have enough money to buy this item.");
            return false;
        }
        moneyStrategy.takeMoney(member.getPlayer(), buyCost);

        ItemStack bought = getItem().clone();
        bought.setAmount(amount);
        inv.addItem(bought);
        member.message("§7" + amount + " " + Chat.PRIM + ItemUtils.getName(getItem()) + "§7 bought for " + Chat.PRIM + "$" + buyCost + "§7.");
        Bukkit.getPluginManager().callEvent(new PlayerItemBuyEvent(member.getPlayer(), bought, buyCost));
        return true;
    }

    @Override
    public EditComponentMenu getEditMenu(IComponentContainer container, Consumer<Player> updateStrategy) {
        return new EditCategoryItemMenu(this, container, updateStrategy);
    }

    @Override
    public String getTypeName() {
        return "Category Item";
    }

    private double getCost(int amount) {
       return amount == 1 ? getCost() : MathUtils.trim(amount * cost, 2);
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getSellValue() {
        return sellValue;
    }

    public void setSellValue(double sellValue) {
        this.sellValue = MathUtils.trim(sellValue);
    }

}
