package com.se6387.ahem.service;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

import java.util.List;
import java.util.Map;
import java.lang.Math;

public class EdgeWeightSetter {
    /**
     * compute edge weight based on distance and pollutant level
     * @param graph the graph, default edge weight is 1
     * @param id2NodeMap coordinate of nodes in the graph
     * @param sensitivePollutants user preferences for sensitive pollutants
     * @return new graph with computed edge weight
     */
    public Map<Long, List<Edge>> computeEdgeWeight(Map<Long, List<Edge>> graph,
                                                   Map<Long, Node> id2NodeMap,
                                                   List<Pollutant> sensitivePollutants) {
        //iterate through map where entry refers to a key value pair in the map
        for (Map.Entry<Long, List<Edge>> entry : graph.entrySet()) {

            //get starting nodes id and then the node object
            Long fromNodeId = entry.getKey();
            Node fromNode = id2NodeMap.get(fromNodeId);
            //iterate through each edge in the edge list
            for (Edge edge : entry.getValue()) {
                //get destination nodes id and then the node object
                Long toNodeId = edge.getTo();
                Node toNode = id2NodeMap.get(toNodeId);

                //get aqi values at fromNodeId
                //get aqi values at toNodeId
                //use lat long to find sensor id from Table sensor
                //use sensor id to find the aqi values

                //get edge default weight: what is it the default value based on?
                Double currentWeight = edge.getWeight();

                //calculate the distance between the two nodes in miles
                Double lat1 = fromNode.getLatitude();
                Double lat2 = toNode.getLatitude();
                Double long1 = fromNode.getLongitude();
                Double long2 = toNode.getLongitude();
                Double distanceInMiles = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1)) * 6371 / 1.6;

                //assign weight based on miles
                Double distanceWeight = 0.7 * distanceInMiles;

                //get aqi values at fromNodeId
                // Double fromNodeAQI = getAQI(lat1,long1);
                //get aqi values at toNodeId
                // Double toNodeAQI = getAQI(lat2,long2);


            }
        }
        return graph;
    }
}
