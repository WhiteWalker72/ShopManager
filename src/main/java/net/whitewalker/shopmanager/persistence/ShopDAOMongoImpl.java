package net.whitewalker.shopmanager.persistence;

import net.rayze.core.services.MongoDB;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.utils.ItemUtils;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.components.ShopCategory;
import net.whitewalker.shopmanager.domain.components.ShopCategoryItem;
import net.whitewalker.shopmanager.domain.components.ShopComponent;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopDAOMongoImpl extends DAOMongoImpl<Shop> {

    ShopDAOMongoImpl(MongoDB database, String collectionName) {
        super(database.getCollection(collectionName));
    }

    @Override
    public String getDocIdentifier() {
        return "name";
    }

    @Override
    public String getIdentifier(Shop dto) {
        return dto.getShopName();
    }

    @Override
    public Shop docToDto(Document doc) {
        return new Shop(doc.getString("name"), doc.containsKey("perm") ? doc.getString("perm") : "", MenuSize.getSize(doc.getString("menu_size")), getShopComponents(doc));
    }
    
    private List<ShopComponent> getShopComponents(Document doc) {
        if (!doc.containsKey("components")) 
            return new ArrayList<>();
        List<ShopComponent> shopComponents = new ArrayList<>();
        
        List<Document> components = (List<Document>) doc.get("components");
        for (Document compDoc : components) {
            shopComponents.add(getComponentFromDoc(compDoc));
        }
        return shopComponents;
    }

    private ShopComponent getComponentFromDoc(Document doc) {
        int index = doc.getInteger("index");
        ItemStack item = ItemUtils.fromDocument((Document) doc.get("item"));

        if (doc.containsKey("components")) {
            List<ShopComponent> subComponents = new ArrayList<>();
            for (Document compDoc : ((List<Document>) doc.get("components"))) {
                subComponents.add(getComponentFromDoc(compDoc));
            }
            return new ShopCategory(index, item, subComponents, doc.containsKey("menu_size") ? MenuSize.getSize(doc.getString("menu_size")) : MenuSize.THREE_LINE);
        }
        double cost = doc.getDouble("cost");
        return new ShopCategoryItem(index, item, cost, doc.containsValue("sellCost") ? doc.getDouble("sellCost") : cost/3);
    }

    @Override
    public Document dtoToDoc(Shop dto) {
        Document doc = new Document("name", dto.getShopName());
        if (dto.getPermission() != null) {
            doc.put("perm", dto.getPermission());
        }

        List<Document> componentDocs = new ArrayList<>();
        for (ShopComponent shopComponent : dto.getComponents()) {
            componentDocs.add(componentToDoc(shopComponent));
        }
        doc.put("menu_size", dto.getMenuSize().toString());

        doc.put("components", componentDocs);
        return doc;
    }

    private Document componentToDoc(ShopComponent comp) {
        Document compDoc = new Document("index", comp.getIndex());
        compDoc.put("item", ItemUtils.toDocument(comp.getItem()));

        if (comp instanceof ShopCategory) {
            ShopCategory shopComp = (ShopCategory) comp;
            compDoc.put("menu_size", shopComp.getMenuSize().toString());

            List<Document> components = new ArrayList<>();
            shopComp.getComponents().forEach(item -> components.add(componentToDoc(item)));
            compDoc.put("components", components);
            return compDoc;
        }

        if (comp instanceof ShopCategoryItem) {
            compDoc.put("cost", ((ShopCategoryItem) comp).getCost());
        }
        return compDoc;
    }

}
