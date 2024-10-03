package com.leopoldhsing.digitalhippo.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ProductFileType {
    THUMBNAIL("THUMBNAIL"),
    CARD("CARD"),
    TABLET("TABLET");

    private final String value;
    ProductFileType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ProductFileType fromValue(String value) {
        for (ProductFileType b : ProductFileType.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        return null;
    }
}
