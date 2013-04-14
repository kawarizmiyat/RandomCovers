package algorithms.graph;

import java.util.ArrayList;

public class DepthFirstSearch {

	private static boolean D = false;
	
	
	// Depth-First search of the graph. (starting from node 0)
	// returns a boolean arrayList that say which node were traversed. 
	public static ArrayList<Boolean> execute(
			ArrayList<ArrayList<Boolean>> adjGraph) {


		ArrayList<Boolean> visited = new ArrayList<Boolean>(); 
		for (int i = 0; i < adjGraph.size(); i++) { 
			visited.add(false);
		}
		
		// System.out.print("Starting DFS at 0 \n");
		DFSHelper(adjGraph, 0, visited); 
		
		return visited;
	
	
	}

	
	private static void DFSHelper(ArrayList<ArrayList<Boolean> > graph, int i, ArrayList<Boolean> visited) {
		
		if (D) { System.out.printf("Marking %d as true \n", i); }
		visited.set(i, true); 		// mark visited
		
		// TODO: change j to i+1; (test)
		// for (int j = 0; j < graph.get(i).size(); j++) {
		for (int j = i + 1; j < graph.get(i).size(); j++)
			if (graph.get(i).get(j) && visited.get(j) == false) { 
				DFSHelper(graph, j, visited);
			}
	}
		
}

	

