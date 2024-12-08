# Bipartite Drawing Problem using heuristics - Flor De Meulemeester

A bipartite graph is represented as a .gr file. In the benchmarkset you can find plenty of examples of the format.
The Input class contains a method to convert such a Java File object (just with the pathname to the file), into a Java BipartiteGraph object.
The code for Input and BipartiteGraph are found in the Graph package.

Once you have done that you can perform a heuristic search with one of the following methods in BipartiteGraph:

- `runFirstHeuristic()` is implemented with the Barycenter heuristic
- `runSecondHeuristic()` is implemented with the Splitting heuristic
- `runMetaHeuristic()` is implemented with Tabu Search

These methods return a pair, the first element is the amount of crossings found and the second is a List containing the 
solution found in a List, first all the indices from A followed by the indices from B.
The code for the implementation is found in the respective Java class in the Heuristics package.
You could make a Main class and perform some of these heuristics.
