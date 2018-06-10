package net.whitewalker.shopmanager.domain.components;

import org.bukkit.entity.Player;

public interface IMoneyStrategy {

    void takeMoney(Player player, double money);

    void giveMoney(Player player, double money);

    boolean hasEnoughMoney(Player player, double money);

}
