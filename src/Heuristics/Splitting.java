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
        List<Integer> bestA = graph.getSequenceA();
        List<Integer> bestB = graph.getSequenceB();
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
            long difference = 0;
            int mid = (left + right) / 2;
            long oldCrossings = graph.calculateTotalCrossings();
            List<Integer> sequence = new ArrayList<Integer>(graph.getSequenceOfGroup(group));
            int midMinIndex = sequence.get(Math.max(mid-1,0));
            int midPlusIndex = sequence.get(Math.min(mid+1, sequence.size()-1));
            // go over elke knoop
            for (int vertex : sequence) {
                int heightOfVertex = graph.getHeightOfVertex(vertex);
                // indien de knoop in het geselecteerde interval zit
                if (left <= heightOfVertex && heightOfVertex <= right) {
                    // indien de knoop hoger is dan het midden
                    if (heightOfVertex > mid) {
                        // this method calculates the difference in crossings after swap and also performs the swap
                        difference = graph.differenceCrossingsAfterSwap(vertex, midMinIndex, group);
                        if (difference <= 0) {
                            graph.swap(vertex, midMinIndex, group); // undo swap
                        } else {
                            oldCrossings -= difference; // improvement
                            midMinIndex = sequence.get(Math.max(mid-1,1)); // there is now a new element at this spot
                        }
                    } else if (heightOfVertex < mid) { // indien de knoop lager is dan het midden
                        // this method calculates the difference in crossings after swap and also performs the swap
                        difference = graph.differenceCrossingsAfterSwap(vertex, midPlusIndex, group);
                        if (difference <= 0) {
                            graph.swap(vertex, midPlusIndex, group); // undo swap
                        } else {
                            oldCrossings -= difference; // improvement !!
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
}