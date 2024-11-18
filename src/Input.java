import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class has a method to convert the given input format used in the benchmark set to a BipartiteGraph object.
 *
 * @author Flor De Meulemeester
 */
public class Input {

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("test.txt");
        BipartiteGraph graph = textToGraph(file);
        System.out.println(graph.getVertices());
    }

    public static BipartiteGraph textToGraph(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String line = scanner.nextLine();
        while (line.charAt(0) != 'p') {
            line = scanner.nextLine();
        }
        String[] numbers = line.split(" ");
        int n0 = Integer.parseInt(numbers[2]);
        int n1 = Integer.parseInt(numbers[3]);
        int m = Integer.parseInt(numbers[4]);
        BipartiteGraph graph = new BipartiteGraph(n0 + n1, n0, n1, m);
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.charAt(0) == 'c') {
                continue;
            }
            String[] indexes = line.split(" ");
            int a = Integer.parseInt(indexes[0]);
            int b = Integer.parseInt(indexes[1]);
            graph.addEdge(a, b);
        }
        scanner.close();
        return graph;
    }

}
