import java.util.*;

/**
 * This class contains a method that solves BDP with the barycenter heuristic.
 * @author Flor De Meulemeester
 */
public class BaryCenter {

    public static Map.Entry<Integer, List> heuristic(BipartiteGraph graph) {
        boolean improved = true;
        int oldCrossings = graph.calculateTotalCrossings();
        int newCrossings = oldCrossings;
        int crossings = oldCrossings;
        ArrayList<Integer> bestA = graph.getHeightA();
        ArrayList<Integer> bestB = graph.getHeightB();
        while (improved) {
            // first we permute the vertices in A and we lock B
            HashMap<Integer, Long> verticesA = new HashMap<>();
            for (int vertex : graph.getHeightA()) {
                verticesA.put(vertex, BaryCenter.calculateBaryCenterValue(graph, vertex, "A"));
            }
            Collections.sort(graph.getHeightA(), Comparator.comparing(verticesA::get));

            // then we permute the vertices in B and we lock A
            HashMap<Integer, Long> verticesB = new HashMap<>();
            for (int vertex : graph.getHeightB()) {
                verticesB.put(vertex, BaryCenter.calculateBaryCenterValue(graph, vertex, "B"));
            }
            Collections.sort(graph.getHeightB(), Comparator.comparing(verticesB::get));

            // check if we improved
            newCrossings = graph.calculateTotalCrossings();
            if (newCrossings < oldCrossings) {
                crossings = graph.calculateTotalCrossings();
                bestA = graph.getHeightA();
                bestB = graph.getHeightB();
            } else {
                improved = false;
            }
            oldCrossings = newCrossings;
        }

        // get the correct output format
        List<Integer> list = new ArrayList<>(bestA);
        list.addAll(bestB);
        Map.Entry<Integer, List> result = new AbstractMap.SimpleEntry<>(crossings, list);
        return result;
    }

    /**
     * Calculate the barycenter value of a vertex.
     */
    public static long calculateBaryCenterValue(BipartiteGraph graph, int vertex, String group) {
        if (graph.getNeighborsOf(vertex).isEmpty()) {
            return 1;
        }
        int average = 0;
        if (group.equals("A")) {
            for (Object neighbour : graph.getNeighborsOf(vertex)) {
                average += graph.getHeightB().indexOf(neighbour);
            }
        } else {
            for (Object neighbour : graph.getNeighborsOf(vertex)) {
                average += graph.getHeightA().indexOf(neighbour);
            }
        }
        return average / graph.getNeighborsOf(vertex).size();
    }

}
