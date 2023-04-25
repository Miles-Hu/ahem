package com.se6387.ahem.service;

import com.se6387.ahem.view.Coordinate;
import crosby.binary.osmosis.OsmosisReader;
import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MyOsmReader implements Sink {

    @Autowired
    private EdgeWeightSetter edgeWeightSetter;

    /**
     * the graph representing the OSM data
     */
    static Map<Long, List<Edge>> graph = new HashMap<>();
    /**
     * nodeId to node, the longitude and latitude saved in the node
     */
    static Map<Long, Node> id2NodeMap = new HashMap<>();

    static Map<List<Integer>, Map<Long, List<Edge>>> cachedGraph = new HashMap<>();

    static List<Node> nodes = new ArrayList<>();
 
    @Override
    public void initialize(Map<String, Object> arg0) {
    }
 
    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer) {
            nodes.add((Node) entityContainer.getEntity());
        } else if (entityContainer instanceof WayContainer) {
            Way myWay = ((WayContainer) entityContainer).getEntity();
            for (Tag myTag : myWay.getTags()) {
                if ("highway".equalsIgnoreCase(myTag.getKey())) {
                    List<WayNode> wayNodes = myWay.getWayNodes();
                    int size = wayNodes.size() - 1;
                    for (int i = 0; i < size; i++) {
                        WayNode wayNode1 = wayNodes.get(i);
                        WayNode wayNode2 = wayNodes.get(i + 1);
                        Edge edge1 = new Edge(wayNode1.getNodeId(), wayNode2.getNodeId(), 1d);
                        Edge edge2 = new Edge(wayNode2.getNodeId(), wayNode1.getNodeId(), 1d);
                        graph.computeIfAbsent(wayNode1.getNodeId(), e -> new ArrayList<>()).add(edge1);
                        graph.computeIfAbsent(wayNode2.getNodeId(), e -> new ArrayList<>()).add(edge2);
                    }
/*                    System.out.println(" Woha, it's a highway: " + myWay.getId());
                    System.out.println(myWay.getWayNodes());*/
                    break;
                }
            }
        } else if (entityContainer instanceof RelationContainer) {
            // Nothing to do here
        } else {
            System.out.println("Unknown Entity!");
        }
    }
 
    @Override
    public void complete() {
    }
 
    @Override
    public void close() {
    }

    @PostConstruct
    public void init() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("src/UTD_area.osm.pbf");
        OsmosisReader reader = new OsmosisReader(inputStream);
        reader.setSink(new MyOsmReader());
        reader.run();

        nodes = nodes.stream().filter(node -> graph.containsKey(node.getId())).collect(Collectors.toList());
        for (Node node : nodes) {
            id2NodeMap.put(node.getId(), node);
        }
        refreshGraph();
    }

    private void refreshGraph() {
        graph = computeEdgeWeight(graph, id2NodeMap, null);
        cacheGraph();
    }

    private void cacheGraph() {
        List<Integer> arrayList = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<List<Integer>> combinations = new ArrayList<>();
        for (int i = 1; i <= arrayList.size(); i++) {
            combinations.addAll(getCombinations(arrayList, i));
        }
        for (List<Integer> combination : combinations) {
            Collections.sort(combination);
            List<Pollutant> list = new ArrayList<>();
            for (Integer integer : combination) {
                list.add(Pollutant.fromValue(integer));
            }
            Map<Long, List<Edge>> newGraph = computeEdgeWeight(graph, id2NodeMap, list);
            cachedGraph.put(combination, newGraph);
        }
    }

    private Map<Long, List<Edge>> getGraph(List<Integer> sensitivePollutants) {
        if (cachedGraph.containsKey(sensitivePollutants)) {
            return cachedGraph.get(sensitivePollutants);
        }
        return graph;
    }
 
    public List<Coordinate> route(Coordinate startPoint,
                                  Coordinate endPoint,
                                  List<Integer> sensitivePollutants){
        // double[] sP = {-96.7533417, 32.9950066}, eP = {-96.7504163, 32.9862204};
        double[] sP = {startPoint.getLongitude(), startPoint.getLatitude()}, eP = {endPoint.getLongitude(), endPoint.getLatitude()};
        Map<Long, List<Edge>> newGraph = getGraph(sensitivePollutants);
        double loDis1 = 10000, laDis1 = 10000;
        double loDis2 = 10000, laDis2 = 10000;
        Node sNode = null, eNode = null;
        for (Node node : id2NodeMap.values()) {
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
            double longitude = id2NodeMap.get(node).getLongitude();
            double latitude = id2NodeMap.get(node).getLatitude();
            sb.append(longitude + ", " + latitude).append("\n");
            result.add(new Coordinate(longitude, latitude));
        }
        System.out.println(sb);
        return result;
    }

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
        return graph;
    }

    public static List<List<Integer>> getCombinations(List<Integer> elements, int k) {
        List<List<Integer>> combinations = new ArrayList<>();

        // Base case: if k is zero, there is only one combination, which is an empty list.
        if (k == 0) {
            combinations.add(new ArrayList<>());
            return combinations;
        }

        // Base case: if k is greater than the size of the elements list, there are no valid combinations.
        if (k > elements.size()) {
            return combinations;
        }

        // Get the first element and the remaining elements.
        Integer firstElement = elements.get(0);
        List<Integer> remainingElements = elements.subList(1, elements.size());

        // Recursively compute the combinations of the remaining elements with k-1 elements.
        List<List<Integer>> subCombinations = getCombinations(remainingElements, k - 1);

        // Add the first element to each of the sub-combinations.
        for (List<Integer> subCombination : subCombinations) {
            List<Integer> combination = new ArrayList<>(subCombination);
            combination.add(0, firstElement);
            combinations.add(combination);
        }

        // Recursively compute the combinations of the remaining elements with k elements.
        subCombinations = getCombinations(remainingElements, k);

        // Add the sub-combinations to the result.
        combinations.addAll(subCombinations);

        return combinations;
    }



}