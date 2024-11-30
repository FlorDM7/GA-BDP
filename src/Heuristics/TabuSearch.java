package Heuristics;

import Graph.BipartiteGraph;
import org.testng.internal.collections.Pair;
import java.util.*;

/**
 * This class contains a method that solves BDP with the tabu search meta-heuristic.
 * @author Flor De Meulemeester
 */
public class TabuSearch {

    static final int maxTabuSize = 30;

    public static Map.Entry<Long, List> heuristic(BipartiteGraph graph, int maxIterations, int tabuSize) {
        // choose an initial solution
        Map.Entry<Long, List> initial = initializeSolution(graph);
        // initialize the "best" solution
        long bestCrossings = initial.getKey();
        HashMap<Integer, Integer> bestA = graph.getHeightA();
        HashMap<Integer, Integer> bestB = graph.getHeightB();
        // keep track of the number of iterations
        int currentIterations = 0;

        // we define a neighbourhood move as a swap between any two nodes
        // in the tabulist we keep track of recently executed moves so that they won't be repeated
        List<Pair<Integer, Integer>> tabulist = new ArrayList<>();
        Pair<Integer, Integer> bestCandidate = null;
        long bestCandidateCrossings = objectiveFunction(graph);

        while (!stoppingCondition(currentIterations, maxIterations)) {
            String group = decideGroup(currentIterations);
            List<Pair<Integer, Integer>> neighbourhood = getNeighbours(graph, group);
            bestCandidate = null;
            for (Pair<Integer, Integer> candidate : neighbourhood) {
                if (!tabulist.contains(candidate)) {
                    graph.swap(candidate.first(), candidate.second(), group);
                    if (graph.calculateTotalCrossings() <= bestCandidateCrossings) {
                        bestCandidate = candidate;
                        bestCandidateCrossings = graph.calculateTotalCrossings();
                    }
                    graph.swap(candidate.first(), candidate.second(), group);
                }
            }

            if (bestCandidate == null) {
                break;
            }

            // execute the move and also update tabulist
            graph.swap(bestCandidate.first(), bestCandidate.second(), group);
            tabulist.add(bestCandidate);
            if (tabulist.size() > tabuSize) {
                tabulist.removeFirst();
            }

            // update the best solution if needed
            if (bestCandidateCrossings < bestCrossings) {
                bestA = graph.getHeightA();
                bestB = graph.getHeightB();
                bestCrossings = bestCandidateCrossings;
            }

            // increment
            currentIterations++;
        }

        // return solution in correct format
        List<Integer> list = graph.heightToSequenceA(bestA);
        list.addAll(graph.heightToSequenceB(bestB));
        return new AbstractMap.SimpleEntry<>(bestCrossings, list);
    }

    /**
     * Overloaded method for when there is no tabuSize given.
     */
    public static Map.Entry<Long, List> heuristic(BipartiteGraph graph, int maxIterations) {
        return heuristic(graph, maxIterations, maxTabuSize);
    }

    public static Map.Entry<Long, List> initializeSolution(BipartiteGraph graph) {
        List<Integer> list = new ArrayList<>(graph.getSequenceA());
        list.addAll(graph.getSequenceB());
        return new AbstractMap.SimpleEntry<>(graph.calculateTotalCrossings(), list);
    }

    public static Map.Entry<Long, List> initializeWIthBaryCenter(BipartiteGraph graph) {
        return BaryCenter.heuristic(graph);
    }

    public static Map.Entry<Long, List> initializeWithSplitting(BipartiteGraph graph) {
        return Splitting.heuristic(graph);
    }

    public static long objectiveFunction(BipartiteGraph graph) {
        return graph.calculateTotalCrossings();
    }

    public static boolean stoppingCondition(int currentIterations, int maxIterations) {
        return maxIterations - currentIterations == 0;
    }

    public static List<Pair<Integer, Integer>> getNeighbours(BipartiteGraph graph, String group) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        int firstIndex = graph.getFirstIndexOfGroup(group);
        int lastIndex = firstIndex + graph.getNumberOfVerticesInGroup(group);
        for (int i = firstIndex; i < lastIndex; i++) {
            for (int j = firstIndex; j < lastIndex; j++) {
                if (i != j) {
                    neighbours.add(new Pair<>(i, j));
                }
            }
        }
        return neighbours;
    }

    public static String decideGroup(int iterations) {
        if (iterations % 2 == 0) {
            return "A";
        } else {
            return "B";
        }
    }

}
