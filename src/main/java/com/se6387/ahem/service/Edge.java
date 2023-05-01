package com.se6387.ahem.service;

import lombok.Data;

/**
 * @author Jun
 * @create 2023-04-07 3:35 PM
 * @email miles.j.hoo@gmail.com
 **/

@Data
public class Edge {

    private Long from;

    private Long to;

    private Double weight;

    private Double distance_feet;



    public Edge(Long from, Long to, Double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public void setWeight(double Weight){
        this.weight = Weight;
    }

    public double getWeight(){
        return this.weight;
    }

    public void setDistance(double Distance){
        this.distance_feet = Distance;
    }

    public double getDistance(){
        return this.distance_feet;
    }
}
