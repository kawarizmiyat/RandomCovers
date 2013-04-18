package com.test;

import java.awt.geom.Point2D;
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
		ArrayList<Point2D> points = 
				g.generateRegionalizedPoints(100);
		String ps = pointsToString(points); 
		MyUtil.printFile("reg_points.dat", ps);

		
		g.createRectangleScript("rect_script.p");

		
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
