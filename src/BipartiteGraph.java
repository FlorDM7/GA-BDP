import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class implements the BipartiteGraphInterface
 * @author Flor De Meulemeester
 */
public class BipartiteGraph implements BipartiteGraphInterface {

    @Override
    public Collection getVertices() {
        return List.of();
    }

    @Override
    public int getNumberOfEdges() {
        return 0;
    }

    @Override
    public int getNumberOfVertices() {
        return 0;
    }

    @Override
    public boolean areNeighbors(Object u, Object v) {
        return false;
    }

    @Override
    public int getDegree(Object u) {
        return 0;
    }

    @Override
    public Collection getNeighborsOf(Object u) {
        return List.of();
    }

    @Override
    public Map.Entry<Integer, List> runMetaheuristicTest(int maxIterations) {
        return null;
    }

    @Override
    public Map.Entry<Integer, List> runFirstHeuristic() {
        return null;
    }

    @Override
    public Map.Entry<Integer, List> runSecondHeuristic() {
        return null;
    }
}
