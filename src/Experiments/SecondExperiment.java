package Experiments;
import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SecondExperiment {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("benchmarkset/GraphBase_second_experiment").listFiles();

        System.out.println("-=-=-=-Barycenter-=-=-=-");
        runExperiment(files, BipartiteGraph::runFirstHeuristic);

        System.out.println();
        System.out.println("-=-=-=-Tabu Search-=-=-=-");
        runExperiment(files,graph -> graph.runMetaheuristicTest(5));

        System.out.println();
        System.out.println("-=-=-=-Splitting-=-=-=-");
        runExperiment(files, BipartiteGraph::runSecondHeuristic);
    }

    public static void runExperiment(File[] files, Function<BipartiteGraph, Map.Entry<Long, List>> heuristic) throws FileNotFoundException {
        int edges = 10;
        int j = 0;
        int average = 0;
        long original = 0;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += heuristic.apply(graph).getKey();
            j++;
            if (j % 100 == 0) {
                float averageCrossings = (float) average / 100;
                original = original / 100;
                long improvement = (long) ((1 -averageCrossings/original) * 100);
                System.out.println("Average crossings with " + edges + " edges and with " + edges + " nodes:\t" + averageCrossings + "\t(" + original + ")" + "\t(" + improvement + "%)");
                average = 0;
                original = 0;
                j = 0;
                edges += 10;
            }
        }
        long stopTime = System.currentTimeMillis();
        float elapsedTime = (float) (stopTime - startTime) / 1000;
        float elapsedMinutes = (float) elapsedTime / 60;
        System.out.println("Elapsed time: " + elapsedTime + " s");
        System.out.println("Elapsed time: " + elapsedMinutes + " min");
    }
}

