package algorithms.graph;

import java.util.ArrayList;

public class Graph {

	public ArrayList<ArrayList<Boolean> >  adjGraph;
	public ArrayList<ArrayList<Integer> >  listGraph;
	
	public Graph() { 
		adjGraph = new ArrayList<ArrayList<Boolean> >();
		listGraph  = new ArrayList<ArrayList<Integer> >();
	}
	
	
	// Initialize the Adjacency matrix of a graph
	public void initAdjGraph(int w, int l) { 
		if (adjGraph == null) { adjGraph = new ArrayList< ArrayList<Boolean> >(); }
		else { adjGraph.clear(); }
		
		for (int i = 0; i < w; i++) { 
			ArrayList<Boolean> t = new ArrayList<Boolean>();
			for (int j = 0; j < l; j++) { 
				t.add(false);
			}
			adjGraph.add(t);
		}
	}
	

	// Initialize the List graph: 
	// TODO
	public void initListGraph(int n) { 
		for (int i = 0; i < n; i++) { 
			ArrayList<Integer> t = new ArrayList<Integer>();
			listGraph.add(t);
		}
	}

	
	// Printing the adjacency matrix
	public void printAdjGraph() { 
		
		String result = "";

		for (int i = 0; i < adjGraph.size(); i++ ) {
			for (int j = 0; j < adjGraph.get(i).size(); j++) {
				if (adjGraph.get(i).get(j) ) result += "1 "; 
				else result += "0 ";
				
			}
			result += "\n";
		}
		
		System.out.print(result);
	}

	
	// Printing List graph: 
	public void printListGraph() {
		
	
		System.out.println("Printing a listed Bipartite graph: ");
		System.out.print(strListGraph());
		

	}

	
	// Convert Adjacency to List
	public void adjToListGraph() {
		
		listGraph.clear(); 
		
		for (int i = 0; i < adjGraph.size(); i++) { 
			ArrayList<Integer> t = new ArrayList<Integer>();
			for (int j = 0; j < adjGraph.get(i).size(); j++) {
				if (adjGraph.get(i).get(j)) t.add(j); 
			}
			listGraph.add(t);
		}
		
	}
	
	
	// is graph connected: (accept adj only)
	public boolean isConnected() { 
		
		// execute Depth-First Search
		ArrayList<Boolean> visited = DepthFirstSearch.execute(adjGraph); 
		
		// check for errors 
		if (visited.size() != adjGraph.size() ) {
			System.out.printf("There is an error v(%d), g(%d) ", visited.size(), adjGraph.size());
			System.exit(0);
		}
		
		// verify if every node is visited by DFS
		for (int i = 0; i < visited.size(); i++) { 
			if (! visited.get(i)) {System.out.printf("node %d was not visited \n", i); return false; } 
		}
		
		
		return true; 
		
	}

	
	
	public String strListGraph() {
		String str = "";
		
		for (int i = 0; i < listGraph.size(); i++) {
			for (int j = 0; j < listGraph.get(i).size(); j++) { 
				str += listGraph.get(i).get(j); 
				str += " ";
				
			}
			str += "\n";
		}		
		return str;
	}
	
	
}
