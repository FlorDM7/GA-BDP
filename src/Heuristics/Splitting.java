package Heuristics;

import Graph.BipartiteGraph;

import java.util.*;

/**
 * This class contains a method that solves BDP with the splitting heuristic.
 * @author Flor De Meulemeester
 */
public class Splitting {

    public static Map.Entry<Long, List> heuristic(BipartiteGraph graph) {
        boolean improved = true;
        long oldCrossings = graph.calculateTotalCrossings();
        long newCrossings = oldCrossings;
        long crossings = oldCrossings;
        ArrayList<Integer> bestA = graph.getSequenceA();
        ArrayList<Integer> bestB = graph.getSequenceB();
        while (improved) {
            // do splitting on both sides once
            splittingRecursive(graph, 0, graph.getNumberOfVerticesInGroup("A") - 1, "A");
            splittingRecursive(graph, 0, graph.getNumberOfVerticesInGroup("B") - 1, "B");

            // check if we improved
            newCrossings = graph.calculateTotalCrossings();
            if (newCrossings < oldCrossings) {
                crossings = graph.calculateTotalCrossings();
                bestA = graph.getSequenceA();
                bestB = graph.getSequenceB();
            } else {
                improved = false;
            }
            oldCrossings = newCrossings;
        }
        // get the correct output format
        crossings = graph.calculateTotalCrossings();
        List<Integer> list = new ArrayList<>(graph.getSequenceA());
        list.addAll(graph.getSequenceB());
        Map.Entry<Long, List> result = new AbstractMap.SimpleEntry<>(crossings, list);
        return result;
    }

    /**
     * Recursief dingen rond het midden wisselen of zo
     *
     * @param   graph
     *          De graaf
     * @param   left
     *          Hoogste van het meest linkse element
     * @param   right
     *          Hoogte van het meeste rechtse element
     * @param   group
     *          A of B
     */
    private static void splittingRecursive(BipartiteGraph graph, int left, int right, String group) {
        if (left < right) {
            int mid = (left + right) / 2;
            long oldCrossings = graph.calculateTotalCrossings();
            List<Integer> sequence = graph.getSequenceOfGroup(group);
            int midMinIndex = sequence.get(Math.max(mid-1,0));
            int midPlusIndex = sequence.get(Math.min(mid+1, sequence.size()-1));
            // go over elke knoop
            for (int vertex : graph.getSequenceOfGroup(group)) {
                int heightOfVertex = graph.getHeightOfVertex(vertex);
                // indien de knoop in het geselecteerde interval zit
                if (left <= heightOfVertex && heightOfVertex <= right) {
                    // indien de knoop hoger is dan het midden
                    if (heightOfVertex > mid) {
                        graph.swap(vertex, midMinIndex, group);
                        long newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swap(vertex, midMinIndex, group); // undo swap
                        } else {
                            oldCrossings = newCrossings; // improvement
                            midMinIndex = sequence.get(Math.max(mid-1,1)); // there is now a new element at this spot
                        }
                    } else if (heightOfVertex < mid) { // indien de knoop lager is dan het midden
                        graph.swap(vertex, midPlusIndex, group);
                        long newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swap(vertex, midPlusIndex, group); // undo swap
                        } else {
                            oldCrossings = newCrossings; // improvement !!
                            midPlusIndex = sequence.get(Math.min(mid+1, sequence.size()-1)); // there is now a new element on this spot
                        }
                    }
                }
            }
            graph.heightToSequenceGroup(graph.getHeightOfGroup(group), group);
            // recursion
            splittingRecursive(graph, left, mid-1, group);
            splittingRecursive(graph, mid+1, right, group);
        }
    }

    /*
    private static void splittingRecursiveB(BipartiteGraph graph, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            for (int vertex : graph.getVerticesOf("B").keySet()) {
                long oldCrossings = graph.calculateTotalCrossings();
                if (left <= graph.getHeightOfVertexB(vertex) && graph.getHeightOfVertexB(vertex) <= right) {
                    int index = graph.getSequenceB().indexOf(vertex);
                    if (graph.getHeightOfVertexB(vertex) > mid) {
                        graph.swapB(index, mid - 1);
                        long newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swapB(index, mid - 1);
                        }
                    } else if (graph.getHeightOfVertexB(vertex) < mid) {
                        graph.swapB(index, mid + 1);
                        long newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swapB(index, mid + 1);
                        }
                    }
                }
            }
            splittingRecursiveB(graph, left, mid - 1);
            splittingRecursiveB(graph, mid + 1, right);
        }
    }
     */
}