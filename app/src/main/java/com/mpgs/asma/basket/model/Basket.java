package com.mpgs.asma.basket.model;

import com.mpgs.asma.catalogue.model.Item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the cart data model
 */
public class Basket {
    private List<BasketItemWrapper> basketItems;

    public Basket() {
        basketItems = new ArrayList<BasketItemWrapper>();
    }

    /**
     * Get the underlying data (for e.g. display in a Recycler)
     * @return
     */
    public List<BasketItemWrapper> getBasketItems() {
        return basketItems;
    }

    /**
     * Is the basket empty?
     * @return
     */
    public boolean isEmpty() {
        return (null == basketItems || basketItems.isEmpty());
    }

    /**
     * Add one of an item to the basket.
     * Quantity is incremented, with an entry being added if required.
     * @param i
     */
    public void addOneOfItem(Item i) {
        for (BasketItemWrapper item : basketItems) {
            if (item.item.equals(i)) {
                item.addOne();
                return;
            }
        }

        basketItems.add(new BasketItemWrapper(i));
    }

    /**
     * Remove one of an item from the basket.
     * Quantity is decremented / entry removed as required.
     * @param i
     */
    public void removeOneOfItem(Item i) {
        Iterator<BasketItemWrapper> iterator = basketItems.iterator();
        while (iterator.hasNext()) {
            BasketItemWrapper item = iterator.next();
            if (item.item.equals(i)) {
                item.removeOne();
                if (item.quantity == 0) {
                    iterator.remove();
                }
            }
        }
    }

    /**
     * Find the total value of all items in the basket.
     * @return
     */
    public int getValueOfAllItems() {
        int totalValue = 0;
        for (BasketItemWrapper item : basketItems) {
            totalValue += item.getValue();
        }

        return totalValue;
    }

    /**
     * Find out how many items in total are in the basket
     * @return
     */
    public int getQuantityOfAllItems() {

        int counter = 0;
        for (BasketItemWrapper item : basketItems) {
            counter += item.quantity;
        }

        return counter;
    }

    /**
     * Find out how many of a particular item there are in the basket.
     *
     * @param itemId
     * @return
     */
    public int getQuantityOfItem(String itemId) {

        for (BasketItemWrapper item : basketItems) {
            if (item.item.id.equals(itemId)) {
                return item.quantity;
            }
        }

        return 0;
    }
}
