package net.whitewalker.shopmanager.persistence;

import net.rayze.core.services.MongoDB;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPC;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class PersistenceService {

    private final FileConfiguration config;
    private final DAO<Shop> shopDAO;
    private final DAO<ShopNPC> shopNpcDAO;

    public PersistenceService(FileConfiguration config) {
        this.config = config;
        MongoDB database = new MlabDatabase(config);
        shopDAO = new ShopDAOMongoImpl(database, config.getString("mongo.shop_collection"));
        shopNpcDAO = new NPCDAOMongoImpl(database, config.getString("mongo.npc_collection"));
    }

    public List<Shop> findAllShops() {
        return shopDAO.findAll();
    }

    public Shop findShop(String identifier) {
        return shopDAO.findById(identifier);
    }

    public boolean insertShop(Shop dto) {
        return shopDAO.insert(dto);
    }

    public boolean updateShop(Shop dto) {
        return shopDAO.update(dto);
    }

    public boolean deleteShop(Shop dto) {
        return shopDAO.delete(dto);
    }

    public List<ShopNPC> findAllShopNPCs() {
        return shopNpcDAO.findAll();
    }

    public ShopNPC findNPC(String identifier) {
        return shopNpcDAO.findById(identifier);
    }

    public boolean insertNPC(ShopNPC dto) {
        return shopNpcDAO.insert(dto);
    }

    public boolean updateNPC(ShopNPC dto) {
        return shopNpcDAO.update(dto);
    }

    public boolean deleteNPC(ShopNPC dto) {
        return shopNpcDAO.delete(dto);
    }

}
