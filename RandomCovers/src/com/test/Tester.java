package com.test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import algorithms.cover.*;
import algorithms.graph.*;
import algorithms.voronoi.*;

public class Tester {

	private static int numberNodes = 70; 
	private static int minDistance = 1; 
	private static int xMax = 30, yMax = 30; 
	
	
	// use a hash set -- from stackoverflow
	// note that Point2D has a overridden method equals. 
	public static void removeRedundancies(List<Point2D> l) {
		HashSet<Point2D> hs = new HashSet<Point2D>();
		hs.addAll(l);
		l.clear(); 
		l.addAll(hs);
	}
	
	
	// change precision of a double 
	public static double changePrecision(double input, int p) { 
		input = input * Math.pow(10,p); 
		input = Math.ceil(input); 
		input = input / Math.pow(10, p);
		
		return input; 
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CoveringCircles c = new CoveringCircles();
		c.generateRandomPoints(10, 30,30, 'r'); 
		
		if (c.points.size() == 0) { System.out.println("No points "); }
		if (! c.generateVoronoiSites() ) {System.out.println("Could not generate V sites"); } 
		c.setMaxDistance(); 
		
		
		System.out.printf("Max distance : %f ", c.maxCoverDistance );
		// System.exit(0);
		
		// creating graph. 
		UDG udg = new UDG(); 
		udg.createGraph(c.points, c.maxCoverDistance);
		udg.print();
		
		
		
		// is graph connected ? 
		if (udg.isConnected()) System.out.println("Graph is connected"); 
		else { System.out.println("Graph is not connected"); }
	
		
		// Now, generate a set of points.
		c.generateRandomPoints(20, 30, 30, 't'); 
		udg.createBipartiteGraph(c.points, c.tags, c.maxCoverDistance);
		udg.printBipartiteGraph();
		if (udg.isBipCovered()) System.out.println("Bibpartite graph is covering "); 
		else System.out.println("Bibpartite graph is not covering");
		
		// converting bipartite into lists; -- to make it ready for set-cover
		udg.convertBipartiteToList();
		udg.printBipartiteList();  
		
		ArrayList<Integer> tags = new ArrayList<Integer>(); 
		for (int i = 0; i < c.tags.size(); i++) tags.add(i); 
		
		
		// Computing the set cover
		ArrayList<ArrayList<Integer> > copyBipGraph = new ArrayList<ArrayList<Integer> >();
		copyBipGraph.addAll(udg.listBipGraph);
		ArrayList<Integer> sets = setCover(copyBipGraph, tags);
		
		
		
		System.out.println("Printing the set cover indices");
		for (int i = 0; i < sets.size(); i++) { 
			System.out.printf("%d ", sets.get(i));
		}
		System.out.println();
		
		
		// printing the cover sets elements: 
		System.out.println("Printing the set cover elements");
		for (int i = 0; i < sets.size(); i++) { 
			System.out.printf("%d: ", sets.get(i)); 
			for (int j = 0; j < udg.listBipGraph.get(sets.get(i)).size(); j++ ) { 
				System.out.printf("%d ", udg.listBipGraph.get(sets.get(i)).get(j) );
			}
			System.out.println();
		}
		
	}
	
	public static ArrayList<Integer> setCover(ArrayList<ArrayList<Integer>> listBipGraph, ArrayList<Integer> tags) {
		
		
		System.out.println("Set cover ");
		
		ArrayList<Integer> sets = new ArrayList<Integer>();
		ArrayList<Boolean> markedElements = new ArrayList<Boolean>();
		for (int i = 0; i < tags.size(); i++) markedElements.add(false); 
		
		
		int iteration = 1; 
		while (! allMarked(markedElements)) {
			
			System.out.printf("Starting iteration %d \n" , iteration);
			
			int maxIndex = findMaxSizeList(listBipGraph); 
			
			
			
			
			System.out.printf("Adding %d \n", maxIndex);
			sets.add(maxIndex);
			
	
			for (int i = 0; i < listBipGraph.get(maxIndex).size(); i++) { 
				markedElements.set(listBipGraph.get(maxIndex).get(i), true);
				System.out.printf("Marking element %d \n", listBipGraph.get(maxIndex).get(i));
			}
			
			// do the intersection.
			for (int i = 0; i< listBipGraph.size(); i++) {
				// removeAll(listBipGraph.get(maxIndex), listBipGraph.get(i));
				ArrayList<Integer> re = removeElements(listBipGraph.get(maxIndex), listBipGraph.get(i)); 
				listBipGraph.set(i, re);
			}
			
			// After deletion: 
			System.out.println("After deletion");
			for (int i = 0; i < listBipGraph.size(); i++) { 
				for (int j = 0; j < listBipGraph.get(i).size(); j++) {
					System.out.printf("%d ", listBipGraph.get(i).get(j));
				}
				System.out.println();
			}

			iteration ++;
		}
		
		// System.out.println("going out");
		
		return sets;
	}
	
