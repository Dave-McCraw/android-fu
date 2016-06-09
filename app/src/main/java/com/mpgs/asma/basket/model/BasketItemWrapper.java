package com.mpgs.asma.basket.model;

import com.mpgs.asma.catalogue.model.Item;

/**
 * This class wraps particular items, allowing for multiple to be in the basket.
 * TODO: feels like this is missing a good rework using fundamental OO
 */
public class BasketItemWrapper {

    public Item item;
    public int quantity;

    public BasketItemWrapper(Item item) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasketItemWrapper that = (BasketItemWrapper) o;

        if (quantity != that.quantity) return false;
        return item != null ? item.equals(that.item) : that.item == null;

    }

    @Override
    public int hashCode() {
        int result = item != null ? item.hashCode() : 0;
        result = 31 * result + quantity;
        return result;
    }
}
