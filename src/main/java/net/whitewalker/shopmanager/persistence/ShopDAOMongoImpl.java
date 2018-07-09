package net.whitewalker.shopmanager.persistence;

import net.rayze.core.services.MongoDB;
import net.rayze.core.spigot.menu.MenuSize;
import net.rayze.core.spigot.utils.ItemUtils;
import net.whitewalker.shopmanager.domain.components.*;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        ShopComponent component;

        String typeName = doc.getString("type_name");
        switch (typeName.toLowerCase()) {
            case "category item": {
                double cost = doc.getDouble("cost");
                component = new ShopCategoryItem(index, item, cost, doc.containsValue("sellCost") ? doc.getDouble("sellCost") : cost / 3);

                break;
            }
            case "category" : {
                component = new ShopCategory(index, item, getComponents(doc), doc.containsKey("menu_size") ? MenuSize.getSize(doc.getString("menu_size")) : MenuSize.THREE_LINE);
                break;
            }

            case "multimenu": {
                component = new ShopMultiMenu(index, item, getComponents(doc));
                break;
            }

            default: {
                double cost = doc.getDouble("cost");
                component = new ShopCategoryItem(index, item, cost, doc.containsValue("sellCost") ? doc.getDouble("sellCost") : cost / 3);
            }
        }

        if (doc.containsKey("display_name")) {
            component.withDisplayName(doc.getString("display_name"));
        }
        if (doc.containsKey("nbt")) {
            component.withNBT(doc.getString("nbt"));
        }

        return component;
    }

    private List<ShopComponent> getComponents(Document doc) {
        if (doc.containsKey("components")) {
            List<ShopComponent> subComponents = new ArrayList<>();
            for (Document compDoc : ((List<Document>) doc.get("components"))) {
                subComponents.add(getComponentFromDoc(compDoc));
            }
            return subComponents;
        }
        return new ArrayList<>();
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
        compDoc.put("type_name", comp.getTypeName());
        compDoc.put("item", ItemUtils.toDocument(comp.getItem()));
        if (!comp.getDisplayName().isEmpty()) {
            compDoc.put("display_name", comp.getDisplayName());
        }
        if (comp.getNBT() != null) {
            compDoc.put("nbt", comp.getNBT());
        }

        if (comp instanceof ShopCategory) {
            ShopCategory shopComp = (ShopCategory) comp;
            compDoc.put("menu_size", shopComp.getMenuSize().toString());
            compDoc.put("components", shopComp.getComponents().stream().map(this::componentToDoc).collect(Collectors.toList()));
            return compDoc;
        }

        if (comp instanceof ShopCategoryItem) {
            compDoc.put("cost", ((ShopCategoryItem) comp).getCost());
        }

        if (comp instanceof ShopMultiMenu) {
            ShopMultiMenu shopComp = (ShopMultiMenu) comp;
            compDoc.put("components", shopComp.getComponents().stream().map(this::componentToDoc).collect(Collectors.toList()));
            return compDoc;
        }

        return compDoc;
    }

}
