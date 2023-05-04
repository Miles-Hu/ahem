package com.se6387.ahem.service;

import com.se6387.ahem.model.Edge;
import com.se6387.ahem.view.Coordinate;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jun
 * @create 2023-04-07 11:37 AM
 * @email miles.j.hoo@gmail.com
 **/

@Service
public class RouteService {

    @Autowired
    private OsmReader myOsmReader;

    public List<Coordinate> route(Coordinate start, Coordinate end, List<Integer> sensitivePollutants) {
        // double[] sP = {-96.7533417, 32.9950066}, eP = {-96.7504163, 32.9862204};
        double[] sP = {start.getLongitude(), start.getLatitude()}, eP = {end.getLongitude(), end.getLatitude()};
        Map<Long, List<Edge>> newGraph = myOsmReader.getGraph(sensitivePollutants);
        double loDis1 = 10000, laDis1 = 10000;
        double loDis2 = 10000, laDis2 = 10000;
        Node sNode = null, eNode = null;
        for (Node node : myOsmReader.id2NodeMap.values()) {
            if (Math.abs(node.getLongitude() - sP[0]) + Math.abs(node.getLatitude() - sP[1]) < (loDis1 + laDis1)) {
                loDis1 = Math.abs(node.getLongitude() - sP[0]);
                laDis1 = Math.abs(node.getLatitude() - sP[1]);
                sNode = node;
            }
            if (Math.abs(node.getLongitude() - eP[0]) + Math.abs(node.getLatitude() - eP[1]) < (loDis2 + laDis2)) {
                loDis2 = Math.abs(node.getLongitude() - eP[0]);
                laDis2 = Math.abs(node.getLatitude() - eP[1]);
                eNode = node;
            }
        }
        System.out.println(sNode.getLongitude() + ", " + sNode.getLatitude());
        System.out.println(eNode.getLongitude() + ", " + eNode.getLatitude());
        System.out.println();
        List<Long> path = ShortestPathFinder.findShortestPath(newGraph, sNode.getId(), eNode.getId());
        StringBuilder sb = new StringBuilder();
        List<Coordinate> result = new ArrayList<>();
        for (Long node : path) {
            double longitude = myOsmReader.id2NodeMap.get(node).getLongitude();
            double latitude = myOsmReader.id2NodeMap.get(node).getLatitude();
            sb.append(longitude + ", " + latitude).append("\n");
            result.add(new Coordinate(longitude, latitude));
        }
        System.out.println(sb);
        return result;
    }

}
