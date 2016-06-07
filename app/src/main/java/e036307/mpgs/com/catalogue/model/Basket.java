package e036307.mpgs.com.catalogue.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by e036307 on 02/06/2016.
 */
public class Basket {
    public List<BasketCatalogueItem> basketItems;

    public Basket() {
        basketItems = new ArrayList<BasketCatalogueItem>();
    }

    public void addItem(CatalogueItem i) {
        for (BasketCatalogueItem item : basketItems) {
            if (item.item.equals(i)) {
                item.addOne();
                return;
            }
        }

        basketItems.add (new BasketCatalogueItem(i));
    }

    public void removeItem(CatalogueItem i) {
        for (BasketCatalogueItem item : basketItems) {
            if (item.item.equals(i)) {
                item.removeOne();
                if (item.quantity == 0){
                    basketItems.remove(item);
                }
            }
        }
    }

    public int getValue() {
        int totalValue = 0;
        for (BasketCatalogueItem item : basketItems) {
            totalValue += item.getValue();
        }

        return totalValue;
    }

    public int count() {

        int counter = 0;
        for (BasketCatalogueItem item : basketItems) {
            counter += item.quantity;
        }

        return counter;
    }
    public int countItem( String itemId) {

        for (BasketCatalogueItem item : basketItems) {
            if (item.item.id.equals(itemId)){
                return item.quantity;
            }
        }

        return 0;
    }
}
