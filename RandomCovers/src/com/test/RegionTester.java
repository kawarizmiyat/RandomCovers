package com.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import structures.Grid;
import structures.Region;



public class RegionTester {

	PrintStream a = System.out; 
	Grid g; 
	
	public void run() { 
		
		// Divide the rectangle into regions. 
		// -- that is, create class Region. 
		// each region is a rectangle represented by 
		// 1. left button point, 2. width and 3. height .
		
		
		
		int width = 5; 
		int length = 5; 
		int maxW = 30; 
		int maxL = 30; 
		
		Grid g = new Grid(width, length, maxW, maxL);
		g.print();

		int maxFilled = 20; 
		assignWeights(g, maxFilled);
		
	}



	
	private void assignWeights(Grid g, int max) {

		// Initialization: pick a random region - make it filled. 
		// while (i < max) { 
		// pick a filled region, and pick a neighbor region of it. 
		// make this neighbor filled. 
		// i ++; 
		// endwhile. 
		
		int randomRegion = new Random().nextInt(g.regions.size()) ;
		
	}




	
}
