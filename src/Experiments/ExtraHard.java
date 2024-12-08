package Experiments;

import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileNotFoundException;

public class ExtraHard {

    public static void main(String[] args) throws FileNotFoundException {
        for (int i=1; i<=200; i++) {
            BipartiteGraph graph = Input.textToGraph(new File("benchmarkset/extra_hard/" + i + ".gr"));
            System.out.println(i);
            long before = graph.calculateTotalCrossings();
            System.out.println(before);
            long after = graph.runFirstHeuristic().getKey();
            System.out.println(after);
        }
    }
}
