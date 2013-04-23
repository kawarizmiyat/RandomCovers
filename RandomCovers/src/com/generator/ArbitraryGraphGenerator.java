package com.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import my.util.MyUtil;

import algorithms.cover.SetCover;
import algorithms.graph.BipartiteUDG;
import algorithms.graph.Graph;

public class ArbitraryGraphGenerator {

	
	public static void generateRandomGraphs(int numFiles,
			int numReaders, 
			int numTags, 
			double p, 
			String foldername, 
			String filenamePrefix) {
		
		ArrayList<Graph> graphs = new ArrayList<Graph>();
		
		ArrayList<Integer> elements = new ArrayList<Integer>(); 
		for (int i = 0; i < numTags; i++) { 
			elements.add(i);
		}
		
		
		int i = 0;
		while (i < numFiles) { 
			Graph g = generateRandomBipGraph(numReaders, numTags, p);
			if (SetCover.isSetCover(g.listGraph, elements)) { 
				graphs.add(g); 
				i ++;
			}
		}
		
		
		print(graphs, foldername, filenamePrefix);
		
		
		
	}
	

	private static Graph generateRandomBipGraph(int numReaders, int numTags, double p) {
		Graph g = new Graph(); 
		Random r = new Random();
		for (int i = 0; i < numReaders; i++) { 
			
			ArrayList<Integer> ri = new ArrayList<Integer>();
			for (int j = 0; j < numTags; j++ ) { 
				if (r.nextDouble() <= p) { 
					ri.add(j); 
				}
			}

			g.listGraph.add(ri);
			
		}
		
		return g; 
	}

	
	public static void print(ArrayList<Graph> graphs, String foldername, 
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

