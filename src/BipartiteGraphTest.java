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
        File file = new File("test.txt");
        BipartiteGraph graph = Input.textToGraph(file);
        List<Integer> outputA = new ArrayList<Integer>();
        outputA.add(1);
        outputA.add(2);
        outputA.add(3);
        assertEquals(graph.getSequenceA(), outputA);
        List<Integer> outputB = new ArrayList<>();
        outputB.add(4);
        outputB.add(5);
        outputB.add(6);
        assertEquals(graph.getSequenceB(), outputB);
        HashMap<Integer, Integer> outputMapA = new HashMap<>();
        outputMapA.put(1, 3);
        outputMapA.put(2, 2);
        outputMapA.put(3, 1);
        assertEquals(graph.sequenceToHeightA(graph.getSequenceA()), outputMapA);
        HashMap<Integer, Integer> outputMapB = new HashMap<>();
        outputMapB.put(4, 3);
        outputMapB.put(5, 2);
        outputMapB.put(6, 1);
        assertEquals(graph.sequenceToHeightB(graph.getSequenceB()), outputMapB);
        assertEquals(graph.heightToSequenceA(graph.getHeightA()), outputA);
        assertEquals(graph.heightToSequenceB(graph.getHeightB()), outputB);
    }

}
