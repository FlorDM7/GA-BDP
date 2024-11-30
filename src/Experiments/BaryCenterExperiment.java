package Experiments;

import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileNotFoundException;

public class BaryCenterExperiment {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("benchmarkset/GraphBase_first_experiment").listFiles();
        int edges = 10;
        int j = 0;
        int average = 0;
        long original = 0;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += graph.runFirstHeuristic().getKey();
            j++;
            if (j % 100 == 0) {
                float averageCrossings = (float) average / 100;
                original = original / 100;
                long improvement = (long) ((1 -averageCrossings/original) * 100);
                System.out.println("Average crossings with " + edges + " edges:\t" + averageCrossings + "\t(" + original + ")" + "\t(" + improvement + "%)");
                average = 0;
                original = 0;
                j = 0;
                edges += 10;
            }
        }
        long stopTime = System.currentTimeMillis();
        float elapsedTime = (float) (stopTime - startTime) /1000;
        System.out.println("Elapsed time: " + elapsedTime + " s");
    }
}
