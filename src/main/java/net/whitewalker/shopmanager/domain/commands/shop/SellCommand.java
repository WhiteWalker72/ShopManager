package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.ui.SellShop;

class SellCommand extends SubCommand<ShopCommand> {

    SellCommand(ShopCommand command) {
        super(command, "Sell some items", "[shop]", "sr_mod", "sell");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        Shop shop = null;
        if (args.length > 0) {
            String shopName = args[0];
            if (!getCommand().shopExistsTest(member, shopName)) {
                return;
            }
            shop = getCommand().getShopManager().getShop(shopName);
            if (!member.hasPermission(shop.getPermission())) {
                member.message("§7You're not allowed to sell items to this shop");
                return;
            }
        }

        new SellShop(member.getPlayer(), shop);
    }

}
