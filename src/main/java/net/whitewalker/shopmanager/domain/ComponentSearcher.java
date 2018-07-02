package net.whitewalker.shopmanager.domain;

import net.whitewalker.shopmanager.domain.components.IComponentContainer;
import net.whitewalker.shopmanager.domain.components.Shop;
import net.whitewalker.shopmanager.domain.components.ShopComponent;

public class ComponentSearcher<T> {

    public T findComponent(String name, Shop shop, Class<T> type) {
        for (ShopComponent component : shop.getComponents()) {
            T comp = findComponent(name, component, type);
            if (comp != null) {
                return comp;
            }
        }
        return null;
    }

    public T findComponent(String name, ShopComponent shopComponent, Class<T> type) {
        if (shopComponent.getName() != null && shopComponent.getName().equalsIgnoreCase(name) && type.isInstance(shopComponent)) {
            return (T) shopComponent;
        }

        if (shopComponent instanceof IComponentContainer) {
            for (ShopComponent component : ((IComponentContainer) shopComponent).getComponents()) {
                T comp = findComponent(name, component, type);
                if (comp != null) {
                    return comp;
                }
            }
        }
        return null;
    }


}
