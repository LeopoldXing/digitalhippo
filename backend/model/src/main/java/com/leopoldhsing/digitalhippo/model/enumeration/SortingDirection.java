package com.leopoldhsing.digitalhippo.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortingDirection {
    DESC("DESC"),
    ASC("ASC"),
    NONE("NONE");

    private final String value;

    SortingDirection(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SortingDirection fromValue(String value) {
        for (SortingDirection direction : SortingDirection.values()) {
            if (direction.value.equalsIgnoreCase(value)) {
                return direction;
            }
        }
        return DESC;
    }
}
