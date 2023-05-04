package com.se6387.ahem.model;


public enum PollutantEnum {

    PM25(1),
    PM10(2),
    O3(3),
    SO2(4),
    CO(5),
    NO2(6);

    private Integer value;

    PollutantEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static PollutantEnum fromValue(Integer value) {
        for (PollutantEnum pollutant : PollutantEnum.values()) {
            if (pollutant.getValue().equals(value)) {
                return pollutant;
            }
        }
        return null;
    }
}
