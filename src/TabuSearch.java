import org.testng.internal.collections.Pair;
import java.util.*;

/**
 * This class contains a method that solves BDP with the tabu search meta-heuristic.
 * @author Flor De Meulemeester
 */
public class TabuSearch {

    static final int maxTabuSize = 30;

    public static Map.Entry<Integer, List> heuristic(BipartiteGraph graph, int maxIterations) {
        // choose an initial solution
        Map.Entry<Integer, List> initial = initializeSolution(graph);
        // initialize the "best" solution
        int bestCrossings = initial.getKey();
        List bestA = initial.getValue().subList(0, graph.getFirstIndexOfB() - 1);
        List bestB = initial.getValue().subList(graph.getFirstIndexOfB() - 1, graph.getNumberOfVertices());
        // keep track of the number of iterations
        int currentIterations = 0;

        // we define a neighbourhood move as a swap between any two nodes
        // in the tabulist we keep track of recently executed move so that they won't be repeated
        List<Pair<Integer, Integer>> tabulist = new ArrayList<>();
        Pair<Integer, Integer> bestCandidate = null;
        int bestCandidateCrossings = objectiveFunction(graph);

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
                currentIterations++;
                continue;
            }

            // execute the move and also update tabulist
            graph.swap(bestCandidate.first(), bestCandidate.second(), group);
            tabulist.add(bestCandidate);
            if (tabulist.size() > maxTabuSize) {
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
        List<Integer> list = new ArrayList<>(bestA);
        list.addAll(bestB);
        return new AbstractMap.SimpleEntry<>(bestCrossings, list);
    }

    public static Map.Entry<Integer, List> initializeSolution(BipartiteGraph graph) {
        List<Integer> list = new ArrayList<>(graph.getHeightA());
        list.addAll(graph.getHeightB());
        return new AbstractMap.SimpleEntry<>(graph.calculateTotalCrossings(), list);
    }

    public static Map.Entry<Integer, List> initializeWIthBaryCenter(BipartiteGraph graph) {
        return BaryCenter.heuristic(graph);
    }

    public static Map.Entry<Integer, List> initializeWithSplitting(BipartiteGraph graph) {
        return Splitting.heuristic(graph);
    }

    public static int objectiveFunction(BipartiteGraph graph) {
        return graph.calculateTotalCrossings();
    }

    public static boolean stoppingCondition(int currentIterations, int maxIterations) {
        return maxIterations - currentIterations == 0;
    }

    public static List<Pair<Integer, Integer>> getNeighbours(BipartiteGraph graph, String group) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        for (int i = 0; i < graph.getVerticesOf(group).size(); i++) {
            for (int j = 0; j < graph.getVerticesOf(group).size(); j++) {
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
