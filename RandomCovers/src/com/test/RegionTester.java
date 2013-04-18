package com.test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import my.util.MyUtil;

import structures.Grid;
import structures.IntegerPair;
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
		// g.print();

		int maxFilled = 20; 
		assignWeights(g, maxFilled);
		
		a.printf("Filled regions: \n");
		for (int i = 0; i < g.regions.size(); i++) {
			Region r = g.regions.get(i);
			if (r.isFilled()) {
				a.printf("%d (%d, %d): %d \n", 
						r.id, r.i, r.j , r.filled);
			}
		}
		
		
		createRectangleScript("rect_script.p", g);
		
		
	}



	
	public void createRectangleScript(String filename, Grid g) {
		String s = "";
		int x1, y1, x2, y2; 
		Region r; 
		
		s += "unset object \n";
		for (int i = 0; i < g.regions.size(); i++ ) {
			r = g.regions.get(i); 
			if (r.isFilled()) {
			
			x1 = r.i; 
			y1 = r.j; 
			x2 = x1 + 1; 
			y2 = y1 + 1; 
			
			
			s += "set object rectangle from "; 
			s += x1 + " , " + y1 ; 
			s += " to "; 
			s += x2 + " , " + y2; 
			
			switch (r.filled) {
				case 1: 
					s += " fc rgb 'grey90' \n";
					break;
				case 2: 
					s += " fc rgb 'grey70' \n"; 
					break; 
				case 3: 
					s += " fc rgb 'grey50' \n";
					break; 
				case 4:
					s += " fc rgb 'grey30' \n"; 
					break; 
				default: 
					a.printf("r.filled %d value is not accepted \n",
						r.filled);
					System.exit(0);
					break;
			}
			}
		}
		
		s += "\n"; 
		s += "plot [0:6] x lw 0 t '' ";  

		MyUtil.printFile(filename, s);
		
	}




	private void assignWeights(Grid g, int max) {

		// Initialization: pick a random region - make it filled. 
		// while (i < max) { 
		// pick a filled region, and pick a neighbor region of it. 
		// make this neighbor filled. 
		// i ++; 
		// endwhile. 
		
		int randomRegionIndex, randomFilledRegion;
		ArrayList<Integer> filledIndices = new ArrayList<Integer>();
		Region r;
		int iteration = 1; 

		
		// pick a random region in the grid. -- called r
		randomRegionIndex = new Random().nextInt(g.regions.size()) ;
		r = g.regions.get(randomRegionIndex);
		
		
		// Fille region r. 
		r.increaseFilled(); 
		filledIndices.add(randomRegionIndex);
		a.printf("Adding %d to filled regions which has index (%d,%d) \n", 
				randomRegionIndex,
				r.i, r.j);

		
		// iteration until max regions are filled.
		iteration = 1; 
		while (iteration < max) {
			
			// Pick a random filled region. -- call it r. 
			randomFilledRegion = filledIndices.get(new Random().nextInt(filledIndices.size())); 
			r = g.regions.get(randomFilledRegion);

			// debug
			a.printf("Picking %d as a random filled region which has index (%d,%d) \n", 
					randomFilledRegion,
					r.i, r.j);
			

			
			// Get a random neighbor of r -- let's its index be randomRegionIndex
			IntegerPair pairR = r.getRandomNeighbor();
			randomRegionIndex = g.convert2Dto1D(pairR);
			
			a.printf("A random neighbor of %d is (%d,%d) which is equal to %d \n", 
					randomFilledRegion,
					pairR.x, pairR.y, 
					randomRegionIndex);
			
			
			// Add random neighbor of r to filledIndices.
			g.regions.get(randomRegionIndex).increaseFilled();
			filledIndices.add(randomRegionIndex) ;
	
			a.printf("Adding %d to filled regions \n", randomRegionIndex);
			
			// move to next iteration.
			iteration ++; 
		}
		
	}




	
}
