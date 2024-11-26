import be.kuleuven.cs.som.annotate.Basic;
import java.util.ArrayList;

/**
 * This class represents a vertex within a bipartite graph.
 * I don't know yet if I will use it.
 * @author Flor De Meulemeester
 */
public class Vertex {

    /**
     * The index of a vertex.
     */
    private int index;

    /**
     * The height of a vertex.
     */
    private int height;

    /**
     * The neighbour of a vertex (also vertices).
     */
    private ArrayList<Vertex> neighbours;

    public Vertex(int index) {
        this.index = index;
        this.neighbours = new ArrayList<>();
    }

    @Basic
    public int getIndex() {
        return index;
    }

    @Basic
    private void setIndex(int index) {
        this.index = index;
    }

    @Basic
    public ArrayList<Vertex> getNeighbours() {
        return neighbours;
    }

    @Basic
    protected void setNeighbours(ArrayList<Vertex> neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * Compares the index of the vertex with the index of another vertex.
     *
     * @param   vertex
     *          The vertex to compare with
     */
    public boolean equals(Vertex vertex) {
        return index == vertex.getIndex();
    }
}