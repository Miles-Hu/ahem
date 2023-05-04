package com.se6387.ahem.service;

import com.se6387.ahem.model.Edge;

import java.util.*;

public class ShortestPathFinder {

    public static List<Long> findShortestPath(Map<Long, List<Edge>> graph, long startPoint, long endPoint) {
        Map<Long, Double> distMap = new HashMap<>(); // To keep track of distances from the startPoint
        Map<Long, Long> parentMap = new HashMap<>(); // To keep track of parent vertices in the shortest path
        Set<Long> visited = new HashSet<>(); // To keep track of visited vertices
        PriorityQueue<Long> pq = new PriorityQueue<>(Comparator.comparingDouble(distMap::get)); // To store vertices to visit sorted by distance

        // Initialize the distMap with infinite distances and the startPoint with distance 0
        for (long vertex : graph.keySet()) {
            distMap.put(vertex, Double.MAX_VALUE);
        }
        distMap.put(startPoint, 0d);
        pq.add(startPoint);

        // Run Dijkstra's algorithm
        while (!pq.isEmpty()) {
            long current = pq.poll();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            for (Edge neighborEdge : graph.getOrDefault(current, Collections.emptyList())) {
                double altDist = distMap.get(current) + neighborEdge.getWeight(); // Assume all edges have weight 1
                if (altDist < distMap.get(neighborEdge.getTo())) {
                    distMap.put(neighborEdge.getTo(), altDist);
                    parentMap.put(neighborEdge.getTo(), current); // Add the current vertex as the parent of the neighbor in the shortest path
                    pq.add(neighborEdge.getTo());
                }
            }
        }

        // Reconstruct the shortest path from the endPoint to the startPoint
        List<Long> path = new ArrayList<>();
        long current = endPoint;
        while (current != startPoint) {
            path.add(current);
            current = parentMap.get(current);
            System.out.println(distMap.get(current) - distMap.getOrDefault(parentMap.get(current), 0d));
        }
        path.add(startPoint);
        Collections.reverse(path);

        // Return the shortest path (or null if no path exists)
        return distMap.get(endPoint) == Long.MAX_VALUE ? null : path;
    }

}
