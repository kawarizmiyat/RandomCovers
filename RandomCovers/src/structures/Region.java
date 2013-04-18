package structures;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;



public class Region {

	
	private static final int MAX_FILLED = 4;
	public int width, length; 
	public Point2D point; 
	public int i, j; 
	public IntegerPair leftNeighbor, rightNeighbor, upNeighbor, downNeighbor; 
	public ArrayList<IntegerPair> neighbors;
	public int filled;  
	public int id;
	
	
	public Region(double x, double y, int w, int l) { 
		this.point = new Point2D.Double(x,y); 
		this.width  = w; 
		this.length = l;
		
		leftNeighbor = null; 
		rightNeighbor = null; 
		upNeighbor = null; 
		downNeighbor = null; 
		
		neighbors = new ArrayList<IntegerPair>();
		filled = 0; 
	}
	
	public boolean isFilled() { 
		return (filled > 0);
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setIndex(int x, int y) { 
		this.i = x; 
		this.j = y;
	}
	
	public void print() { 
		System.out.printf("[%f, %f] (%d,%d) ", 
				this.point.getX(),
				this.point.getY(), 
				this.width, 
				this.length);
	}
	
	
	public IntegerPair getLeftNeighbor() {
		return new IntegerPair(i , j - 1); 
	}
	

	public IntegerPair getRightNeighbor() {
		return new IntegerPair(i , j + 1); 
	}
	

	public IntegerPair getUpNeighbor() {
		return new IntegerPair(i - 1, j); 
	}
	

	public IntegerPair getDownNeighbor() {
		return new IntegerPair(i + 1, j); 
	}

	public void setLeftNeighbor(IntegerPair leftNeighbor) {
		this.leftNeighbor = new IntegerPair(leftNeighbor.x, leftNeighbor.y);
		neighbors.add(new IntegerPair(leftNeighbor));
	}

	public void setRightNeighbor(IntegerPair rightNeighbor) {
		this.rightNeighbor = new IntegerPair(rightNeighbor.x, rightNeighbor.y); 
		neighbors.add(new IntegerPair(rightNeighbor));
	}

	public void setUpNeighbor(IntegerPair upNeighbor) {
		this.upNeighbor = new IntegerPair(upNeighbor.x, upNeighbor.y);
		neighbors.add(new IntegerPair(upNeighbor));
	}

	public void setDownNeighbor(IntegerPair downNeighbor) {
		this.downNeighbor = new IntegerPair(downNeighbor.x, downNeighbor.y);
		neighbors.add(new IntegerPair(downNeighbor));
	}

	public IntegerPair getRandomNeighbor() {
		int ri = new Random().nextInt(neighbors.size());
		return neighbors.get(ri);
		
	}

	public void increaseFilled() {
		if (filled < MAX_FILLED) {
			filled ++; 
		}
	}

	
	
	
}
