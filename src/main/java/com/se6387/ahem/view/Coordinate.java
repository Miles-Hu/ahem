package com.se6387.ahem.view;

import lombok.Data;

/**
 * @author Jun
 * @create 2023-04-07 11:45 AM
 * @email miles.j.hoo@gmail.com
 **/
@Data
public class Coordinate {

    private Double longitude;

    private Double latitude;

    public Coordinate(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
