package com.se6387.ahem.service;

import com.se6387.ahem.sensor.AqiMeasurement;
import com.se6387.ahem.sensor.AqiPoint;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.lang.Math;
@Service
public class EdgeWeightSetter {

    @Autowired
    private PollutantService pollutantService;

    /**
     * compute edge weight based on distance and pollutant level
     * @param graph the graph, default edge weight is 1
     * @param id2NodeMap coordinate of nodes in the graph
     * @param sensitivePollutants user preferences for sensitive pollutants
     * @return new graph with computed edge weight
     */
    public Map<Long, List<Edge>> computeEdgeWeight(Map<Long, List<Edge>> graph,
                                                   Map<Long, Node> id2NodeMap,
                                                   List<PollutantEnum> sensitivePollutants) {
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
                double distanceInMiles = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1)) * 6371 / 1.6;




                //get aqi values at fromNodeId
                AqiPoint fromNodeAQI = pollutantService.getAqiPoint(lat1,long1);
                List<AqiMeasurement> fromNodeAQIValues = fromNodeAQI.getMeasurements();

                //finding the max AQI value in the list of aqi values at from Node
                int fromMaxAQI = 0;
                for (AqiMeasurement aqiValue: fromNodeAQIValues){
                    int temp =aqiValue.getValue();
                    if( temp >fromMaxAQI){
                        fromMaxAQI = temp;
                    }
                }

                //get aqi values at toNodeId
                AqiPoint toNodeAQI = pollutantService.getAqiPoint(lat2,long2);
                List<AqiMeasurement> toNodeAQIValues = toNodeAQI.getMeasurements();

                //finding the max AQI value in the list of aqi values at to Node
                int toMaxAQI = 0;
                for (AqiMeasurement aqiValue: toNodeAQIValues){
                    int temp =aqiValue.getValue();
                    if( temp >toMaxAQI){
                        toMaxAQI = temp;
                    }
                }

                //computing edge weight function
                double AQI_weight = (fromMaxAQI + toMaxAQI)/2;

                //set edge distance in feet
                edge.setDistance_feet(distanceInMiles*5280);

                Double edgeWeight = 0.3*distanceInMiles + 0.7*AQI_weight;

                edge.setWeight(edgeWeight);
            }
        }
        return graph;
    }
}
