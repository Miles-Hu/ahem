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

    public Edge(Long from, Long to, Double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}
