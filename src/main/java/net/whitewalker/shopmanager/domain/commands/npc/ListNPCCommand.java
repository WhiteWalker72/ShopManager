package net.whitewalker.shopmanager.domain.commands.npc;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.rayze.core.spigot.commands.base.SubCommand;
import net.rayze.core.spigot.managers.TextPageManager;
import net.rayze.core.spigot.member.Member;
import net.rayze.core.utils.MathUtils;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPC;
import net.whitewalker.shopmanager.utils.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

class ListNPCCommand extends SubCommand<ShopNPCCommand> {

    private final JavaPlugin plugin;
    private final Map<UUID, TextPageManager> pageManagerMap = new HashMap<>();

    ListNPCCommand(ShopNPCCommand command, JavaPlugin plugin) {
        super(command, "Get a list of all the created NPCs", "[page]", "sr_mod", "list");
        this.plugin = plugin;
    }

    @Override
    public void execute(Member member, String label, String... args) {
        Set<ShopNPC> shopNPCSet = getCommand().getShopNPCFacade().getAllShopNPCS();
        if (shopNPCSet.isEmpty()) {
            member.message("§7There are no shop NPCs created.");
            return;
        }
        int page = args.length > 1 ? MathUtils.parseInt(args[1]).visit(() -> 1, x -> x) : 1;


        UUID uuid = member.getUUID();
        if (pageManagerMap.containsKey(uuid)) {
            pageManagerMap.get(uuid).sendToPlayer(member.getPlayer(), page);
            return;
        }

        TextPageManager pageManager = new TextPageManager(Chat.PRIM + "NPC list", "/shopnpc list", getPageElements(shopNPCSet), 9, 6);
        pageManagerMap.put(uuid, pageManager);
        Bukkit.getScheduler().runTaskLater(plugin, () -> pageManagerMap.remove(uuid), 120);

        pageManager.sendToPlayer(member.getPlayer(), page);
    }

    private List<BaseComponent[]> getPageElements(Set<ShopNPC> shopNPCSet) {
        List<BaseComponent[]> elements = new ArrayList<>();
        for (ShopNPC shopNPC : shopNPCSet) {
            Location loc = shopNPC.getLocation();
            String locStr = "world: " + loc.getWorld().getName() + " coords: " + (int) loc.getX() + ", " + (int) loc.getY() + ", " + (int) loc.getZ();
            elements.add(TextComponent.fromLegacyText(Chat.PRIM + shopNPC.getName() + " §7" + shopNPC.getEntity().getName() + " " + locStr + "."));
        }
        return elements;
    }

}
