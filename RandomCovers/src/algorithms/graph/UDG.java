package algorithms.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UDG {

	// The visibile of these variables may be changed later. 
	public ArrayList<ArrayList<Boolean> >  graph; 
	public ArrayList<ArrayList<Boolean> >  bipGraph; 
	public ArrayList<ArrayList<Integer> >  listBipGraph;
	
	public UDG() { 
		graph = new ArrayList<ArrayList<Boolean> >();
		bipGraph = new ArrayList<ArrayList<Boolean> >();
		listBipGraph = new ArrayList<ArrayList <Integer> >();
	}
	
	

	
	public void initGraph(ArrayList< ArrayList<Boolean> > graph, int w, int l) { 
		if (graph == null) { graph = new ArrayList< ArrayList<Boolean> >(); }
		else { graph.clear(); }
		
		for (int i = 0; i < w; i++) { 
			ArrayList<Boolean> t = new ArrayList<Boolean>();
			for (int j = 0; j < l; j++) { 
				t.add(false);
			}
			graph.add(t);
		}
		
		
	}
	
	
	public void createBipartiteGraph(ArrayList<Point2D> readers, ArrayList<Point2D> tags, double range) { 
		initGraph(bipGraph, readers.size(), tags.size());
		
		if (tags == null) { System.out.printf("tags not init \n"); }
		if (readers == null) { System.out.printf("readers not init \n"); }
		if (bipGraph == null) { System.out.printf("bipGraph is not init \n"); }
		
		for (int i = 0; i < readers.size(); i++) { 
			for (int j = 0; j < tags.size(); j++) { 
				boolean input =  (readers.get(i).distance(tags.get(j)) <= range);
				bipGraph.get(i).set(j, input);
			}
		}
	}
	
	public void createGraph(ArrayList<Point2D> points, double range) { 
		
		// initialize the graph. 
		initGraph(graph, points.size(), points.size()); 

		// Creating graph from points - with a given range
		for (int i = 0; i < points.size(); i++ ) {
			
			for (int j = i; j < points.size(); j++) { 
				if (points.get(i).distance(points.get(j)) <= range  && (i != j) ) { 
					graph.get(i).set(j, true);
					graph.get(j).set(i, true);
					
				} else { 
					graph.get(i).set(j, false);
					graph.get(j).set(i, false);				
				}
			}
		}

		
	}

	public void print() { 
		System.out.println("Printing graph ");
		System.out.print(this); 
	}

	public void printBipartiteGraph() {
		if (bipGraph == null) { return; }
		
		for (int i = 0; i < bipGraph.size(); i++) { 
			for (int j = 0; j < bipGraph.get(i).size(); j++) {
				if (bipGraph.get(i).get(j)) System.out.print("1 "); 
				else System.out.print("0 ");
			}
			System.out.println();
		}
	}
	
	public String toString() { 
		
		String result = "";

		for (int i = 0; i < graph.size(); i++ ) {
			for (int j = 0; j < graph.get(i).size(); j++) {
				if (graph.get(i).get(j) ) result += "1 "; 
				else result += "0 ";
				
			}
			result += "\n";
		}
		
		return result; 
	}
	
	public boolean isConnected() { 
		
		// execute Depth-First Search
		ArrayList<Boolean> visited = DFS(graph); 
		
		
		// check for errors 
		if (visited.size() != graph.size() ) {
			System.out.printf("There is an error v(%d), g(%d) ", visited.size(), graph.size());
			System.exit(0);
		}
		
		// verify if every node is visited by DFS
		for (int i = 0; i < visited.size(); i++) { 
			if (! visited.get(i)) {System.out.printf("node %d was not visited \n", i); return false; } 
		}
		
		
		return true; 
		
	}

	
	// Depth-First search of the graph. (starting from node 0)
	// returns a boolean arrayList that say which node were traversed. 
	public ArrayList<Boolean> DFS(ArrayList<ArrayList<Boolean> > graph) {
		ArrayList<Boolean> visited = new ArrayList<Boolean>(); 
		for (int i = 0; i < graph.size(); i++) { 
			visited.add(false);
		}
		
		// System.out.print("Starting DFS at 0 \n");
		DFSHelper(graph, 0, visited); 
		
		return visited;
		
	}
	
	
	private void DFSHelper(ArrayList<ArrayList<Boolean> > graph, int i, ArrayList<Boolean> visited) {
		
		System.out.printf("Marking %d as true \n", i);
		visited.set(i, true); 		// mark visited
		
		// TODO: change j to i+1; (test)
		for (int j = 0; j < graph.get(i).size(); j++) {
			if (graph.get(i).get(j) && visited.get(j) == false) { 
				DFSHelper(graph, j, visited);
			}
		}
		
	}

	public boolean isBipCovered() {
		int w = bipGraph.size(); 
		int l = bipGraph.get(0).size();
		boolean tagCovered = false; 
		
		for (int j = 0; j < l; j++) { 
			
			tagCovered = false;
			for (int i = 0; i < w; i++) { 
				if (bipGraph.get(i).get(j) == true) { tagCovered = true; break; }
			}
			
			
			if (! tagCovered) { 
				System.out.printf("Tag %d is not covered \n", j);
				return false; 
			}
		}
		
		return true;

	}
	
	public void convertBipartiteToList() {
		
		listBipGraph.clear(); 
		
		for (int i = 0; i < bipGraph.size(); i++) { 
			ArrayList<Integer> t = new ArrayList<Integer>();
			for (int j = 0; j < bipGraph.get(i).size(); j++) {
				if (bipGraph.get(i).get(j)) t.add(j); 
			}
			listBipGraph.add(t);
		}
		
	}
	
	public void printBipartiteList() {
		System.out.println("Printing a listed Bipartite graph: ");
		for (int i = 0; i < listBipGraph.size(); i++) {
			for (int j = 0; j < listBipGraph.get(i).size(); j++) { 
				System.out.printf("%d ", listBipGraph.get(i).get(j));
			}
			System.out.println();
		}
	}
	
}
