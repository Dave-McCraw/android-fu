package e036307.mpgs.com.catalogue.model;

/**
 * Created by e036307 on 01/06/2016.
 */
public class CatalogueItem {
    public final String id;
    public final String content;
    public final String details;
    public final String detailHeader;
    public final String thumbnailSource;
    public final int priceMinorUnits;

    public CatalogueItem(String id, String content, String thumbnailSource, String details, String detailHeader, int priceMinorUnits) {
        this.id = id;
        this.content = content;
        this.details = details;
        this.thumbnailSource = thumbnailSource;
        this.priceMinorUnits = priceMinorUnits;
        this.detailHeader = detailHeader;
    }

    @Override
    public String toString() {
        return content;
    }
}
