package Experiments;
import Graph.BipartiteGraph;
import java.io.File;
import java.io.FileNotFoundException;

public class ThirdExperiment {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("benchmarkset/GraphBase_third_experiment").listFiles();

        System.out.println("-=-=-=-Barycenter-=-=-=-");
        FirstExperiment.runExperiment(files, BipartiteGraph::runFirstHeuristic);

        System.out.println();
        System.out.println("-=-=-=-Splitting-=-=-=-");
        FirstExperiment.runExperiment(files, BipartiteGraph::runSecondHeuristic);

        System.out.println();
        int iterations = 10;
        System.out.println("-=-=-=-Tabu Search met " + iterations + " iteraties-=-=-=-");
        FirstExperiment.runExperiment(files,graph -> graph.runMetaheuristicTest(iterations));
    }
}

