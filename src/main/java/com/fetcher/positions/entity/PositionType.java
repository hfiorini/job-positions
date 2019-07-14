package com.fetcher.positions.entity;

public enum PositionType {
    CONTRACT("Contract"),
    FULL_TIME("Full Time"),
    PART_TIME("Part Time");

    private String value;

    PositionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static PositionType fromString(String value) {
        for (PositionType positionType : PositionType.values()) {
            if (positionType.value.equalsIgnoreCase(value)) {
                return positionType;
            }
        }
        return null;
    }
}
