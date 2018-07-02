package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.ShopManager;
import net.whitewalker.shopmanager.utils.Chat;

class RenameShopCommand extends SubCommand<ShopCommand> {

    RenameShopCommand(ShopCommand command) {
        super(command, "Rename a shop", "<shop> <newname>", "sr_mod", "rename");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        if (!getCommand().shopExistsTest(member, shopName))
            return;
        ShopManager shopManager = getCommand().getShopManager();

        String newName = args[1];
        Shop shop = shopManager.getShop(shopName);
        if (shopManager.shopExists(newName)) {
            member.message("§7The '" + Chat.PRIM + newName + "§7' shop already exists.");
            return;
        }

        shop.setShopName(newName);
        getCommand().getPlugin().getPersistenceService().updateShop(shop);
        member.message("§7The '" + shopName + "' shop has been renamed to " + Chat.PRIM + newName + "§7.");
    }

}
