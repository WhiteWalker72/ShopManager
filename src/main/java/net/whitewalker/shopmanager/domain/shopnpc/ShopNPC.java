package net.whitewalker.shopmanager.domain.shopnpc;

import net.rayze.core.spigot.member.Member;
import net.rayze.core.spigot.npc.NPC;
import net.whitewalker.shopmanager.domain.ShopServices;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.ui.SellShop;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class ShopNPC extends NPC {

    private final String uuidStr;
    private final String shopName;

    public ShopNPC(String uuidStr, EntityType type, Location location, String name, String shopName) {
        super(type, location, Chat.NPC_NAME + name, Chat.PRIM + "Left §7click to buy", Chat.PRIM + "Right §7click to sell" );
        this.uuidStr = uuidStr;
        this.shopName = shopName;
    }

    @Override
    public void onLeftClick(Member member) {
        if (!doInteractTest(member)) {
            return;
        }
        getShop().open(member);
    }

    @Override
    public void onRightClick(Member member) {
        if (!doInteractTest(member)) {
            return;
        }
        new SellShop(member.getPlayer(), getShop());
    }

    private boolean doInteractTest(Member member) {
        Shop shop = getShop();
        if (shop == null) {
            member.message("§7Shop '" + Chat.PRIM + shopName + "§7' doesn't exist.");
            return false;
        }
        String perm = shop.getPermission();
        if (!(perm == null || perm.equals("") || member.hasPermission(perm))) {
            member.message("§7You are §cnot allowed §7to interact with this shop.");
            return false;
        }
        return true;
    }

    private Shop getShop() {
        return ShopServices.getInstance().getShop(shopName);
    }

    public String getUUIDStr() {
        return uuidStr;
    }

    public String getShopName() {
        return shopName;
    }

}
