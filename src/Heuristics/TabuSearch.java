package Heuristics;

import Graph.BipartiteGraph;
import org.testng.internal.collections.Pair;
import java.util.*;

/**
 * This class contains a method that solves BDP with the tabu search meta-heuristic.
 * @author Flor De Meulemeester
 */
public class TabuSearch {

    /**
     * The size of the tabu list, this is a default value.
     */
    static final int maxTabuSize = 15;

    /**
     * Minimize the amount of crossings using the tabu search meta-heuristic.
     * We generate a series of moves and choose the best possible move. A move consist of
     * swapping two vertices in a subset.
     *
     * @param   graph
     *          The given graph.
     * @param   maxIterations
     *          The maximum amount of iterations that the algorithm will do.
     * @param   tabuSize
     *          The size of the tabu list, in which we stored prohibited moves.
     * @param   rangeOfNeighbourhood
     *          The size of the neighbourhood, how many moves do we allow for each vertex.
     * @param   start
     *          Which start solution do we use, options: "Normal", "Barycenter", "Splitting"
     *          This is handy when we combine heuristics
     * @return  The minimal amount of crossings found along with a List that contains the solution.
     */
    public static Map.Entry<Long, List> heuristic(BipartiteGraph graph, int maxIterations, int tabuSize, int rangeOfNeighbourhood, String start) {
        Map.Entry<Long, List> initial;
        // choose an initial solution
        if (start.equals("Splitting")) {
            initial = initializeWithSplitting(graph);
        } else if (start.equals("Barycenter")) {
            initial = initializeWIthBaryCenter(graph);
        } else {
            initial = initializeSolution(graph);
        }
        // initialize the "best" solution
        long bestCrossings = initial.getKey();
        HashMap<Integer, Integer> bestA = graph.sequenceToHeightA(initial.getValue().subList(0, graph.getFirstIndexOfB()));
        HashMap<Integer, Integer> bestB = graph.sequenceToHeightA(initial.getValue().subList(graph.getFirstIndexOfB(), graph.getNumberOfVertices()));
        // keep track of the number of iterations
        int currentIterations = 0;

        // we define a neighbourhood move as a swap between any two nodes
        // in the tabulist we keep track of recently executed moves so that they won't be repeated
        List<Pair<Integer, Integer>> tabulist = new ArrayList<>();
        Pair<Integer, Integer> bestCandidate = null;
        long bestCandidateCrossings = objectiveFunction(graph);
        long bestCandidateCrossingsWon = 0;

        // we calculate the neighbourhood OUTSIDE the loop, otherwise this is a big bottleneck
        List<Pair<Integer, Integer>> neighbourhoodA;
        if (rangeOfNeighbourhood > graph.getNumberOfVerticesInGroup("A")) {
            neighbourhoodA = getAllNeighbours(graph,"A");
        } else {
            neighbourhoodA = getAdjacentNeighbours(graph,"A");
        }
        List<Pair<Integer, Integer>> neighbourhoodB;
        if (rangeOfNeighbourhood > graph.getNumberOfVerticesInGroup("B")) {
            neighbourhoodB = getAllNeighbours(graph,"B");
        } else {
            neighbourhoodB = getAdjacentNeighbours(graph, "B");
        }
        List<Pair<Integer, Integer>> neighbourhood;

        while (!stoppingCondition(currentIterations, maxIterations)) {
            String group = decideGroup(currentIterations);
            bestCandidate = null;
            bestCandidateCrossingsWon = Long.MIN_VALUE;
            if (group.equals("A")) {
                neighbourhood = neighbourhoodA;
            } else {
                neighbourhood = neighbourhoodB;
            }
            for (Pair<Integer, Integer> candidate : neighbourhood) {
                if (!tabulist.contains(candidate)) {
                    // this method calculates the difference in crossings after swap and also performs the swap
                    long crossingsWon = graph.differenceCrossingsAfterSwap(candidate.first(), candidate.second(), group);
                    if (crossingsWon > bestCandidateCrossingsWon) {
                        bestCandidate = candidate;
                        bestCandidateCrossingsWon = crossingsWon;
                    }
                    // always undo the move, because we do the best move at the end
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

            // add the difference in crossings (we don't calculate completely again)
            bestCandidateCrossings -= bestCandidateCrossingsWon;
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
        return heuristic(graph, maxIterations, maxTabuSize, graph.getNumberOfVertices(), "Normal");
    }

    /**
     * Put the state of the graph into the solution format.
     */
    public static Map.Entry<Long, List> initializeSolution(BipartiteGraph graph) {
        List<Integer> list = new ArrayList<>(graph.getSequenceA());
        list.addAll(graph.getSequenceB());
        return new AbstractMap.SimpleEntry<>(graph.calculateTotalCrossings(), list);
    }

    /**
     * Run the Barycenter heuristic and return the solution.
     */
    public static Map.Entry<Long, List> initializeWIthBaryCenter(BipartiteGraph graph) {
        return BaryCenter.heuristic(graph);
    }

    /**
     * Run the Splitting heuristic on the graph and return the solution.
     */
    public static Map.Entry<Long, List> initializeWithSplitting(BipartiteGraph graph) {
        return Splitting.heuristic(graph);
    }

    public static long objectiveFunction(BipartiteGraph graph) {
        return graph.calculateTotalCrossings();
    }

    public static boolean stoppingCondition(int currentIterations, int maxIterations) {
        return maxIterations - currentIterations == 0;
    }

    public static List<Pair<Integer, Integer>> getAllNeighbours(BipartiteGraph graph, String group) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        int firstIndex = graph.getFirstIndexOfGroup(group);
        int lastIndex = firstIndex + graph.getNumberOfVerticesInGroup(group);
        for (int i = firstIndex; i < lastIndex; i++) {
            for (int j = i+1; j < lastIndex; j++) {
                neighbours.add(new Pair<>(i, j));
            }
        }
        return neighbours;
    }

    /**
     * We definiëren een andere neighbourhood move waarbij we enkel kijken
     * naar de knopen op een bepaalde afstand van elkaar.
     *
     * Indien we range gelijk aan 1 nemen krijgen we hetzelfde als getAdjacentNeighbours.
     */
    public static List<Pair<Integer, Integer>> getNeighboursInSpecificRange(BipartiteGraph graph, int range, String group) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        if (range == 0) {
            return neighbours;
        }
        int firstIndex = graph.getFirstIndexOfGroup(group);
        int lastIndex = firstIndex + graph.getNumberOfVerticesInGroup(group);
        for (int i = firstIndex; i < lastIndex-1; i++) {
            int limit = Math.min(firstIndex + range, lastIndex);
            for (int j = i+1; j <= limit; j++) {
                neighbours.add(new Pair<>(i, j));
            }
        }
        System.out.println(neighbours);
        return neighbours;
    }

    /**
     * We definiëren een andere neighbourhood move waarbij we enkel kijken naar de echte 'buren'.
     */
    public static List<Pair<Integer, Integer>> getAdjacentNeighbours(BipartiteGraph graph, String group) {
        List<Pair<Integer, Integer>> neighbours = new ArrayList<>();
        int firstIndex = graph.getFirstIndexOfGroup(group);
        int lastIndex = firstIndex + graph.getNumberOfVerticesInGroup(group);
        for (int i = firstIndex; i < lastIndex-1; i++) {
            neighbours.add(new Pair<>(i, i+1));
        }
        return neighbours;
    }

    /**
     * Help method so that we can alternate between the groups. (this way we avoid some code duplication)
     */
    public static String decideGroup(int iterations) {
        if (iterations % 2 == 0) {
            return "A";
        } else {
            return "B";
        }
    }

}
