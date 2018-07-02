package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.utils.StringUtils;
import net.whitewalker.shopmanager.domain.ShopManager;
import net.whitewalker.shopmanager.utils.Chat;

import java.util.ArrayList;
import java.util.List;

class CreateShopCommand extends SubCommand<ShopCommand> {

    CreateShopCommand(ShopCommand command) {
        super(command, "Create a new shop", "<shopname> <size> [permission]", "sr_mod", "create");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        ShopManager shopManager = getCommand().getShopManager();

        if (shopManager.shopExists(shopName)) {
            member.message("§7There is already a shop with this name.");
            return;
        }

        String size = args[1];
        MenuSize menuSize = MenuSize.getSize(size);

        if (menuSize == null) {
            List<String> sizeList = new ArrayList<>();
            for (MenuSize loopSize : MenuSize.values()) {
                sizeList.add(loopSize.toString().toLowerCase());
            }
            member.message("§7Size not found, possible values are: " + StringUtils.formatStringCollection(sizeList) + ".");
            return;
        }

        String permission = args.length > 2 ? args[2] : "";
        shopManager.createShop(shopName, permission, menuSize);
        member.message("§7The '" + Chat.PRIM + shopName + "§7' got created, type " + Chat.PRIM +  "/shop manage " + shopName + " §7to add shop items.");
    }

}
