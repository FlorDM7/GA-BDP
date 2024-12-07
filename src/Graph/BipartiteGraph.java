package Graph;

import Heuristics.BaryCenter;
import Heuristics.Splitting;
import Heuristics.TabuSearch;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

import java.util.*;

/**
 * This class implements the BipartiteGraphInterface.
 * This class represents a bipartite graph with 2 disjoint sets of vertices.
 * @author Flor De Meulemeester
 */
public class BipartiteGraph implements BipartiteGraphInterface {

    /**
     * The number of vertices in the graph, this is initialized in the constructor.
     */
    final int numberOfVertices;

    /**
     * The number of vertices in subset A of the vertices of the graph, this number is also initialized in the constructor.
     */
    final int numberOfVerticesInA;

    /**
     * The number of vertices in subset B of the vertices of the graph, this number is also initialized in the constructor.
     */
    final int numberOfVerticesInB;

    /**
     * The number of edges in the graph, also initialized in the constructor.
     */
    final int numberOfEdges;

    /**
     * verticesA and verticesB map the indexes of the vertices in their group
     * to a list of the indexes of their direct neighbours in the other group.
     *  Example:
     *  1 is element of A and has edges to 3 and 4 that are element of B.
     *  In verticesA we put: (1,{3,4})
     *  In verticesB we put: (3,{1,...}) and (4,{1,...})
     */
    private HashMap<Integer, ArrayList<Integer>> verticesA = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> verticesB = new HashMap<>();

    /**
     * Sequence is an array that sorts the indexes of vertices in the group based on their height currently in the graph.
     * We use this in the BaryCenter heuristic when we permute the indexes.
     */
    public List<Integer> sequenceA = new ArrayList<>();
    public List<Integer> sequenceB = new ArrayList<>();

    /**
     * Height is a HashMap for each group that maps the indexes on their height.
     * We use this for calculating the total amount of crossings in a graph.
     */
    public HashMap<Integer, Integer> heightA = new HashMap<>();
    public HashMap<Integer, Integer> heightB = new HashMap<>();

    // CONSTRUCTOR

    /**
     * Constructor for creating a bipartite graph.
     * This is used when we read a graph from an input text file.
     *
     * @param   numberOfVertices
     *          The number of vertices in the graph.
     * @param   a
     *          The number of vertices in the subset A.
     * @param   b
     *          The number of vertices in the subset B.
     * @param   numberOfEdges
     *          The number of edges in the graph.
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
     * This method is only used in the constructor.
     */
    private void initializeVertices(int b) {
        int i = 1;
        while (i <= numberOfVerticesInA) {
            verticesA.put(i, new ArrayList<>());
            sequenceA.add(i);
            heightA.put(i, numberOfVerticesInA - i + 1);
            i++;
        }
        int j = numberOfVertices - b + 1;
        while (j <= numberOfVertices) {
            verticesB.put(j, new ArrayList<>());
            sequenceB.add(j);
            heightB.put(j, numberOfVertices - j + 1);
            j++;
        }
    }

    /**
     * When reading the input we use this method to add the connections between the vertices.
     */
    protected void addEdge(int a, int b) {
        verticesA.get(a).add(b); // add index b to the neighbours of a
        verticesB.get(b).add(a); // vice versa
    }

    /**
     * We also sort the indexes when reading the input.
     * So we can better predict where vertices are during testing and debugging.
     */
    protected void sortInput() {
        Collections.sort(sequenceA);
        Collections.sort(sequenceB);
        sequenceToHeightA(sequenceA);
        sequenceToHeightB(sequenceB);
    }

    // CALCULATE CROSSINGS

    /**
     * Calculate the K value of a given (i,j) pair where i and j are in subset A
     * K(i,j) = #crossings that edges of i cause on edges of j
     *
     * @pre i and j should be elements of A
     */
    public int calculateValueOfKInGroupA(int i, int j) {
        if (!(getHeightOfVertexA(i) > getHeightOfVertexA(j))) {
            return 0;
        }
        int k = 0;
        List<Integer> neighboursOfI = verticesA.get(i);
        List<Integer> neighboursOfJ = verticesA.get(j);
        for (int neighbourOfI : neighboursOfI) {
            for (int neighbourOfJ : neighboursOfJ) {
                if (getHeightOfVertexB(neighbourOfI) < getHeightOfVertexB(neighbourOfJ)) {
                    k++;
                }
            }
        }
        return k;
    }

    /**
     * Calculate the K value of a given (i,j) pair where i and j are in subset B
     * K(i,j) = #crossings that edges of i cause on edges of j
     *
     * @pre i and j should be elements of B
     */
    private int calculateValueOfKInGroupB(int i, int j) {
        if (!(getHeightOfVertexB(i) >= getHeightOfVertexB(j))) {
            return 0;
        }
        int k = 0;
        List<Integer> neighboursOfI = getVerticesOf("B").get(i);
        List<Integer> neighboursOfJ = getVerticesOf("B").get(j);
        for (int neighbourOfI : neighboursOfI) {
            for (int neighbourOfJ : neighboursOfJ) {
                if (getHeightOfVertexA(neighbourOfI) < getHeightOfVertexA(neighbourOfJ)) {
                    k++;
                }
            }
        }
        return k;
    }

