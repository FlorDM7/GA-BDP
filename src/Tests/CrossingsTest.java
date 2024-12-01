package Tests;

import java.io.File;
import java.io.FileNotFoundException;
import Graph.BipartiteGraph;
import Graph.Input;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CrossingsTest {

    /**
     * TOTAL CROSSINGS
     */

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
        File file = new File("splitting.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(5, graph.calculateTotalCrossings());
    }

    @Test
    void test8() throws FileNotFoundException {
        File file = new File("benchmarkset/extra_hard/1.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        assertTrue(graph.calculateTotalCrossings() > 27_000_000);
    }

    /**
     * CROSSINGS DIFFERENCE
     */

    @Test
    void test9() throws FileNotFoundException {
        File file = new File("barycenter.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(5, graph.calculateTotalCrossings());
        // when we swap 1 en 2, we will improve by 2 crossings
        assertEquals(2, graph.differenceCrossingsAfterSwapA(1,2));
        // when we swap 5 en 6, we will improve by 2 crossings
        assertEquals(2, graph.differenceCrossingsAfterSwapB(6,5));
        assertEquals(1, graph.calculateTotalCrossings()); // the previous methods acctualy perform the swaps
    }

    @Test
    void test10() throws FileNotFoundException {
        File file = new File("splitting.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        assertEquals(5, graph.calculateTotalCrossings());
        // we swap 5 en 6 we imrove by 2
        assertEquals(2, graph.differenceCrossingsAfterSwapB(5,6));
    }

    @Test
    void test11() throws FileNotFoundException {
        File file = new File("benchmarkset/extra_hard/1.gr");
        BipartiteGraph graph = Input.textToGraph(file);
        long before = graph.calculateTotalCrossings();

        // we do a swap in group A
        long difference = graph.differenceCrossingsAfterSwapA(5,230);
        long after = graph.calculateTotalCrossings();
        assertEquals(before - after, difference);
        graph.swapA(5,230); // undo swap

        // we do a swap in group B
        difference = graph.differenceCrossingsAfterSwapB(10572,8319);
        after = graph.calculateTotalCrossings();
        assertEquals(before - after, difference);
        graph.swapB(10572,8319); // undo swap

        assertEquals(before, graph.calculateTotalCrossings());
    }

}