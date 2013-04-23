package com.test;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import algorithms.cover.SetCover;
import algorithms.graph.BipartiteUDG;
import my.util.MyUtil;

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
		// g.print();

		int maxFilled = 30; 
		g.assignWeights(maxFilled);
		
		a.printf("Filled regions: \n");
		for (int i = 0; i < g.regions.size(); i++) {
			Region r = g.regions.get(i);
			if (r.isFilled()) {
				a.printf("%d (%d, %d): %d \n", 
						r.id, r.i, r.j , r.filled);
			}
		}
		
		
		
		
		
		// TODO: 
		// create with different distributions:
		ArrayList<Point2D> tags = 
				g.generateRegionalizedPoints(500);
		String ts = pointsToString(tags); 
		MyUtil.printFile("reg_tags.dat", ts);

		
		ArrayList<Point2D> readers = 
				g.generateRegionalizedPoints(50);
		String rs = pointsToString(readers);
		MyUtil.printFile("reg_readers.dat", rs);
		

		// generate a gnuplot script file
		g.createRectangleScript("rect_script.p");
		
		
		// Creating bipartite graph ! 
		BipartiteUDG coveringBip = new BipartiteUDG();
		
		coveringBip.createAdjGraph(readers, 
				tags, 
				g.regionLength);
		
		coveringBip.adjToListGraph();
		coveringBip.printListGraph();		
		
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for (int i = 0; i < tags.size(); i++) elements.add(i); 
		
		boolean is = SetCover.isSetCover(coveringBip.listGraph, elements);
		if (!is) { 
			System.out.println("this is not a set cover");
		} else {
			System.out.println("this is a set cover ");
		}
		
		

		
	}

	
	
	private String pointsToString(ArrayList<Point2D> ps) {
		Point2D p; 
		String s = "";
		for (int i = 0; i < ps.size(); i++) { 
			p = ps.get(i);
			s += p.getX() + " " + p.getY() + " ";
			s += "\n";
		}
		
		return s; 
	}


	
	
}
