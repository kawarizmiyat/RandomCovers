package com.test;

import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import algorithms.cover.*;
import algorithms.graph.*;
import algorithms.voronoi.*;
import my.util.*; 

public class Tester {

	
	private static int numReaders = 10; 
	private static int numTags = 50; 
	private static int maxX = 30; 
	private static int maxY = 30; 
	private static boolean D = true;
	/**
	 * @param args
	 */
	
	public void run() {
	// public static void run() {	
		CoveringCircles coveringReaders = createCoveringReaders(
				numReaders, numTags, 
				maxX, maxY);

		BipartiteUDG coveringBip = new BipartiteUDG();
		
		coveringBip.createAdjGraph(coveringReaders.points, 
				coveringReaders.tags, 
				coveringReaders.maxCoverDistance);
		
		
		
		coveringBip.adjToListGraph();
		coveringBip.printListGraph();		
		
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for (int i = 0; i < coveringReaders.tags.size(); i++) elements.add(i); 
		
		boolean is = SetCover.isSetCover(coveringBip.listGraph, elements);
		if (!is) { 
			System.out.println("this is not a set cover ?!");
		}
		
		
		double cmr = coveringReaders.maxCoverDistance; 
		coveringReaders.generateVoronoiSites(); 
		coveringReaders.setMaxDistance(); 
		System.out.printf("old cd: %f - new cd: %f \n", cmr, coveringReaders.getMaxDistance());
		
		
		MyUtil.printFile("listGraph1.txt", coveringBip.strListGraph()); 
				
		String ps = circlesToString(coveringReaders.points, coveringReaders.maxCoverDistance);
		String ts = pointsToString(coveringReaders.tags);
		
		MyUtil.printFile("readers1.txt", ps);
		MyUtil.printFile("tags1.txt", ts);
		
		
		String script = generateGnuPlotScript("scatter.gif");
		MyUtil.printFile("script.p", script);

		try {
			Runtime.getRuntime().exec("gnuplot script.p");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private String generateGnuPlotScript(String output) {
		String s = ""; 
		
		s += "set terminal gif \n"; 
		s += "set output '" + output + "'\n" ; 
		s += "set yrange [0:" + maxY +"] \n";  
		s += "set xrange [0:" + maxX +"] \n";
		
		s += "plot 'readers1.txt' using 1:2 with points pt 7 notitle ,\\\n" ;  
		s += " 	'readers1.txt' using 1:2:3 with circles notitle ,\\\n ";
		s += " 	'tags1.txt' using 1:2 with points pt 5 notitle \n";
			
		return s; 
		
	}

	private String circlesToString(ArrayList<Point2D> p, double r) {
		String str = "";
		for (int i = 0; i < p.size(); i++) { 
			str += p.get(i).getX();
			str += "  "; 
			str += p.get(i).getY();
			str += "  ";
			str += r; 
			str += "\n";
		}
		return str;
	}

	private String pointsToString(ArrayList<Point2D> p) {
		String str = "";
		for (int i = 0; i < p.size(); i++) { 
			str += p.get(i).getX();
			str += "  "; 
			str += p.get(i).getY();
			str += "\n";
		}
		return str;
	}


	// Create a set of covering reader for the sets T, 
	// build it over the set of readers R 
	public CoveringCircles createCoveringReaders(int numReaders, 
			int numTags,
			int maxX, 
			int maxY) {
		
		

		
		// 1: generate random points for readers and tags. 
		// using CoveringCircles.
		CoveringCircles r = new CoveringCircles();

		r.generateRandomNormalPoints(numReaders, maxX, maxY,
				maxX/2, maxX/2, 'r'); 
		r.generateRandomNormalPoints(numTags, maxX, maxY, 
				maxX/2, maxX/8, 't');
		// r.generateRandomPoints(numReaders, maxX,maxY, 'r'); 
		// r.generateRandomPoints(numTags, maxX, maxY, 't');
	
		
		r.generateVoronoiSites(); 
		r.setMaxDistance(); 
		
		// 2: create a bipartite graph of readers and tags, 
		// and execute a set cover algorithm.
		BipartiteUDG bip = new BipartiteUDG();
		bip.createAdjGraph(r.points, r.tags, r.maxCoverDistance);
		bip.adjToListGraph();
		bip.printListGraph();
		
		
		ArrayList<Integer> elements = new ArrayList<Integer>(); 
		for (int i = 0; i < r.tags.size(); i++) elements.add(i); 
		
		ArrayList<Integer> coverSets = SetCover.execute(bip.listGraph, elements);
		
		
		// 3. find a backbone from the set cover coverSets. 
		ArrayList<Point2D> backbone = createBackbone(coverSets, r); 
		
		
		// 4. Initialize the output to backbone.
		ArrayList<Point2D> coveringReaders = new ArrayList<Point2D>();
		coveringReaders.addAll(backbone);
		
		// 5. start filling the coveringReaders until coveringReaders.size() 
		// is the required numReaders + every reader covers at least one tag.
		int iteration = 0; 
		while (coveringReaders.size() < numReaders) {
			
			
			// 5.a: create additional points + add them to coveringReaders.
			ArrayList<Point2D> additionalPoints = createAdditionalPoints(
					numReaders - coveringReaders.size(), 
					maxX, 
					maxY);
			
			coveringReaders.addAll(additionalPoints);

			
			// 5.b: create a bipartite graph to check whether the set of 
			// backbone + additional Points satisfuy our condition
			BipartiteUDG bip1 = new BipartiteUDG();
			bip1.createAdjGraph(coveringReaders, r.tags, r.maxCoverDistance);
			bip1.adjToListGraph();
			
			if (D) {
				System.out.println("The graph of the new bipartite graph");
				bip1.printListGraph();
			}
			
			// 5.c: find the covering Readers .. 
			ArrayList<Integer> coveredReaders = bip1.findCoveredReaders(); 
			if (D) { 
				System.out.printf("Size of coveredReaders is %d \n", coveredReaders.size());
			}
			
			
			// 5.d: remove all uncovered nodes from coveringReaders. 
			ArrayList<Point2D> copy = new ArrayList<Point2D>(); 
			for (int i = 0; i < coveredReaders.size(); i++ ) {
				copy.add(coveringReaders.get(coveredReaders.get(i)));
			}
			
			coveringReaders.clear();
			coveringReaders.addAll(copy);
			
			
			if (D) { System.out.printf("The size of coveringReaders is %d \n", coveringReaders.size());}
			iteration ++;
		}
		
		System.out.printf("Quitting createCoveringReaders afer %d iterations \n", iteration);
		
		// 6. Output the result 
		r.points.clear();
		r.points.addAll(coveringReaders);
		
		return r;
		
	}

	

	private ArrayList<Point2D> createAdditionalPoints(int k, int X,
			int Y) {

		ArrayList<Point2D> additionalPoints = new ArrayList<Point2D>();
		CoveringCircles c1 = new CoveringCircles();
		c1.generateRandomPoints(k, X ,Y, 'r'); 
		additionalPoints.addAll(c1.points);
		
		return additionalPoints; 
		
	}






	private ArrayList<Point2D> createBackbone(
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
