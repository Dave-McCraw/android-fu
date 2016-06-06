package e036307.mpgs.com.catalogue.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by e036307 on 02/06/2016.
 */
public class Basket {
    public List<CatalogueItem> basketItems;
    public Collection<BasketListener<CatalogueItem>> basketListeners;

    public void Basket() {
        basketItems =  new ArrayList<CatalogueItem>();
    }

    public void addListener(BasketListener<CatalogueItem> l){
        if (null == basketListeners){
            basketListeners =  new ArrayList<BasketListener<CatalogueItem>>();
        }

        basketListeners.add(l);

    }

    public void addItem(CatalogueItem i){
        if (null == basketItems){
            basketItems =  new ArrayList<CatalogueItem>();
        }

        basketItems.add(i);

        if (null != basketListeners){
            for (BasketListener<CatalogueItem> l : basketListeners){
                l.notify(this.basketItems);
            }
        }

    }

    public int getValue(){
        int totalValue = 0;
        for (CatalogueItem item : basketItems){
            totalValue+= item.priceMinorUnits;
        }

        return totalValue;
    }
}
