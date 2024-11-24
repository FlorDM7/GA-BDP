import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * This class contains a method that solves BDP with the splitting heuristic.
 * @author Flor De Meulemeester
 */
public class Splitting {

    public static Map.Entry<Integer, List> heuristic(BipartiteGraph graph) {
        boolean improved = true;
        int oldCrossings = graph.calculateTotalCrossings();
        int newCrossings = oldCrossings;
        int crossings = oldCrossings;
        ArrayList<Integer> bestA = graph.getHeightA();
        ArrayList<Integer> bestB = graph.getHeightB();
        while (improved) {
            // do splitting on both sides once
            splittingRecursiveA(graph, 0, bestA.size()-1);
            splittingRecursiveB(graph, 0, bestB.size()-1);

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
        crossings = graph.calculateTotalCrossings();
        List<Integer> list = new ArrayList<>(graph.getHeightA());
        list.addAll(graph.getHeightB());
        Map.Entry<Integer, List> result = new AbstractMap.SimpleEntry<>(crossings, list);
        return result;
    }

    private static void splittingRecursiveA(BipartiteGraph graph, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            int oldCrossings = graph.calculateTotalCrossings();
            for (int vertex : graph.getVerticesOf("A").keySet()) {
                if (left <= graph.getHeightOfVertexA(vertex) && graph.getHeightOfVertexA(vertex) <= right) {
                    int index = graph.getHeightA().indexOf(vertex);
                    if (graph.getHeightOfVertexA(vertex) > mid) {
                        graph.swapA(index, mid - 1);
                        int newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swapA(index, mid - 1);
                        } else {
                            oldCrossings = newCrossings;
                        }
                    } else if (graph.getHeightOfVertexA(vertex) < mid) {
                        graph.swapA(index, mid + 1);
                        int newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swapA(index, mid + 1);
                        } else {
                            oldCrossings = newCrossings;
                        }
                    }
                }
            }
            splittingRecursiveA(graph, left, mid - 1);
            splittingRecursiveA(graph, mid+1, right);
        }
    }

    private static void splittingRecursiveB(BipartiteGraph graph, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            for (int vertex : graph.getVerticesOf("B").keySet()) {
                int oldCrossings = graph.calculateTotalCrossings();
                if (left <= graph.getHeightOfVertexB(vertex) && graph.getHeightOfVertexB(vertex) <= right) {
                    int index = graph.getHeightB().indexOf(vertex);
                    if (graph.getHeightOfVertexB(vertex) > mid) {
                        graph.swapB(index, mid - 1);
                        int newCrossings = graph.calculateTotalCrossings();
                        if (newCrossings >= oldCrossings) {
                            graph.swapB(index, mid - 1);
                        }
                    } else if (graph.getHeightOfVertexB(vertex) < mid) {
                        graph.swapB(index, mid + 1);
                        int newCrossings = graph.calculateTotalCrossings();
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
}