    /**
     * When we swap two nodes, not all crossings are impacted, we only need to calculate the K-values
     * of vertices i, i+1, ..., j-1, j. This way we don't have to calculate all crossings again after
     * doing one swap.
     *
     * @pre i and j should be elements of A
     */
    public long differenceCrossingsAfterSwapA(int i, int j) {
        long before = 0;
        List<Integer> sequence = heightToSequenceA(getHeightA());
        int heightOfI = getHeightOfVertexA(i);
        int heightOfJ = getHeightOfVertexA(j);
        // my implementation of K(i,j) only works when h(i) > h(j) so if not we swap i and j
        if (heightOfI < heightOfJ) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        List<Integer> subsequence = sequence.subList(sequence.indexOf(i), sequence.indexOf(j)+1);
        for (int value : subsequence) {
            before += calculateValueOfKInGroupA(i, value);
            before +=  calculateValueOfKInGroupA(value, j);
        }
        before -= calculateValueOfKInGroupA(i, j); // we calculated this twice

        swapA(i,j);

        long after = 0;
        for (int value  : subsequence) {
            after += calculateValueOfKInGroupA(value, i);
            after +=  calculateValueOfKInGroupA(j, value);
        }
        after -= calculateValueOfKInGroupA(j, i); // we calculated this twice
        return before - after;
    }

    /**
     * When we swap two nodes, not all crossings are impacted, we only need to calculate the K-values
     * of vertices i, i+1, ..., j-1, j. This way we don't have to calculate all crossings again after
     * doing one swap.
     *
     * @pre i and j should be elements of B
     */
    public long differenceCrossingsAfterSwapB(int i, int j) {
        long before = 0;
        List<Integer> sequence = heightToSequenceB(getHeightB());
        int heightOfI = getHeightOfVertexB(i);
        int heightOfJ = getHeightOfVertexB(j);
        // my implementation of K(i,j) only works when h(i) > h(j) so if not we swap i and j
        if (heightOfI < heightOfJ) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        List<Integer> subsequence = sequence.subList(sequence.indexOf(i), sequence.indexOf(j) + 1);
        for (int value : subsequence) {
            before += calculateValueOfKInGroupB(i, value);
            before +=  calculateValueOfKInGroupB(value, j);
        }
        before -= calculateValueOfKInGroupB(i, j); // we calculated this twice

        swapB(i,j);

        long after = 0;
        for (int value  : subsequence) {
            after += calculateValueOfKInGroupB(value, i);
            after +=  calculateValueOfKInGroupB(j, value);
        }
        after -= calculateValueOfKInGroupB(j, i); // we calculated this twice
        return before - after;
    }

    /**
     * Execute the methods above depending on the group.
     *
     * @param   i
     *          The index of the first vertex
     * @param   j
     *          The index of the second vertex
     * @param   group
     *          A string, this is either "A" or "B"
     */
    public long differenceCrossingsAfterSwap(int i, int j, String group) {
        if (group.equals("A")) {
            return differenceCrossingsAfterSwapA(i, j);
        } else {
            return differenceCrossingsAfterSwapB(i, j);
        }
    }

    /**
     * Calculates the total #crossings in the graph using the help function with K values.
     */
    public long calculateTotalCrossings() {
        long crossings = 0;
        for (int i=1; i < getFirstIndexOfB(); i++) {
            for (int j=1; j < getFirstIndexOfB(); j++) {
                crossings += calculateValueOfKInGroupA(i, j);
            }
        }
        return crossings;
    }

    // VERTICES

    /**
     * Return a hashmap containing indexes that map to the indexes of the neighbours.
     *
     * @param   group
     *          A string, this is either "A" or "B"
     * @return  Either verticesA or verticesB
     */
    public HashMap<Integer, ArrayList<Integer>> getVerticesOf(String group) {
        if (group.equals("A")) {
            return verticesA;
        }
        if (group.equals("B")) {
            return verticesB;
        }
        return null;
    }

    /**
     * @param   group
     *          A string, this is either "A" or "B"
     * @return  The amount of vertices in group
     */
    public int getNumberOfVerticesInGroup(String group) {
        if (group.equals("A")) {
            return numberOfVerticesInA;
        } else {
            return numberOfVerticesInB;
        }
    }

    // SEQUENCE

    public List<Integer>  getSequenceOf(String group) {
        if (group.equals("A")) {
            return sequenceA;
        }
        if (group.equals("B")) {
            return sequenceB;
        }
        return null;
    }

    @Basic
    public List<Integer> getSequenceA() {
        return sequenceA;
    }

    @Basic
    public List<Integer> getSequenceB() {
        return sequenceB;
    }

