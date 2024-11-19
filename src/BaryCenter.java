import java.util.HashMap;

/**
 * This class will contain a method that solve BDP with the barycenter heuristic.
 * @author Flor De Meulemeester
 */
public class BaryCenter {

    public int heuristic(BipartiteGraph graph) {
        // first we permute the vertices in A and we lock B
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int vertex : graph.getHeigthA()) {

        }
        return graph.calculateTotalCrossings();
    }

    public int calculateBaryCenterValueA(BipartiteGraph graph, int vertex) {
        int average = 0;
        for (Object neighbour : graph.getNeighborsOf(vertex)) {
            average += graph.getHeigthB().indexOf(neighbour);
        }
        return average / graph.getNeighborsOf(vertex).size();
    }

}
