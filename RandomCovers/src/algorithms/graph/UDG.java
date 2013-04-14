package algorithms.graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UDG extends Graph {

	
	public UDG() { 
		super();

	}
	

	public void createAdjGraph(ArrayList<Point2D> points, double range) { 
		
		// initialize the graph. 
		initAdjGraph(points.size(), points.size()); 

		// Creating graph from points - with a given range
		for (int i = 0; i < points.size(); i++ ) {
			
			for (int j = i; j < points.size(); j++) { 
				if (points.get(i).distance(points.get(j)) <= range  && (i != j) ) { 
					adjGraph.get(i).set(j, true);
					adjGraph.get(j).set(i, true);
					
				} else { 
					adjGraph.get(i).set(j, false);
					adjGraph.get(j).set(i, false);				
				}
			}
		}

		
	}

	
}