	// TODO: (Fix) This is a buggy implementation 
	private static void removeAll(ArrayList<Integer> smallList,
			ArrayList<Integer> bigList) {

			System.out.println("removing "); 
			for (int i = 0; i < smallList.size(); i++) System.out.printf("%d ", smallList.get(i));
			System.out.println();
			
			System.out.println("from : "); 
			for (int i = 0; i < bigList.size(); i++) System.out.printf("%d ", bigList.get(i));
			System.out.println();
			
			// TODO: the error is here: This is not efficient 
			for (int i = 0; i < smallList.size(); i++ ) {
				bigList.remove(smallList.get(i));
			}
		
			
			System.out.println("result is");
			for (int i = 0; i < bigList.size(); i++) System.out.printf("%d ", bigList.get(i));
			System.out.println();
	}

	
	private static ArrayList<Integer> removeElements(ArrayList<Integer> smallList, ArrayList<Integer> bigList) { 
		Collections.sort(bigList);
		Collections.sort(smallList);
		
		System.out.println("removing ");
		for (int i = 0; i < smallList.size(); i++) System.out.printf("%d ", smallList.get(i));
		System.out.println();

		
		System.out.println("from : "); 
		for (int i = 0; i < bigList.size(); i++) System.out.printf("%d ", bigList.get(i));
		System.out.println();
		
		if (smallList.size() == 0) { return bigList; }
		
		ArrayList<Integer> result = new ArrayList<Integer>(); 
		int i = 0, j = 0;
		while ( i < bigList.size() && j < smallList.size())  { 
			
			System.out.printf("Comparing (s(%d)): %d to l(%d): %d \n", j, smallList.get(j), i, bigList.get(i));
			
			if (smallList.get(j) > bigList.get(i)) { result.add(bigList.get(i)); System.out.printf("Add %d \n", bigList.get(i));  i ++; }
			else if (smallList.get(j) == bigList.get(i)) { i++ ; j++; } 
			else { j++; }
			
			 
		}
		
		for (;i < bigList.size(); i++)
			result.add(bigList.get(i)); 
		
		// System.exit(0);
		return result; 
	}
	

	private static boolean allMarked(ArrayList<Boolean> markedElements) {

		for (int i = 0; i < markedElements.size(); i++ ) { 
			if (markedElements.get(i) == false) { System.out.printf("element %d is not marked yet \n", i); return false; }
		}
		
		return true;
	}


	private static int findMaxSizeList(
			ArrayList<ArrayList<Integer>> listBipGraph) {

		int max = 0, maxIndex = -1;
		for (int i = 0; i < listBipGraph.size(); i++) {
			if (listBipGraph.get(i).size() > max) { 
				max = listBipGraph.get(i).size();
				maxIndex = i; 
			}
		}
		
		if (maxIndex == -1) { 
			System.out.println("There must be an error in findMaxSizeList");
			System.exit(0);
		}
		
		
		return maxIndex;
	}


	public static Point2D.Double shrinkPoint(Point2D.Double c, Point2D.Double po,  double scale) { 
		Point2D.Double pn = new Point2D.Double(); 
		pn.x = c.x + (c.x - po.x) * scale; 
		pn.y = c.y + (c.y - po.y) * scale; 
		
		return pn; 
	}


}
