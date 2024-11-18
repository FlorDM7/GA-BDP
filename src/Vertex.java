import be.kuleuven.cs.som.annotate.Basic;

/**
 * This class represents a vertex within a graph.
 * @author Flor De Meulemeester
 */
public class Vertex {

    /**
     * The index of a vertex.
     */
    private int index;

    public Vertex(int index) {
        this.index = index;
    }

    @Basic
    public int getIndex() {
        return index;
    }

    @Basic
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Compares the index of the vertex with the index of another vertex.
     *
     * @param vertex
     * The vertex to compare with
     */
    public boolean equals(Vertex vertex) {
        return index == vertex.getIndex();
    }
}