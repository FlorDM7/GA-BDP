package Tests;

import java.io.File;
import java.io.FileNotFoundException;
import Graph.BipartiteGraph;
import Graph.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CrossingsTest {

    @Test
    void test1() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/cycle_8_shuffled.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(12, graph.calculateTotalCrossings());
    }

    @Test
    void test2() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/star_6.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(3, graph.calculateTotalCrossings());
    }

    @Test
    void test3() throws FileNotFoundException {
        File file = new File("test.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(1, graph.calculateTotalCrossings());
    }

    @Test
    void test4() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/grid_9_shuffled.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(25, graph.calculateTotalCrossings());
    }

    @Test
    void test5() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/matching_4_4.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(2, graph.calculateTotalCrossings());
    }

    @Test
    void test6() throws FileNotFoundException {
        File file = new File("barycenter.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(5, graph.calculateTotalCrossings());
    }

    @Test
    void test7() throws FileNotFoundException {
        File file = new File("benchmarkset/extra_hard/1.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertTrue(graph.calculateTotalCrossings() > 27_000_000);
    }

}