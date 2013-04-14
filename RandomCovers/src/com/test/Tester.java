package com.test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import algorithms.cover.*;
import algorithms.graph.*;
import algorithms.voronoi.*;

public class Tester {

	
	private static int numReaders = 10; 
	private static int numTags = 20; 
	private static int maxX = 30; 
	private static int maxY = 30; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CoveringCircles c = new CoveringCircles();
		c.generateRandomPoints(numReaders, maxX,maxY, 'r'); 
		
		if (c.points.size() == 0) { System.out.println("No points "); }
		if (! c.generateVoronoiSites() ) {System.out.println("Could not generate V sites"); } 
		c.setMaxDistance(); 
		
		
		System.out.printf("Max distance : %f ", c.maxCoverDistance );
		// System.exit(0);
		
		// creating graph. 
		UDG udg = new UDG(); 
		udg.createAdjGraph(c.points, c.maxCoverDistance);
		udg.printAdjGraph();
		
		
		
		// is graph connected ? 
		if (udg.isConnected()) System.out.println("Graph is connected"); 
		else { System.out.println("Graph is not connected"); }
	
		
		// Now, generate a set of points.
		c.generateRandomPoints(numTags, maxX, maxY, 't'); 
		BipartiteUDG bip = new BipartiteUDG();
		bip.createAdjGraph(c.points, c.tags, c.maxCoverDistance);
		bip.printAdjGraph();
		if (bip.isBipCovered()) System.out.println("Bibpartite graph is covering "); 
		else System.out.println("Bibpartite graph is not covering");
		
		// converting bipartite into lists; -- to make it ready for set-cover
		bip.adjToListGraph();
		bip.printListGraph();  
		
		ArrayList<Integer> tags = new ArrayList<Integer>(); 
		for (int i = 0; i < c.tags.size(); i++) tags.add(i); 
		
		
		// Computing the set cover
		ArrayList<Integer> coverSets = SetCover.execute(bip.listGraph, tags);
		
		
		
		System.out.println("Printing the set cover indices");
		for (int i = 0; i < coverSets.size(); i++) { 
			System.out.printf("%d ", coverSets.get(i));
		}
		System.out.println();
		
		
		// printing the cover sets elements: 
		System.out.println("Printing the set cover elements");
		for (int i = 0; i < coverSets.size(); i++) { 
			System.out.printf("%d: ", coverSets.get(i)); 
			for (int j = 0; j < bip.listGraph.get(coverSets.get(i)).size(); j++ ) { 
				System.out.printf("%d ", udg.listGraph.get(coverSets.get(i)).get(j) );
			}
			System.out.println();
		}
		
		
		// Result: 
		System.out.printf("Out of %d reader, our set cover is %d \n", 
				numReaders, coverSets.size());
		
		
		
	}
	
	


	

	// TODO: shrinkage of points toward the center .. 
	// It is just an idea.
	public static Point2D.Double shrinkPoint(Point2D.Double c, Point2D.Double po,  double scale) { 
		Point2D.Double pn = new Point2D.Double(); 
		pn.x = c.x + (c.x - po.x) * scale; 
		pn.y = c.y + (c.y - po.y) * scale; 
		
		return pn; 
	}


}
