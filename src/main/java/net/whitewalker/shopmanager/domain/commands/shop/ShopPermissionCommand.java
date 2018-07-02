package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Group;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.utils.Chat;

class ShopPermissionCommand extends SubCommand<ShopCommand> {

    ShopPermissionCommand(ShopCommand command) {
        super(command, "Change the shop permission", "<shopname> <permission>", "sr_mod", "permission", "perm", "setperm", "setpermission");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        if (!getCommand().shopExistsTest(member, shopName))
            return;

        Shop shop = getCommand().getShopManager().getShop(shopName);
        String permission = args[1];

        Group group = Group.fromString(permission);
        if (group == null) {
            member.message("§7Permission '" + Chat.PRIM + permission + "§7' does not exist.");
            return;
        }

        shop.setPermission(permission);
        member.message("§7Permission updated to " + Chat.PRIM + permission + "§7.");
    }

}