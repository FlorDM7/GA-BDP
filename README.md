# Bipartite Drawing Problem using heuristics
### Flor De Meulemeester

Een "move" bij barycenter is een volledige permutatie van de knopen, dit werkt goed bij weinig knopen, maar is heel traag bij veel knopen.
Een "move" bij tabu list is een wissel van om het even twee knopen uit dezelfde set.
Bij splitting worden ook telkens twee knopen gewisseld, maar wel meerdere in één iteratie.

A bipartite graph is represented as a .gr file. In the benchmarkset you can find plenty of examples of the format.
The Input class contains a method to convert such a file into a Java BipartiteGraph object.
Once you have done that you can perform a heuristic search which one of the following methods in BipartiteGraph:
- `runFirstHeuristic()` is implemented with the BaryCenter heuristic
- `runSecondHeuristic()` is implemented with the Splitting heuristic
- `runMetaHeuristic()` is implemented with Tabu Search

The code for the implementation is found in the respective Java class.

Bepaalde methoden zijn gekopieerd om voor ofwel subset A ofwel subset B te werken, nu probeer ik algemenere methoden te maken