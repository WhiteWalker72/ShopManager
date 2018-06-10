package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.rayze.core.utils.StringUtils;
import net.whitewalker.shopmanager.domain.components.Shop;

import java.util.List;
import java.util.stream.Collectors;

public class ShopListCommand extends SubCommand<ShopCommand> {

    ShopListCommand(ShopCommand command) {
        super(command, "Get a list of all the shops", "", "sr_mod", "list");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        List<String> shopNames = getCommand().getShopManager().getAllShops().stream().map(Shop::getShopName).collect(Collectors.toList());
        if (shopNames.isEmpty()) {
            member.message("§7There are no shops on this server.");
            return;
        }
        member.message("§7The names of the shops are: " + StringUtils.formatStringCollection(shopNames));
    }

}
