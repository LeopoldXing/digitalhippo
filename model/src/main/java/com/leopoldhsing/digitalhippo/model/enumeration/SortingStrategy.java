package com.leopoldhsing.digitalhippo.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SortingStrategy {
    CREATED_TIMESTAMP("CREATED_TIMETAMP"),
    POPULARITY("POPULARITY"),
    RELEVANCE("RELEVANCE");

    private final String value;
    SortingStrategy(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static SortingStrategy fromValue(String value) {
        for (SortingStrategy sortingStrategy : SortingStrategy.values()) {
            if (sortingStrategy.getValue().equalsIgnoreCase(value)) {
                return sortingStrategy;
            }
        }
        return RELEVANCE;
    }
}
