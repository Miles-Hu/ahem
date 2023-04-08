package com.se6387.ahem.service;


public enum Pollutant {

    PM25(1),
    PM10(2),
    O3(3),
    SO2(4),
    CO(5),
    NO2(6);

    private Integer value;

    Pollutant(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static Pollutant fromValue(Integer value) {
        for (Pollutant pollutant : Pollutant.values()) {
            if (pollutant.getValue().equals(value)) {
                return pollutant;
            }
        }
        return null;
    }
}
