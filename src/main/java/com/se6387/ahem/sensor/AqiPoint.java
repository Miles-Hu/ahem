package com.se6387.ahem.sensor;


import java.util.List;

/*
 * @author jtsmith@utdallas.edu
 */

public class AqiPoint {

    private final double longitude;
    private final double latitude;
    private final List<AqiMeasurement> measurements;

    public AqiPoint(double longitude, double latitude, List<AqiMeasurement> measurements) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.measurements = measurements;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public List<AqiMeasurement> getMeasurements() {
        return this.measurements;
    }
}
