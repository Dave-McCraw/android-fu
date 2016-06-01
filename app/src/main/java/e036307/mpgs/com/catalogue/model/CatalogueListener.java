package e036307.mpgs.com.catalogue.model;

import java.util.List;

/**
 * Created by e036307 on 01/06/2016.
 */
public interface CatalogueListener<T> {

    void onTaskComplete(List<T> s);
}
