package Tests;

import Graph.BipartiteGraph;
import Graph.Input;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TabuTest {

    static int iterations;

    @BeforeAll
    static void init() {
        iterations = 10;
    }

    @Test
    void test1() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/cycle_8_shuffled.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        Map.Entry<Long, List> solution = graph.runMetaheuristicTest(iterations);
        assertTrue(solution.getKey() < crossings);
    }

    @Test
    void test2() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/star_6.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test3() throws FileNotFoundException {
        File file = new File("test.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test4() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/grid_9_shuffled.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test5() throws FileNotFoundException {
        File file = new File("benchmarkset/tiny_test_set/matching_4_4.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test6() throws FileNotFoundException {
        File file = new File("barycenter.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test7() throws FileNotFoundException {
        File file = new File("benchmarkset/extra_hard/1.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

    @Test
    void test8() throws FileNotFoundException {
        File file = new File("benchmarkset/extra_easy/1.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long crossings = graph.calculateTotalCrossings();
        assertTrue(graph.runMetaheuristicTest(iterations).getKey() < crossings);
    }

}
