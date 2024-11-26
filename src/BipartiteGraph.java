import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class implements the BipartiteGraphInterface.
 * This class represents a bipartite graph with 2 groups of vertices.
 * @author Flor De Meulemeester
 */
public class BipartiteGraph implements BipartiteGraphInterface {

    public static void main(String args[]) throws FileNotFoundException {
        File file = new File("barycenter.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        System.out.println(graph.calculateTotalCrossings());
        System.out.println(graph.runMetaheuristicTest(6));
    }

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
    public ArrayList<Integer> heightA = new ArrayList<>();
    public ArrayList<Integer> heightB = new ArrayList<>();

    /**
     * Constructor for creating a bipartite graph.
     * This is used when we read a graph from an input text file.
     *
     * @param   numberOfVertices
     *          The number of vertices in the graph
     * @param   a
     *          The number of vertices in the subset A
     * @param   b
     *          The number of vertices in the subset B
     * @param   numberOfEdges
     *          The number of edges in the graph
     */
    protected BipartiteGraph(int numberOfVertices, int a, int b, int numberOfEdges) {
        this.numberOfVertices = numberOfVertices;
        this.numberOfVerticesInA = a;
        this.numberOfVerticesInB = b;
        this.numberOfEdges = numberOfEdges;
        initializeVertices(b);
    }

    /**
     * This method initializes the indices of the vertices in the graph.
     * Only used for the creation the graph.
     */
    private void initializeVertices(int b) {
        int i = 1;
        while (i <= numberOfVerticesInA) {
            verticesA.put(i, new ArrayList<>());
            heightA.add(i);
            i++;
        }
        int j = numberOfVertices - b + 1;
        while (j <= numberOfVertices) {
            verticesB.put(j, new ArrayList<>());
            heightB.add(j);
            j++;
        }
    }

    /**
     * When reading the input we use this method to add the connections between the vertices.
     */
    protected void addEdge(int a, int b) {
        // voeg toe aan hashmap A
        verticesA.get(a).add(b);
        // voeg toe aan hashmap B
        verticesB.get(b).add(a);
    }

    protected void sortInput() {
        Collections.sort(heightA);
        Collections.sort(heightB);
    }

    @Immutable
    public int getFirstIndexOfA() {
        return 1;
    }

    @Immutable
    public int getFirstIndexOfB() {
        return getNumberOfVertices() - verticesB.size() + 1;
    }

    /**
     * Calculate the K value of a given (i,j) pair where i and j are in the same subset and i < j
     * K is #crossings that edges of i cause on edges of j
     */
    public int calculateValueOfK(int i, int j) {
        // iedere keer opnieuw de index van een vertex zoeken
        if (!(getHeightOfVertexA(i) > getHeightOfVertexA(j))) {
            return 0;
        }
        int k = 0;
        List<Integer> neighboursOfI = getVerticesOf("A").get(i);
        List<Integer> neighboursOfJ = getVerticesOf("A").get(j);
        for (int neighbourOfI : neighboursOfI) {
            for (int neighbourOfJ : neighboursOfJ) {
                if (getHeightOfVertexB(neighbourOfI) < getHeightOfVertexB(neighbourOfJ)) {
                    k++;
                }
            }
        }
        return k;
    }

    public void differenceInCrossingsAfterSwap() {
        // TODO
        // op deze manier niet telkens total crossings opnieuw berekenen
    }

    /**
     * Calculates the total #crossings in the graph using the help function with K values.
     */
    public int calculateTotalCrossings() {
        int crossings = 0;
        for (int i=1; i<getFirstIndexOfB(); i++) {
            for (int j=1; j<getFirstIndexOfB(); j++) {
                crossings += calculateValueOfK(i, j);
            }
        }
        return crossings;
    }


    public HashMap<Integer, ArrayList<Integer>> getVerticesOf(String group) {
        if (group.equals("A")) {
            return verticesA;
        }
        if (group.equals("B")) {
            return verticesB;
        }
        return null;
    }

    public ArrayList<Integer> getHeightOf(String group) {
        if (group.equals("A")) {
            return heightA;
        }
        if (group.equals("B")) {
            return heightB;
        }
        return null;
    }

    @Basic
    public ArrayList<Integer> getHeightA() {
        return heightA;
    }

    @Basic
    public ArrayList<Integer> getHeightB() {
        return heightB;
    }

    @Basic
    protected void setHeightA(ArrayList<Integer> heightA) {
        this.heightA = heightA;
    }

    @Basic
    protected void setHeightB(ArrayList<Integer> heightB) {
        this.heightB = heightB;
    }

    public void swapA(int index1, int index2) {
        Collections.swap(getHeightA(), index1, index2);
    }

    public void swapB(int index1, int index2) {
        Collections.swap(getHeightB(), index1, index2);
    }

    public void swap(int index1, int index2, String group) {
        if (group.equals("A")) {
            Collections.swap(getHeightA(), index1, index2);
        } else if (group.equals("B")) {
            Collections.swap(getHeightB(), index1, index2);
        }
    }

    /**
     * Return the height of the vertex based on the y-axis.
     * We assume that the graph is represented as two vertical lines on x=0 and x=1,
     * so that the nodes are places on those lines respectively.
     */
    public int getHeightOfVertex(int vertex) {
        if (vertex < getFirstIndexOfB()) {
            return getHeightOfVertexA(vertex);
        } else {
            return getHeightOfVertexB(vertex);
        }
    }

    /**
     * Deze methode MOET herwerkt wordt.
     */
    public int getHeightOfVertexA(int vertex) {
        return getHeightA().size() - getHeightA().indexOf(vertex);
    }

    /**
     * En deze dan ook natuurlijk
     */
    public int getHeightOfVertexB(int vertex) {
        return getHeightB().size() - getHeightB().indexOf(vertex);
    }

    /**
     * Returns a collection of objects representing the vertices of the graph.
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
        return verticesA.get(u).contains(v);
    }

    /**
     * Returns the degree of vertex u.
     */
    @Override
    public int getDegree(Object u) {
        if ((int) u < getFirstIndexOfB()) {
            return verticesA.get(u).size();
        } else {
            return verticesB.get(u).size();
        }
    }

    /**
     * Returns a collection of objects representing the neighbours of vertex u.
     */
    @Override
    public Collection getNeighborsOf(Object u) {
        if ((int) u < getFirstIndexOfB()) {
            return verticesA.get(u);
        } else {
            return verticesB.get(u);
        }
    }

    /**
     * Apply the chosen metaheuristic for a maximum of maxIterations iterations. This method returns a Map Entry
     * containing an Integer representing the number of crossings for the acquired solution as well as a List of
     * Vertices representing the order of the vertices in the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runMetaheuristicTest(int maxIterations) {
        return TabuSearch.heuristic(this, maxIterations);
    }

    /**
     * Apply the first chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runFirstHeuristic() {
        return BaryCenter.heuristic(this);
    }

    /**
     * Apply the second chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Integer, List> runSecondHeuristic() {
        return Splitting.heuristic(this);
    }
}
