package algorithms.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class BipartiteUDG extends Graph {

	public ArrayList<Point2D> SPoints, TPoints; 


	public BipartiteUDG() {
		super();
		SPoints = new ArrayList<Point2D>(); 
		TPoints = new ArrayList<Point2D>();
	}
	
	@SuppressWarnings("unused")
	public void createAdjGraph(ArrayList<Point2D> S, ArrayList<Point2D> T, double range) { 
		initAdjGraph(S.size(), T.size());
		
		if (S == null) { System.out.printf("S not initialized \n"); return; }
		if (T == null) { System.out.printf("T not initialized \n"); return; }
		
		// Getting the SPoints and TPoints
		SPoints.addAll(S);
		TPoints.addAll(T);
		
		for (int i = 0; i < S.size(); i++) { 
			for (int j = 0; j < T.size(); j++) { 
				boolean input =  (S.get(i).distance(T.get(j)) <= range);
				adjGraph.get(i).set(j, input);
			}
		}
	}

	
	// 
	public boolean isBipCovered() {
		int w = adjGraph.size(); 
		int l = adjGraph.get(0).size();
		boolean tagCovered = false; 
		
		for (int j = 0; j < l; j++) { 
			
			tagCovered = false;
			for (int i = 0; i < w; i++) { 
				if (adjGraph.get(i).get(j) == true) { tagCovered = true; break; }
			}
			
			
			if (! tagCovered) { 
				System.out.printf("Tag %d is not covered \n", j);
				return false; 
			}
		}
		
		return true;

	}

	// work only on list graphs.
	public ArrayList<Integer> findCoveredReaders() {
		
		ArrayList<Integer> cr = new ArrayList<Integer>();
		for (int i = 0; i < listGraph.size(); i++) { 
			if (listGraph.size() > 0) { 
				cr.add(i);
			} 
			
		}
		return cr; 
	}
	
	
}
