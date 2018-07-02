package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.ComponentSearcher;
import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import net.whitewalker.shopmanager.utils.Chat;

class ShopRemoveItemsCommand extends SubCommand<ShopCommand> {

    ShopRemoveItemsCommand(ShopCommand command) {
        super(command, "Remove all items from a shop category", "<shop> <category>", "dev", "removeitems");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        Shop shop = getCommand().getShopManager().getShop(shopName);

        if (shop == null) {
            getCommand().couldNotFind(member, "shop", shopName);
            return;
        }

        String categoryName = args[1];
        IComponentContainer container = new ComponentSearcher<IComponentContainer>().findComponent(categoryName.replaceAll("_", " "), shop, IComponentContainer.class);
        if (container == null) {
            getCommand().couldNotFind(member, "category", categoryName);
            return;
        }

        container.getComponents().clear();
        container.updateItems();
        member.message("§7All " + Chat.PRIM + categoryName + "§7 items have been removed from the " + Chat.PRIM + shopName + "§7 shop.");
    }

    private IComponentContainer findContainer(String name, Shop shop) {
        for (ShopComponent component : shop.getComponents()) {
            IComponentContainer comp = findContainer(name, component);
            if (comp != null) {
                return comp;
            }
        }
        return null;
    }

    private IComponentContainer findContainer(String name, ShopComponent shopComponent) {
        if (shopComponent.getName() != null && shopComponent.getName().equals(name) && shopComponent instanceof IComponentContainer) {
            return (IComponentContainer) shopComponent;
        }

        if (shopComponent instanceof IComponentContainer) {
            for (ShopComponent component : ((IComponentContainer) shopComponent).getComponents()) {
                IComponentContainer comp = findContainer(name, component);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }

}
