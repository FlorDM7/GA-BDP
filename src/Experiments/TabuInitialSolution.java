package Experiments;

import Graph.BipartiteGraph;
import Graph.Input;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static Experiments.FirstExperiment.printTime;

public class TabuInitialSolution {

    public static void main(String[] args) throws IOException {
        File[] originalFiles = new File("benchmarkset/GraphBase_third_experiment").listFiles();
        File[] files = new File("benchmarkset/GraphBase_third_experiment").listFiles();
        System.arraycopy(files, 100, files, 0,900);
        System.arraycopy(originalFiles, 0, files, 900, 100);
        FileWriter myWriter = new FileWriter("initialsolution3.txt");

        int iterations = 10;

        System.out.println("Normal");
        runExperiment(files, myWriter, 0.1F, iterations, "Normal");
        System.out.println();
        myWriter.write("\n");

        System.out.println("Barycenter");
        runExperiment(files, myWriter, 0.1F, iterations, "Barycenter");
        System.out.println();
        myWriter.write("\n");

        System.out.println("Splitting");
        runExperiment(files, myWriter, 0.1F, iterations, "Splitting");
        System.out.println();

        myWriter.close();
    }

    public static void runExperiment(File[] files, FileWriter myWriter, float factor, int iterations, String start) throws IOException {
        int edges = 10;
        int j = 0;
        long average = 0;
        long total = 0;
        long original = 0;
        long startTime = System.currentTimeMillis();
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += graph.runMetaheuristicTest(iterations, (int) (iterations*factor), graph.getNumberOfVertices(), start).getKey();
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
