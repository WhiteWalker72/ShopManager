package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;

public class OpenShopCommand extends SubCommand<ShopCommand> {

    OpenShopCommand(ShopCommand command) {
        super(command, "Open a shop", "<shopname>", "sr_mod", "open");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        if (!getCommand().shopExistsTest(member, shopName))
            return;

        getCommand().getShopManager().getShop(shopName).open(member);
    }

}
