package net.whitewalker.shopmanager.domain.commands.npc;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPC;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class DeleteNPCCommand extends SubCommand<ShopNPCCommand> {

    private final Set<UUID> confirmSet = new HashSet<>();

    DeleteNPCCommand(ShopNPCCommand command) {
        super(command, "Delete the nearest npc", "", "sr_mod", "delete");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        Location loc = member.getPlayer().getLocation();
        ShopNPC npc = getCommand().getShopNPCFacade().findNearest(loc);

        if (npc == null) {
            member.message("§7No npc found.");
            return;
        }
        if (npc.getLocation().distance(loc) > 100) {
            int distance = (int) npc.getLocation().distance(loc);
            UUID uuid = member.getUUID();
            if (!confirmSet.contains(uuid)) {
                member.message("§7Are you sure you want to delete the " + Chat.PRIM + npc.getName() + "§7 npc? It's " + Chat.PRIM + distance + "§7 blocks away from you.");
                confirmSet.add(uuid);
                return;
            }
            confirmSet.remove(uuid);
        }
        String name = npc.getName();
        getCommand().getShopNPCFacade().deleteNpc(npc);
        npc.destroy();
        member.message(Chat.PRIM + name + "§7 npc got deleted.");
    }

}
