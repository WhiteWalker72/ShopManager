package net.whitewalker.shopmanager.domain.components;

import java.util.List;

public interface IComponentContainer {

    List<ShopComponent> getComponents();

    void updateItems();

}
