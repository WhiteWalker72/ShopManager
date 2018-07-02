package net.whitewalker.shopmanager.domain.commands.shop;

import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.member.Member;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class DeleteShopCommand extends SubCommand<ShopCommand> {

    private final Set<UUID> confirmSet = new HashSet<>();

    DeleteShopCommand(ShopCommand command) {
        super(command, "Delete a shop", "<shopname>", "sr_mod", "delete");
    }

    @Override
    public void execute(Member member, String label, String... args) {
        String shopName = args[0];
        if (!getCommand().shopExistsTest(member, shopName))
            return;

        UUID uuid = member.getUUID();
        if (!confirmSet.contains(uuid)) {
            member.message("§7Are you sure you want to " + Chat.PRIM + "delete §7the " + Chat.PRIM + shopName + " §7shop? All items and categories will be deleted." +
                    " Type " + Chat.PRIM + "/shop delete §7to confirm.");

            confirmSet.add(uuid);
            Bukkit.getScheduler().runTaskLater(getCommand().getPlugin(), () -> confirmSet.remove(uuid), 20 * 8);
            return;
        }

        confirmSet.remove(uuid);
        getCommand().getShopManager().deleteShop(shopName);
        member.message("§7The " + Chat.PRIM + shopName + "§7 shop got deleted.");
    }

}
