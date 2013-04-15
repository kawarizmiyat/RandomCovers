package algorithms.cover;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import algorithms.voronoi.*;

public class CoveringCircles {

	public boolean D = false;
	public double maxX, maxY; 
	public ArrayList<Point2D> points; 
	public ArrayList< ArrayList<Point2D> > vorSites; 
	public double maxCoverDistance = 0.0;
	public ArrayList<Point2D> tags; 
	
	public CoveringCircles() { 
		vorSites = new ArrayList< ArrayList<Point2D> >(); 
		points = new ArrayList<Point2D>();
		tags = new ArrayList<Point2D>();

	}
	
	
	
	
	public void generateRandomPoints(int numberNodes, int maxX, int maxY, char r) { 

		
		this.maxX = maxX; 
		this.maxY = maxY; 
		
		ArrayList<Point2D> tempPoints = new ArrayList<Point2D>();
		
		// generate random points, 
		for (int i = 0; i < numberNodes; i++) { 
			double x =  new Random().nextDouble() * maxX;
			double y = new Random().nextDouble() * maxY; 
			tempPoints.add(new Point2D.Double(x,y));
			
			if (D) {
				System.out.printf("new point (%f, %f) \n", x, y);
			}
		}
		
		if (r == 'r') { 
			points.clear(); 
			points.addAll(tempPoints);
		} else if (r == 't') { 
			tags.clear();
			tags.addAll(tempPoints);
		} else {
			System.out.println("Error: it is not specified which arrayList should be modified. options: r or t");
		}

	}

	public boolean generateVoronoiSites() {
		if (points == null) { return false; }
		if (points.size() == 0) { return false; }
		
		if (D) { System.out.printf("Generating Voronoi Sites for %d points \n", points.size()); }
		
		double minDistance = 1; 
		Voronoi v = new Voronoi(minDistance);
		
		double[] xS = new double[points.size()];
		double[] yS = new double[points.size()];
		for (int i = 0; i < points.size(); i++) { 
			xS[i] = points.get(i).getX(); 
			yS[i] = points.get(i).getY();
			System.out.printf("(%f,%f) ", xS[i], yS[i]);
		}
		
		
		vorSites.clear();
		for (int i = 0; i < points.size() ; i++) { 
			vorSites.add(new ArrayList<Point2D>());
		}
		
		
		
		List<GraphEdge> result = v.generateVoronoi(xS, yS, 0, maxX, 0, maxY);
		System.out.printf("voronois has %d edges \n", result.size());
		
		for (int i = 0; i < result.size(); i++) { 
			GraphEdge e = result.get(i);
			Point2D p1 = new Point2D.Double(e.x1, e.y1); 
			Point2D p2 = new Point2D.Double(e.x2, e.y2);
			
			vorSites.get(e.site1).add(p1);
			vorSites.get(e.site1).add(p2);
			vorSites.get(e.site2).add(p1); 
			vorSites.get(e.site2).add(p2);
		}
		return true; 
	}

	public double getMaxDistance() { 
		return maxCoverDistance; 
	}
	
	public void setMaxDistance() { 
		
		maxCoverDistance = 0.0; 
		
		System.out.println("Pringint table "); 
		for (int i = 0; i < vorSites.size(); i++) { 
			
			for (int j = 0; j < vorSites.get(i).size(); j++) {
				double tempDistance = points.get(i).distance(vorSites.get(i).get(j)); 
				
				if (tempDistance > maxCoverDistance) { maxCoverDistance = tempDistance; }
				
			}
		}
		
	
	}
	
	
}
