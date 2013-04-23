package com.generator;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import my.util.MyUtil;

import algorithms.cover.CoveringCircles;
import algorithms.cover.SetCover;
import algorithms.graph.BipartiteUDG;



// TODO: create NormalGenerator.  --- the only change is in (createCoveringReaders)
public class UniformGenerator {

	private static boolean D = false;
	
	public static void generateRandomUniformCovers(int n, int numReaders, 
			int numTags, int maxX, int maxY, 
			String foldername, String filenamePrefix) { 
		
		ArrayList<BipartiteUDG> graphs = new ArrayList<BipartiteUDG>();
		
		int i = 0; 
		while (i < n)  { 
			
			CoveringCircles coveringReaders = createCoveringReaders(
					numReaders, numTags, 
					maxX, maxY);

			BipartiteUDG coveringBip = new BipartiteUDG();
			
			coveringBip.createAdjGraph(coveringReaders.points, 
					coveringReaders.tags, 
					coveringReaders.maxCoverDistance);
			
			
			graphs.add(coveringBip);
			
			i ++;
		}
		
		// print at a given file folder. 
		print(graphs, foldername, filenamePrefix);
		
	}
	
	public static void print(ArrayList<BipartiteUDG> graphs, String foldername, 
			String filenamePrefix) { 
		
		// TODO: -- create folder if it does not exist. 
		for (int i = 0; i < graphs.size(); i++) { 
			String content = graphs.get(i).strListGraph();
			String filename = foldername + "/" + filenamePrefix + "_" + i + ".dat";
			MyUtil.printFile(filename, content);
		}
		
	}

	
	// Create a set of covering reader for the sets T, 
	// build it over the set of readers R 
	public static CoveringCircles createCoveringReaders(int numReaders, 
			int numTags,
			int maxX, 
			int maxY) {
		
		

		
		// 1: generate random points for readers and tags. 
		// using CoveringCircles.
		CoveringCircles r = new CoveringCircles();

		// r.generateRandomNormalPoints(numReaders, maxX, maxY,
		// 		maxX/2, maxX/2, 'r'); 
		// r.generateRandomNormalPoints(numTags, maxX, maxY, 
		// 		maxX/2, maxX/8, 't');
		r.generateRandomPoints(numReaders, maxX,maxY, 'r'); 
		r.generateRandomPoints(numTags, maxX, maxY, 't');
	
		
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
			// backbone + additional Points satisfy our condition
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

	
	private static ArrayList<Point2D> createBackbone(
			ArrayList<Integer> coverSets, CoveringCircles c) {

		ArrayList<Point2D> backbone = new ArrayList<Point2D>(); 
		for (int i = 0; i < coverSets.size(); i++) { 
			backbone.add(c.points.get(coverSets.get(i)));
		}
		
		return backbone;
	}

	

	private static ArrayList<Point2D> createAdditionalPoints(int k, int X,
			int Y) {

		ArrayList<Point2D> additionalPoints = new ArrayList<Point2D>();
		CoveringCircles c1 = new CoveringCircles();
		c1.generateRandomPoints(k, X ,Y, 'r'); 
		additionalPoints.addAll(c1.points);
		
		return additionalPoints; 
		
	}
	
}
