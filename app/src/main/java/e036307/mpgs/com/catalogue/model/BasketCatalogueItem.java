package e036307.mpgs.com.catalogue.model;

/**
 * Created by e036307 on 07/06/2016.
 */
public class BasketCatalogueItem{

    public CatalogueItem item;
    public int quantity;

    public BasketCatalogueItem(CatalogueItem item) {
        this.item = item;
        this.quantity = 1;
    }


    public int getValue() {
        if (null == item){
            return 0;
        }

        return item.priceMinorUnits * quantity;
    }

    public void addOne() {
        quantity++;
    }

    public void removeOne() {
        quantity--;

        if (quantity < 0){
            quantity = 0;
        }
    }
}