    /**
     * We sort the indicies of group A based on the possibly new heights in the height variable.
     */
    public List<Integer> heightToSequenceA(HashMap<Integer, Integer> height) {
        sequenceA.sort(Collections.reverseOrder(Comparator.comparing(height::get)));
        return sequenceA;
    }

    /**
     * We sort the indicies of group B based on the possibly new heights in the height variable.
     */
    public List<Integer> heightToSequenceB(HashMap<Integer, Integer> height) {
        sequenceB.sort(Collections.reverseOrder(Comparator.comparing(height::get)));
        return sequenceB;
    }

    public void heightToSequenceGroup(HashMap<Integer, Integer> height, String group) {
        if (group.equals("A")) {
            heightToSequenceA(height);
        } else {
            heightToSequenceB(height);
        }
    }

    // SWAP

    public void swapA(int index1, int index2) {
        int h1 = heightA.get(index1);
        int h2 = heightA.get(index2);
        heightA.put(index1, h2);
        heightA.put(index2, h1);
    }

    public void swapB(int index1, int index2) {
        int h1 = heightB.get(index1);
        int h2 = heightB.get(index2);
        heightB.put(index1, h2);
        heightB.put(index2, h1);
    }

    public void swap(int index1, int index2, String group) {
        if (group.equals("A")) {
            swapA(index1, index2);
        } else if (group.equals("B")) {
            swapB(index1, index2);
        }
    }

    // HEIGHT

    /**
     * Return the height of the vertex based on the y-axis.
     * We assume that the graph is represented as two vertical lines on x=0 and x=1,
     * so that the nodes are places on those lines respectively.
     * @pre     vertex should be an element of either A or B
     */
    public int getHeightOfVertex(int vertex) {
        if (vertex < getFirstIndexOfB()) {
            return getHeightOfVertexA(vertex);
        } else {
            return getHeightOfVertexB(vertex);
        }
    }

    /**
     * @param   vertex
     *          The index of the vertex
     * @pre     Vertex should be an element of A
     * @return  The height of the vertex
     */
    public int getHeightOfVertexA(int vertex) {
        return heightA.get(vertex);
    }

    /**
     * @param   vertex
     *          The index of the vertex
     * @pre     Vertex should be an element of B
     * @return  The height of the vertex
     */
    public int getHeightOfVertexB(int vertex) {
        return heightB.get(vertex);
    }

    @Basic
    public HashMap<Integer, Integer> getHeightA() {
        return heightA;
    }

    @Basic
    public HashMap<Integer, Integer> getHeightB() {
        return heightB;
    }

    public HashMap<Integer, Integer> getHeightOfGroup(String group) {
        if (group.equals("A")) {
            return getHeightA();
        } else {
            return getHeightB();
        }
    }

    /**
     * Based on a sequence of indices from group A, we update the values in the height mapping.
     */
    public HashMap<Integer,Integer> sequenceToHeightA(List<Integer> sequence) {
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i);
            heightA.put(index, numberOfVerticesInA - i);
        }
        return heightA;
    }

    /**
     * Based on a sequence of indices from group B, we update the values in the height mapping.
     */
    public HashMap<Integer,Integer> sequenceToHeightB(List<Integer> sequence) {
        for (int i = 0; i < sequence.size(); i++) {
            int index = sequence.get(i);
            heightB.put(index, numberOfVertices - numberOfVerticesInA - i);
        }
        return heightB;
    }

    // HELP METHODS

    @Immutable
    public int getFirstIndexOfA() {
        return 1;
    }

    @Immutable
    public int getFirstIndexOfB() {
        return getNumberOfVertices() - verticesB.size() + 1;
    }

    public int getFirstIndexOfGroup(String group) {
        if (group.equals("A")) {
            return getFirstIndexOfA();
        } else {
            return getFirstIndexOfB();
        }
    }

    // INTERFACE

    /**
     * Returns a collection of integers representing the vertices of the graph.
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
     * Apply the chosen meta-heuristic for a maximum of maxIterations iterations. This method returns a Map Entry
     * containing an Integer representing the number of crossings for the acquired solution as well as a List of
     * Vertices representing the order of the vertices in the acquired solution.
     */
    @Override
    public Map.Entry<Long, List> runMetaheuristicTest(int maxIterations) {
        return TabuSearch.heuristic(this, maxIterations);
    }

    public Map.Entry<Long, List> runMetaheuristicTest(int maxIterations, int tabuSize, int range, String start) {
        return TabuSearch.heuristic(this, maxIterations, tabuSize, range, start);
    }

    /**
     * Apply the first chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Long, List> runFirstHeuristic() {
        return BaryCenter.heuristic(this);
    }

    /**
     * Apply the second chosen heuristic. This method returns a Map Entry containing an Integer representing the number
     * of crossings for the acquired solution as well as a List of Vertices representing the order of the vertices in
     * the acquired solution.
     */
    @Override
    public Map.Entry<Long, List> runSecondHeuristic() {
        return Splitting.heuristic(this);
    }
}
