package Experiments;

import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static Experiments.FirstExperiment.printTime;

public class TabuSize {

    public static void main(String[] args) throws IOException {
        File[] files = new File("benchmarkset/GraphBase_first_experiment").listFiles();
        FileWriter myWriter = new FileWriter("tabusize.txt");

        int iterations = 200;

        System.out.println("factor 0");
        runExperiment(files, myWriter, 0, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 0.01");
        runExperiment(files, myWriter, 0.01F, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 0.1");
        runExperiment(files, myWriter, 0.1F, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 0.25");
        runExperiment(files, myWriter, 0.25F, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 0.5");
        runExperiment(files, myWriter, 0.5F, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 0.75");
        runExperiment(files, myWriter, 0.75F, iterations);
        System.out.println();
        myWriter.write("\n");

        System.out.println("factor 1");
        runExperiment(files, myWriter, 1, iterations);
        System.out.println();

        myWriter.close();
    }

    public static void runExperiment(File[] files, FileWriter myWriter, float factor, int iterations) throws IOException {
        int edges = 10;
        int j = 0;
        long average = 0;
        long total = 0;
        long original = 0;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += graph.runMetaheuristicTest(iterations, (int) (iterations*factor), graph.getNumberOfVertices(), "Normal").getKey();
            j++;
            if (j % 100 == 0) {
                total += average;
                double averageCrossings = (double) average / 100;
                original = original / 100;
                long improvement = (long) ((1 - averageCrossings/original) * 100);
                System.out.println("Average crossings with " + edges + " edges and with 10 nodes:\t" + averageCrossings + "\t(" + original + ")" + "\t(" + improvement + "%)");
                myWriter.write(averageCrossings + " & ");
                average = 0;
                original = 0;
                j = 0;
                edges += 10;
            }
        }
        long stopTime = System.currentTimeMillis();
        printTime(startTime, stopTime, total);
    }

}
