package com.se6387.ahem.sensor;

/*
 * @author jtsmith@utdallas.edu
 */

import java.util.List;

public class AqiPolygon {

    private final double northernLatitude;
    private final double southernLatitude;
    private final double westernLongitude;
    private final double easternLongitude;
    private final List<AqiMeasurement> measurements;

    public AqiPolygon(double northernLatitude, double southernLatitude, double westernLongitude,
                      double easternLatitude, List<AqiMeasurement> measurements) {
        this.northernLatitude = northernLatitude;
        this.southernLatitude = southernLatitude;
        this.westernLongitude = westernLongitude;
        this.easternLongitude = easternLatitude;
        this.measurements = measurements;
    }
    public double getNorthernLatitude() {
        return this.northernLatitude;
    }

    public double getSouthernLatitude() {
        return this.southernLatitude;
    }

    public double getWesternLongitude() {
        return this.westernLongitude;
    }

    public double getEasternLongitude() {
        return this.easternLongitude;
    }

    public List<AqiMeasurement> getMeasurements() {
        return this.measurements;
    }
}
