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
    public Map<String,BasketCatalogueItem> basketItems;

    public Basket() {
        basketItems = new HashMap<String, BasketCatalogueItem>();
    }

    public void addItem(CatalogueItem i) {
        if (basketItems.containsKey(i.id)){
            basketItems.get(i.id).addOne();
            return;
        }

        basketItems.put (i.id, new BasketCatalogueItem(i));
    }

    public void removeItem(CatalogueItem i) {
        if (basketItems.containsKey(i.id)){
            basketItems.get(i.id).removeOne();
            if (basketItems.get(i.id).quantity == 0){
                basketItems.remove(i.id);
            }
        }
    }

    public int getValue() {
        int totalValue = 0;
        for (String id : basketItems.keySet()) {
            totalValue += basketItems.get(id).getValue();
        }

        return totalValue;
    }

    public int count() {
        int counter = 0;
        for (String id : basketItems.keySet()) {
            counter += basketItems.get(id).quantity;
        }

        return counter;
    }
}
