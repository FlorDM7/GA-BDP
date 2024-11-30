package Graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class contains a method to convert the given input format used in the benchmark set to a BipartiteGraph object.
 * @author Flor De Meulemeester
 */
public class Input {

    /**
     * This method takes a file and returns a bipartite graph.
     *
     * @param   file
     *          A file object with the path to the graph file.
     * @return  A bipartite graph object that is created by the rules from the input file.
     * @throws  FileNotFoundException
     *          When the file is not found.
     */
    public static BipartiteGraph textToGraph(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        // the first line that's not a comment should start with a p
        while (line.charAt(0) != 'p') {
            line = scanner.nextLine();
        }
        String[] numbers = line.split(" ");
        int n0 = Integer.parseInt(numbers[2]); // n0 is the amount of vertices in A
        int n1 = Integer.parseInt(numbers[3]); // n1 is the amount of vertices in B
        int m = Integer.parseInt(numbers[4]); // m is the amount of edges in the graph
        BipartiteGraph graph = new BipartiteGraph(n0 + n1, n0, n1, m);
        // now we iterate over all the edges and add them one by one to the graph
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.charAt(0) == 'c') continue; // lines that start with a c are comments and should be ignored
            String[] indexes = line.split(" ");
            int a = Integer.parseInt(indexes[0]); // the index of the node in A
            int b = Integer.parseInt(indexes[1]); // the index of the node in B
            graph.addEdge(a, b);
        }
        scanner.close();
        graph.sortInput();
        return graph;
    }

}
