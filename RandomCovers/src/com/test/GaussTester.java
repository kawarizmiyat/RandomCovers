package com.test;

import algorithms.cover.CoveringCircles;

public class GaussTester {

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	public static void r() {
		run(); 
	}

	private static void run() {

		CoveringCircles c = new CoveringCircles();
		
		int n = 10, nt = 20; 
		double X = 30, Y = 30, u = 15, var = 15, vart = 6; 

		c.generateRandomNormalPoints(n, X, Y, u, var, 'r');
		c.generateRandomNormalPoints(nt, X, Y, u, vart, 't' );

		c.generateVoronoiSites(); 
		c.setMaxDistance(); 
		
		System.out.printf("Maximum Distance: %f \n", 
				c.getMaxDistance());
		
		
		
	}

}
