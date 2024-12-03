package Heuristics;

import Graph.BipartiteGraph;

import java.util.*;

/**
 * This class contains a method that solves BDP with the splitting heuristic.
 * @author Flor De Meulemeester
 */
public class Splitting {

    /**
     * Minimize the amount of crossings using the splitting heuristic. In one iteration we do each group once:
     * We choose the middle element and for each other element we check if it is better above or under the middle
     * and keep the best one. Then we split in two groups and go on.
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
        List<Integer> bestA = graph.getSequenceA();
        List<Integer> bestB = graph.getSequenceB();
        while (improved) {
            // do splitting on both sides once
            splittingRecursive(graph, 0, graph.getNumberOfVerticesInGroup("A") - 1, "A", oldCrossings);
            splittingRecursive(graph, 0, graph.getNumberOfVerticesInGroup("B") - 1, "B", oldCrossings);

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
     *          De given graph
     * @param   left
     *          Index in the sequence (not of the vertex) of the most left element
     * @param   right
     *          Index in the sequence (not of the vertex) of the most right element
     * @param   crossings
     *          This way we don't need to calculate the crossings every recursion
     * @param   group
     *          A string, either "A" of "B"
     */
    private static void splittingRecursive(BipartiteGraph graph, int left, int right, String group, long crossings) {
        if (left < right) {
            long difference = 0;
            int mid = (left + right) / 2;
            List<Integer> sequence = new ArrayList<Integer>(graph.getSequenceOf(group));
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
                            crossings -= difference; // improvement
                            midMinIndex = sequence.get(Math.max(mid-1,1)); // there is now a new element at this spot
                        }
                    } else if (heightOfVertex < mid) { // indien de knoop lager is dan het midden
                        // this method calculates the difference in crossings after swap and also performs the swap
                        difference = graph.differenceCrossingsAfterSwap(vertex, midPlusIndex, group);
                        if (difference <= 0) {
                            graph.swap(vertex, midPlusIndex, group); // undo swap
                        } else {
                            crossings -= difference; // improvement !!
                            midPlusIndex = sequence.get(Math.min(mid+1, sequence.size()-1)); // there is now a new element on this spot
                        }
                    }
                }
            }
            graph.heightToSequenceGroup(graph.getHeightOfGroup(group), group);
            // recursion
            splittingRecursive(graph, left, mid-1, group, crossings);
            splittingRecursive(graph, mid+1, right, group, crossings);
        }
    }
}