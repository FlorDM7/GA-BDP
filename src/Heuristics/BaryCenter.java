package Heuristics;

import Graph.BipartiteGraph;

import java.util.*;

/**
 * This class contains a method that solves BDP with the barycenter heuristic.
 * @author Flor De Meulemeester
 */
public class BaryCenter {

    /**
     * Minimize the amount of crossings using the barycenter heuristic.
     *
     * @param   graph
     *          The given graph.
     * @return The minimal amount of crossings found along with a List that contains the solution.
     */
    public static Map.Entry<Long, List> heuristic(BipartiteGraph graph) {
        boolean improved = true;
        long oldCrossings = graph.calculateTotalCrossings();
        long newCrossings = oldCrossings;
        long crossings = oldCrossings;
        HashMap<Integer, Integer> bestA = graph.getHeightA();
        HashMap<Integer, Integer> bestB = graph.getHeightB();
        while (improved) {
            // first we permute the vertices in A and we lock B
            HashMap<Integer, Long> verticesA = new HashMap<>();
            for (int vertex : graph.getSequenceA()) {
                verticesA.put(vertex, BaryCenter.calculateBaryCenterValue(graph, vertex, "A"));
            }
            graph.getSequenceA().sort(Comparator.comparing(verticesA::get));
            graph.sequenceToHeightA(graph.getSequenceA());

            // then we permute the vertices in B and we lock A
            HashMap<Integer, Long> verticesB = new HashMap<>();
            for (int vertex : graph.getSequenceB()) {
                verticesB.put(vertex, BaryCenter.calculateBaryCenterValue(graph, vertex, "B"));
            }
            graph.getSequenceB().sort(Comparator.comparing(verticesB::get));
            graph.sequenceToHeightB(graph.getSequenceB());

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
        List<Integer> list = graph.heightToSequenceA(bestA);
        list.addAll(graph.heightToSequenceB(bestB));
        Map.Entry<Long, List> result = new AbstractMap.SimpleEntry<>(crossings, list);
        return result;
    }

    /**
     * Calculate the barycenter value of a vertex.
     *
     * @param   graph
     *          The given graph
     * @param   vertex
     *          The index of the vertex that belongs to group
     * @param   group
     *          A string, either "A" or "B"
     */
    public static long calculateBaryCenterValue(BipartiteGraph graph, int vertex, String group) {
        if (graph.getNeighborsOf(vertex).isEmpty()) {
            return 1;
        }
        int average = 0;
        if (group.equals("A")) {
            for (Object neighbour : graph.getNeighborsOf(vertex)) {
                average += graph.getSequenceB().indexOf(neighbour);
            }
        } else {
            for (Object neighbour : graph.getNeighborsOf(vertex)) {
                average += graph.getSequenceA().indexOf(neighbour);
            }
        }
        return average / graph.getNeighborsOf(vertex).size();
    }

}
