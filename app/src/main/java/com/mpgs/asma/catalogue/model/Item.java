package com.mpgs.asma.catalogue.model;

/**
 * Represents an item of commerce
 */
public class Item {
    public final String id;
    public final String content;
    public final String details;
    public final String thumbnailSource;
    public final int priceMinorUnits;

    public Item(String id, String content, String thumbnailSource, String details, int priceMinorUnits) {
        this.id = id;
        this.content = content;
        this.details = details;
        this.thumbnailSource = thumbnailSource;
        this.priceMinorUnits = priceMinorUnits;
    }

    @Override
    public String toString() {
        return "Item '"+content+"'";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (priceMinorUnits != item.priceMinorUnits) return false;
        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (content != null ? !content.equals(item.content) : item.content != null) return false;
        if (details != null ? !details.equals(item.details) : item.details != null) return false;
        return thumbnailSource != null ? thumbnailSource.equals(item.thumbnailSource) : item.thumbnailSource == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (thumbnailSource != null ? thumbnailSource.hashCode() : 0);
        result = 31 * result + priceMinorUnits;
        return result;
    }
}
