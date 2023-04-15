package com.se6387.ahem.sensor;

import java.util.List;

/*
 * @author jtsmith@utdallas.edu
 */

public class AqiPolygons {

    private final List<AqiPolygon> polygons;

    public AqiPolygons(List<AqiPolygon> polygons) {
        this.polygons = polygons;
    }

    public List<AqiPolygon> getPolygons() {
        return this.polygons;
    }
}
