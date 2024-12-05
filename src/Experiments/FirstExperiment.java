package Experiments;

import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FirstExperiment {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("benchmarkset/GraphBase_first_experiment").listFiles();

        System.out.println("-=-=-=-Barycenter-=-=-=-");
        runExperiment(files, BipartiteGraph::runFirstHeuristic);

        System.out.println();
        System.out.println("-=-=-=-Splitting-=-=-=-");
        runExperiment(files, BipartiteGraph::runSecondHeuristic);

        System.out.println();
        int iterations = 2000;
        int tabusize = 15;
        System.out.println("-=-=-=-Tabu Search met " + iterations + " iteraties-=-=-=-");
        runExperiment(files,graph -> graph.runMetaheuristicTest(iterations));
    }

    public static void runExperiment(File[] files, Function<BipartiteGraph, Map.Entry<Long, List>> heuristic) throws FileNotFoundException {
        int edges = 10;
        int j = 0;
        long average = 0;
        long total = 0;
        long original = 0;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += heuristic.apply(graph).getKey();
            j++;
            if (j % 100 == 0) {
                total += average;
                double averageCrossings = (double) average / 100;
                original = original / 100;
                long improvement = (long) ((1 - averageCrossings/original) * 100);
                System.out.println("Average crossings with " + edges + " edges and with 10 nodes:\t" + averageCrossings + "\t(" + original + ")" + "\t(" + improvement + "%)");
                average = 0;
                original = 0;
                j = 0;
                edges += 10;
            }
        }
        long stopTime = System.currentTimeMillis();
        float elapsedTime = (float) (stopTime - startTime) / 1000;
        System.out.println("Average: " + (float) total / 1000);
        System.out.println("Elapsed time: " + elapsedTime + " s");
        System.out.println("Elapsed time: " + elapsedTime / 60 + " min");
    }
}
