package e036307.mpgs.com.catalogue.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by e036307 on 02/06/2016.
 */
public class Basket<T> {
    public List<T> basketItems;
    public Collection<BasketListener<T>> basketListeners;

    public void addListener(BasketListener<T> l){
        if (null == basketListeners){
            basketListeners =  new ArrayList<BasketListener<T>>();
        }

        basketListeners.add(l);

    }

    public void addItem(T i){
        if (null == basketItems){
            basketItems =  new ArrayList<T>();
        }

        basketItems.add(i);

        if (null != basketListeners){
            for (BasketListener<T> l : basketListeners){
                l.notify(this.basketItems);
            }
        }

    }
}
