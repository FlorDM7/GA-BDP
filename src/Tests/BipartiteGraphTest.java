package Tests;

import Graph.BipartiteGraph;
import Graph.Input;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BipartiteGraphTest {

    @Test
    void test1() throws FileNotFoundException {
        File file = new File("benchmarkset/test.txt");
        BipartiteGraph graph = Input.textToGraph(file);

        // test input sequence A
        List<Integer> outputA = new ArrayList<Integer>();
        outputA.add(1);
        outputA.add(2);
        outputA.add(3);
        assertEquals(graph.getSequenceA(), outputA);

        // test input sequence B
        List<Integer> outputB = new ArrayList<>();
        outputB.add(4);
        outputB.add(5);
        outputB.add(6);
        assertEquals(graph.getSequenceB(), outputB);

        // test sequence to height for A
        HashMap<Integer, Integer> outputMapA = new HashMap<>();
        outputMapA.put(1, 3);
        outputMapA.put(2, 2);
        outputMapA.put(3, 1);
        assertEquals(graph.sequenceToHeightA(graph.getSequenceA()), outputMapA);

        // test sequence to height for B
        HashMap<Integer, Integer> outputMapB = new HashMap<>();
        outputMapB.put(4, 3);
        outputMapB.put(5, 2);
        outputMapB.put(6, 1);
        assertEquals(graph.sequenceToHeightB(graph.getSequenceB()), outputMapB);

        // test height to sequence (both A and B)
        assertEquals(graph.heightToSequenceA(graph.getHeightA()), outputA);
        assertEquals(graph.heightToSequenceB(graph.getHeightB()), outputB);
    }

    @Test
    void test2() throws FileNotFoundException {
        File file = new File("benchmarkset/splitting.txt");
        BipartiteGraph graph = Input.textToGraph(file);

        // test input sequence A
        List<Integer> outputA = new ArrayList<Integer>();
        outputA.add(1);
        outputA.add(2);
        outputA.add(3);
        outputA.add(4);
        assertEquals(graph.getSequenceA(), outputA);

        // test input sequence B
        List<Integer> outputB = new ArrayList<>();
        outputB.add(5);
        outputB.add(6);
        outputB.add(7);
        assertEquals(graph.getSequenceB(), outputB);

        // test sequence to height for A
        HashMap<Integer, Integer> outputMapA = new HashMap<>();
        outputMapA.put(1, 4);
        outputMapA.put(2, 3);
        outputMapA.put(3, 2);
        outputMapA.put(4, 1);
        assertEquals(graph.sequenceToHeightA(graph.getSequenceA()), outputMapA);

        // test sequence to height for B
        HashMap<Integer, Integer> outputMapB = new HashMap<>();
        outputMapB.put(5, 3);
        outputMapB.put(6, 2);
        outputMapB.put(7, 1);
        assertEquals(graph.sequenceToHeightB(graph.getSequenceB()), outputMapB);

        // test height to sequence (both A and B)
        assertEquals(graph.heightToSequenceA(graph.getHeightA()), outputA);
        assertEquals(graph.heightToSequenceB(graph.getHeightB()), outputB);
    }

}
