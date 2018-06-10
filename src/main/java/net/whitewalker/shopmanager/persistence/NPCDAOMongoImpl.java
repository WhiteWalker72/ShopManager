package net.whitewalker.shopmanager.persistence;

import net.rayze.core.services.MongoDB;
import net.rayze.core.utils.MongoUtils;
import net.whitewalker.shopmanager.domain.shopnpc.ShopNPC;
import org.bson.Document;
import org.bukkit.entity.EntityType;

public class NPCDAOMongoImpl extends DAOMongoImpl<ShopNPC> {

    NPCDAOMongoImpl(MongoDB database, String collectionName) {
        super(database.getCollection(collectionName));
    }

    @Override
    public String getDocIdentifier() {
        return "uuid";
    }

    @Override
    public String getIdentifier(ShopNPC dto) {
        return dto.getUUIDStr();
    }

    @Override
    public ShopNPC docToDto(Document doc) {
        return new ShopNPC(
                doc.getString("uuid")
                , EntityType.valueOf(doc.getString("entity_type"))
                , MongoUtils.getLocationFromPath(doc, "location")
                , doc.getString("name")
                , doc.getString("shopName")
        );
    }

    @Override
    public Document dtoToDoc(ShopNPC dto) {
        Document doc = new Document();
        doc.put("uuid", dto.getUUIDStr());
        doc.put("entity_type", dto.getEntity().getType().toString());
        doc.put("location", MongoUtils.locationToDoc(dto.getLocation()));
        doc.put("name", dto.getName());
        doc.put("shopName", dto.getShopName());
        return doc;
    }

}
