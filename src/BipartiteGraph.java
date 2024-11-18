import be.kuleuven.cs.som.annotate.Basic;
import java.util.*;

/**
 * This class implements the BipartiteGraphInterface.
 * This class represents a bipartite graph with 2 groups of vertices.
 *
 * @author Flor De Meulemeester
 */
public class BipartiteGraph implements BipartiteGraphInterface {

    /**
     * The number of vertices in the graph, this is initialized in the constructor.
     */
    final int numberOfVertices;

    /**
     * The number of vertices in subset A of the vertices of the graph, this number is also initialized in the constructor
     */
    final int numberOfVerticesInA;
    final int numberOfVerticesInB;
    final int numberOfEdges;
    private HashMap<Integer, ArrayList<Integer>> verticesA = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> verticesB = new HashMap<>();
    private ArrayList<Integer> arrayA = new ArrayList<>();
    private ArrayList<Integer> arrayB = new ArrayList<>();

    protected BipartiteGraph(int numberOfVertices, int a, int b, int numberOfEdges) {
        this.numberOfVertices = numberOfVertices;
        this.numberOfVerticesInA = a;
        this.numberOfVerticesInB = b;
        this.numberOfEdges = numberOfEdges;
    }

    protected void addEdge(int a, int b) {
        if (!verticesA.containsKey(a)) {
            verticesA.put(a, new ArrayList<>());
        }
        verticesA.get(a).add(b);
        if (!verticesB.containsKey(b)) {
            verticesB.put(b, new ArrayList<>());
        }
        verticesB.get(b).add(a);
    }

    /**
     * Returns a collection of objects representing the vertices of the graph.
     *
     * Currently, these are just integers but should be objects
     */
    @Override
    public Collection getVertices() {
        List<Integer> result = new ArrayList<>();
        result.addAll(verticesA.keySet().stream().toList());
        result.addAll(verticesB.keySet().stream().toList());
        return result;
    }

    /**
     * Returns the number of edges present in the graph.
     */
    @Override @Basic
    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    /**
     * Returns the number of vertices present in the graph.
     */
    @Override @Basic
    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    /**
     * Returns whether vertex u and vertex v are neighbours.
     */
    @Override
    public boolean areNeighbors(Object u, Object v) {
        return false;
    }

    /**
     * Returns the degree of vertex u.
     */
    @Override
    public int getDegree(Object u) {
        return 0;
    }

    /**
     * Returns a collection of objects representing the neighbours of vertex u.
     */
    @Override
    public Collection getNeighborsOf(Object u) {
        return List.of();
    }

    /**
     * Apply the chosen metaheuristic for a maximum of maxIterations iterations. This method returns a Map Entry
     * containing an Integer representing the number of crossings for the acquired solution as well as a List of
     * Vertices representing the order of the vertices in the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runMetaheuristicTest(int maxIterations) {
        return null;
    }

    /**
     * Apply the first chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runFirstHeuristic() {
        return null;
    }

    /**
     * Apply the second chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runSecondHeuristic() {
        return null;
    }
}
