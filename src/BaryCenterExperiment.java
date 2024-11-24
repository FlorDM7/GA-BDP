import java.io.File;
import java.io.FileNotFoundException;

public class BaryCenterExperiment {

    public static void main(String[] args) throws FileNotFoundException {
        File[] files = new File("benchmarkset/GraphBase_first_experiment").listFiles();
        int edges = 10;
        int j = 0;
        int average = 0;
        int original = 0;
        for (File file : files) {
            BipartiteGraph graph = Input.textToGraph(file);
            original += graph.calculateTotalCrossings();
            average += graph.runSecondHeuristic().getKey();
            j++;
            if (j % 100 == 0) {
                float averageCrossings = (float) average / 100;
                original = original / 100;
                long improvement = (long) ((original-averageCrossings)/original * 100);
                System.out.println("Average crossings with " + edges + " edges:\t" + averageCrossings + "\t(" + original + ")" + "\t(" + improvement + "%)");
                average = 0;
                original = 0;
                j = 0;
                edges += 10;
            }
        }
    }
}
