package net.whitewalker.shopmanager.domain.commands.npc;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

class CreateNPCCommand extends SubCommand<ShopNPCCommand> {

    CreateNPCCommand(ShopNPCCommand command) {
        super(command, "Set a shop npc at your location", "<npcname> <shopname> <entitytype>", "sr_mod", "create", "createnpc");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String npcName = args[0];
        if (npcName.length() > 12) {
            member.message(Chat.PRIM + npcName + "§7 is too long.");
            return;
        }

        String shopName = args[1];
        Shop shop = getCommand().getShopManager().getShop(shopName);
        if (shop == null) {
            member.message("§7There is no shop called '" + Chat.PRIM + shopName + "§7' type " + Chat.PRIM + "/shop list §7to view all shop names." );
            return;
        }
        Location loc = member.getPlayer().getLocation();
        if (getCommand().getShopNPCFacade().findNPC(loc )!= null) {
            member.message("§7There is already an npc at this location.");
            return;
        }

        String entityTypeStr = args[2];
        EntityType entityType = getEntityType(entityTypeStr);
        if (entityType == null) {
            member.message("§7No entity type found for input " + Chat.PRIM  + entityTypeStr);
            return;
        }
        if (!entityType.isSpawnable() || !entityType.isAlive()) {
            member.message("§7Entity type: " + Chat.PRIM + entityType.toString() + " §7isn't spawnable.");
            return;
        }

        getCommand().getShopNPCFacade().createNpc(npcName, loc, entityType, shop.getShopName());
    }

    private EntityType getEntityType(String type) {
        for (EntityType entityType : EntityType.values()) {
            if (type.toLowerCase().equals(entityType.toString().replaceAll("_", "").toLowerCase())) {
                return entityType;
            }
        }
        return null;
    }

}
