package structures;

import java.io.PrintStream;
import java.util.ArrayList;

public class Grid {

	PrintStream a = System.out; 
	public ArrayList<Region> regions; 
	public int gridWidth, gridLength; 
	public int maxI, maxJ; 
	public int regionWidth, regionLength; 
	
	public Grid(int w, int l, int maxW, int maxL) { 

		regionWidth = w; 
		regionLength = l; 
		gridWidth = maxW; 
		gridLength =  maxL;
		
		maxI = gridWidth / regionWidth - 1; 
		maxJ = gridLength / regionWidth - 1;
		
		
		regions = new ArrayList<Region>();
		int slowI = 0, slowJ = 0; 
		for (int i = 0; i < gridWidth; i = i + regionWidth) {
			for (int j = 0; j < gridLength; j = j + regionLength) { 
				Region r = new Region((double) i, (double) j, regionWidth, regionLength); 
				r.setIndex(slowI, slowJ);
				regions.add(r);
				slowJ ++;
			}
			slowI ++; 
			slowJ = 0;
		}

		assignNeighbors();
	
	}

	
	public void print() { 

		a.println("Printing regions ");
		for (int i = 0; i < regions.size(); i ++) { 
			regions.get(i).print();
			a.println(); 
		}
	}
	
	
	public void assignNeighbors() {

		for (int i = 0; i < regions.size(); i++) { 
		
			Region tempRegion = regions.get(i); 
			IntegerPair tempNeighbor; 
			// left neighbor: 
			
			tempNeighbor = tempRegion.getLeftNeighbor(); 
			if (inRange(tempNeighbor)) {
				tempRegion.setLeftNeighbor( tempNeighbor ) ; 
			}
			
			tempNeighbor = tempRegion.getRightNeighbor(); 
			if (inRange(tempNeighbor)) {
				tempRegion.setRightNeighbor( tempNeighbor ) ; 
			}

			tempNeighbor = tempRegion.getUpNeighbor(); 
			if (inRange(tempNeighbor)) {
				tempRegion.setUpNeighbor( tempNeighbor ) ; 
			}

			tempNeighbor = tempRegion.getDownNeighbor(); 
			if (inRange(tempNeighbor)) {
				tempRegion.setDownNeighbor( tempNeighbor ) ; 
			}

			
 			
		}
		
	}
	
	public boolean inRange(IntegerPair p) { 
		return (p.x >= 0 & p.x <= maxI & p.y >= 0 & p.y <= maxJ);
	}

	
	private int covert2Dto1D(int i, int j, int maxJ) {
		return i*maxJ + j;  
	}
	
}
