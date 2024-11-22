import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * This class will contain a method that solves BDP with the splitting heuristic.
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
        int mid = left + (right - left) / 2;
        int vertex = left;
    }

}