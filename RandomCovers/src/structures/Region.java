package structures;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Region {

	
	public int width, length; 
	public Point2D point; 
	public int i, j; 
	public IntegerPair leftNeighbor, rightNeighbor, upNeighbor, downNeighbor; 
	public ArrayList<IntegerPair> neighbors; 
	
	
	public Region(double x, double y, int w, int l) { 
		this.point = new Point2D.Double(x,y); 
		this.width  = w; 
		this.length = l;
		
		leftNeighbor = null; 
		rightNeighbor = null; 
		upNeighbor = null; 
		downNeighbor = null; 
		
		ArrayList<IntegerPair> neighbor = new ArrayList<IntegerPair>();
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
		this.leftNeighbor.x = leftNeighbor.x;
		this.leftNeighbor.y = leftNeighbor.y;
		neighbors.add(new IntegerPair(leftNeighbor));
	}

	public void setRightNeighbor(IntegerPair rightNeighbor) {
		this.rightNeighbor.x = rightNeighbor.x;
		this.rightNeighbor.y = rightNeighbor.y;
		neighbors.add(new IntegerPair(rightNeighbor));
	}

	public void setUpNeighbor(IntegerPair upNeighbor) {
		this.upNeighbor.x = upNeighbor.x;
		this.upNeighbor.y = upNeighbor.y;
		neighbors.add(new IntegerPair(upNeighbor));
	}

	public void setDownNeighbor(IntegerPair downNeighbor) {
		this.downNeighbor.x = downNeighbor.x;
		this.downNeighbor.y = downNeighbor.y;
		neighbors.add(new IntegerPair(downNeighbor));
	}

	
	
	
}
