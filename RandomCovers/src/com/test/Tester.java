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
	private static int numTags = 50; 
	private static int maxX = 30; 
	private static int maxY = 30; 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CoveringCircles readers = new CoveringCircles();
		readers.generateRandomPoints(numReaders, maxX,maxY, 'r'); 
		
		if (readers.points.size() == 0) { System.out.println("No points "); }
		if (! readers.generateVoronoiSites() ) {System.out.println("Could not generate V sites"); } 
		readers.setMaxDistance(); 
		
		
		System.out.printf("Max distance : %f ", readers.maxCoverDistance );
		// System.exit(0);
		
		// creating graph. 
		UDG udg = new UDG(); 
		udg.createAdjGraph(readers.points, readers.maxCoverDistance);
		udg.printAdjGraph();
		
		
		
		// is graph connected ? 
		if (udg.isConnected()) System.out.println("Graph is connected"); 
		else { System.out.println("Graph is not connected"); }
	
		
		// Now, generate a set of points.
		
		readers.generateRandomPoints(numTags, maxX, maxY, 't'); 
		BipartiteUDG bip = new BipartiteUDG();
		bip.createAdjGraph(readers.points, readers.tags, readers.maxCoverDistance);
		bip.printAdjGraph();
		if (bip.isBipCovered()) System.out.println("Bibpartite graph is covering "); 
		else System.out.println("Bibpartite graph is not covering");
		
		// converting bipartite into lists; -- to make it ready for set-cover
		bip.adjToListGraph();
		bip.printListGraph();  
		
		ArrayList<Integer> tags = new ArrayList<Integer>(); 
		for (int i = 0; i < readers.tags.size(); i++) tags.add(i); 
		
		
		// Computing the set cover
		ArrayList<Integer> coverSets = SetCover.execute(bip.listGraph, tags);
		
		
		
		System.out.println("Printing the set cover indices");
		for (int i = 0; i < coverSets.size(); i++) { 
			System.out.printf("%d ", coverSets.get(i));
		}
		System.out.println();
		
		
		System.out.println("Printing the set cover sets"); 
		for (int i = 0; i < coverSets.size(); i++) { 
			for (int j = 0; j < bip.listGraph.get(coverSets.get(i)).size(); j++) {
				System.out.printf("%d ", bip.listGraph.get(coverSets.get(i)).get(j)); 
			}
			
			System.out.println();
		}
		
		// Result: 
		System.out.printf("Out of %d reader, our set cover is %d \n", 
				numReaders, coverSets.size());
		
		
		// TODO: 
		// 1. create a backbone of readers that cover ever tags. 
		// 2. Add numReaders-coverSets.size() addition readers. 
		// 3. Make sure that each of which covers at least
		// 4. output.
		ArrayList<Point2D> backbone = createBackbone(coverSets, readers); 
		
		ArrayList<Point2D> coveringReaders = createCoveringReaders(numReaders, backbone, readers.tags, maxX, maxY, readers.maxCoverDistance );
		BipartiteUDG coveringBip = new BipartiteUDG();
		coveringBip.createAdjGraph(coveringReaders, readers.tags, readers.maxCoverDistance);
		
		
		
		coveringBip.adjToListGraph();
		coveringBip.printListGraph();		
		
		
		
				
		
	}
	
	// Create a set of covering reader for the sets T, 
	// build it over the set of readers R 
	public static ArrayList<Point2D> createCoveringReaders(int k, ArrayList<Point2D> backbone, 
			ArrayList<Point2D>  tags , 
			int maxX, 
			int maxY, 
			double range) {
		
		boolean lD = true; 			// local debugging
		if (lD) { System.out.println("At createCoveringReaders"); }
		
		ArrayList<Point2D> coveringReaders = new ArrayList<Point2D>();
		coveringReaders.addAll(backbone);
		
		
		int iteration = 0; 
		while (coveringReaders.size() < k) {
			
			
			ArrayList<Point2D> additionalPoints = createAdditionalPoints(
					k - coveringReaders.size(), 
					maxX, 
					maxY);
			
			coveringReaders.addAll(additionalPoints);

			
			BipartiteUDG bip = new BipartiteUDG();
			bip.createAdjGraph(coveringReaders, tags, range);
			bip.adjToListGraph();
			
			if (lD) {
				System.out.println("The graph of the new bipartite graph");
				bip.printListGraph();
			}
			
			ArrayList<Integer> coveredReaders = bip.findCoveredReaders(); 
			if (lD) { 
				System.out.printf("Size of coveredReaders is %d \n", coveredReaders.size());
			}
			
			
			// remove all uncovered nodes from coveringReaders. 
			ArrayList<Point2D> copy = new ArrayList<Point2D>(); 
			for (int i = 0; i < coveredReaders.size(); i++ ) {
				copy.add(coveringReaders.get(coveredReaders.get(i)));
			}
			
			coveringReaders.clear();
			coveringReaders.addAll(copy);
			
			
			if (lD) { System.out.printf("The size of coveringReaders is %d \n", coveringReaders.size());}
			iteration ++;
		}
		
		System.out.printf("Quitting createCoveringReaders afer %d iterations \n", iteration);
		
		return coveringReaders;
		
	}

	

	private static ArrayList<Point2D> createAdditionalPoints(int k, int X,
			int Y) {

		ArrayList<Point2D> additionalPoints = new ArrayList<Point2D>();
		CoveringCircles c1 = new CoveringCircles();
		c1.generateRandomPoints(k, X ,Y, 'r'); 
		additionalPoints.addAll(c1.points);
		
		return additionalPoints; 
		
	}






	private static ArrayList<Point2D> createBackbone(
			ArrayList<Integer> coverSets, CoveringCircles c) {

		ArrayList<Point2D> backbone = new ArrayList<Point2D>(); 
		for (int i = 0; i < coverSets.size(); i++) { 
			backbone.add(c.points.get(coverSets.get(i)));
		}
		
		return backbone;
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
