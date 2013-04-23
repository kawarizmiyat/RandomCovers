package com.generator;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;

import my.util.MyUtil;

import algorithms.cover.SetCover;
import algorithms.graph.BipartiteUDG;

import structures.Grid;

public class RegionalizedGraphGenerator {

	
	
	public static void generateRandomGraphs(int n, int rw, int rl, int gw, int gl, 
			int maxFilled, int numTags, int numReaders, 
			String foldername, String filenamePrefix) { 
		
		ArrayList<BipartiteUDG> graphs = new ArrayList<BipartiteUDG>();
		
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for (int i = 0; i < numTags; i++) elements.add(i); 
		
		int i = 0; 
		while (i < n) { 

			Grid g = new Grid(rw, rl, gw, gl);
			g.assignWeights(maxFilled);
			ArrayList<Point2D> tags = 
					g.generateRegionalizedPoints(numTags);
			ArrayList<Point2D> readers = 
					g.generateRegionalizedPoints(numReaders);
			
			
			// Creating bipartite graph ! 
			BipartiteUDG coveringBip = new BipartiteUDG();
			
			coveringBip.createAdjGraph(readers, 
					tags, 
					g.regionLength);
			
			coveringBip.adjToListGraph();
		
			
			if (SetCover.isSetCover(coveringBip.listGraph, elements) ) { 		
				graphs.add(coveringBip);
				i ++;
			}
		}
		
		// print at a given file folder. 
		print(graphs, foldername, filenamePrefix);
		
		
	}
	
	public static void print(ArrayList<BipartiteUDG> graphs, String foldername, 
			String filenamePrefix) { 
		
		
		try {
			Runtime.getRuntime().exec("mkdir " + foldername);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		for (int i = 0; i < graphs.size(); i++) { 
			String content = graphs.get(i).strListGraph();
			String filename = foldername + "/" + filenamePrefix + "_" + i + ".dat";
			MyUtil.printFile(filename, content);
		}
		
	}

}
