package structures;

import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import my.util.MyUtil;

public class Grid {

	private PrintStream a = System.out; 
	private boolean D = true;
	
	
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

		initThis();
	
	}

	
	private void initThis() {

		int slowI = 0, slowJ = 0;
		int id = 0; 
		for (int i = 0; i < gridWidth; i = i + regionWidth) {
			for (int j = 0; j < gridLength; j = j + regionLength) { 
				Region r = new Region((double) i, (double) j, regionWidth, regionLength); 
				r.setIndex(slowI, slowJ);
				r.setId(id);
				
				
				id ++;
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

	
	
	public int convert2Dto1D(IntegerPair p) {
		return p.x*(maxJ+1) + p.y;  
	}
	
	public void assignWeights(int max) {

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
		randomRegionIndex = new Random().nextInt(this.regions.size()) ;
		r = this.regions.get(randomRegionIndex);
		
		
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
			r = this.regions.get(randomFilledRegion);

			// debug
			a.printf("Picking %d as a random filled region which has index (%d,%d) \n", 
					randomFilledRegion,
					r.i, r.j);
			

			
			// Get a random neighbor of r -- let's its index be randomRegionIndex
			IntegerPair pairR = r.getRandomNeighbor();
			randomRegionIndex = this.convert2Dto1D(pairR);
			
			a.printf("A random neighbor of %d is (%d,%d) which is equal to %d \n", 
					randomFilledRegion,
					pairR.x, pairR.y, 
					randomRegionIndex);
			
			
			// Add random neighbor of r to filledIndices.
			this.regions.get(randomRegionIndex).increaseFilled();
			filledIndices.add(randomRegionIndex) ;
	
			a.printf("Adding %d to filled regions \n", randomRegionIndex);
			
			// move to next iteration.
			iteration ++; 
		}
		
	}


	public void createRectangleScript(String filename) {

		String s = "";
		int x1, y1, x2, y2; 
		Region r; 
		
		s += "unset object \n";
		for (int i = 0; i < this.regions.size(); i++ ) {
			r = this.regions.get(i); 
			if (r.isFilled()) {
			
			x1 = r.i * r.width; 
			y1 = r.j * r.length; 
			x2 = x1 + r.width; 
			y2 = y1 + r.length; 
			
			
			s += "set object rectangle from "; 
			s += x1 + " , " + y1 ; 
			s += " to "; 
			s += x2 + " , " + y2; 
			
			switch (r.filled) {
				case 1: 
					s += " fc rgb 'grey90' lw 0 \n";
					break;
				case 2: 
					s += " fc rgb 'grey70' lw 0 \n"; 
					break; 
				case 3: 
					s += " fc rgb 'grey50' lw 0 \n";
					break;  
				case 4:
					s += " fc rgb 'grey30' lw 0 \n"; 
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
		s += "set xrange [0: " + this.gridWidth + " ] \n";
		s += "set yrange [0: " + this.gridLength + " ] \n";
		s += "plot [0:6] x lw 0 t '' \n"; 
		s += "plot 'reg_points.dat' using 1:2 notitle \n";

		MyUtil.printFile(filename, s);
		

	}

	
	
	
	public ArrayList<Point2D> generateRegionalizedPoints(int n) {

		a.printf("Debugging ");
		for (int i = 0; i < regions.size(); i++) { 
			a.printf("%d \n", regions.get(i).filled);
		}
		
		// initialization:
		ArrayList<Double> weights, highR, lowR; 
		int indexRandom ; 
		ArrayList<Point2D> points; 
		Point2D rp;
		
		// find weights of each region. 
		weights = new ArrayList<Double>();
		findWeights(weights);
		
		 // Cumulative distribution:
		
		highR = new ArrayList<Double>();
		lowR = new ArrayList<Double>();
		findCumultativeDistribution(weights, lowR, highR);
	
			
		
		
		points = new ArrayList<Point2D>();
		int i = 0; 
		while (i < n) {
			indexRandom = pickRandomRegion(highR, lowR);
			
			a.printf("Random region is %d ", indexRandom);
			
			Region region = this.regions.get(indexRandom);
			
			a.printf(" with coord (%f,%f) \n", 
					region.getRegionCoord().getX(), 
					region.getRegionCoord().getY());
			
			rp = this.regions.get(indexRandom).generateRandomPoint();
			
			a.printf("Adding point (%f,%f) in region %d \n", 
					rp.getX(), rp.getY(), indexRandom);
			
			points.add(new Point2D.Double(rp.getX(), rp.getY()));
			i ++;
		}
		return points; 
	}

	
	
	
	private int pickRandomRegion(ArrayList<Double> highR, ArrayList<Double> lowR) {

		double n = new Random().nextDouble();
		for (int i = 0; i < highR.size(); i++) { 
			if (n >= lowR.get(i) && n < highR.get(i)) {
				return i;
			}
		}
	
		a.printf("Are lowR and highR empty? %d %d \n", 
				lowR.size(), highR.size());
		return -1; 
	}

	// Example: 
	// 1 + 2 + 2 + 1 ==> 1/6, 1/3, 1/3, 1/6 
	// 1/6, 1/3, 1/3, 1/6 ==> [0,1/6) , [1/6, 1/2) 
	// 				, [1/2, 5/6), [5/6, 1). 
	private void findCumultativeDistribution(ArrayList<Double> weights,
			ArrayList<Double> lowR, ArrayList<Double> highR) {

		
		double sum = 0; 
		for (int i = 0; i < weights.size(); i++) {
			lowR.add(sum); 
			sum += weights.get(i);
			highR.add(sum);
			
			a.printf("[%f, %f) \n", lowR.get(i), highR.get(i));
		}
		
		
	}

	private void findWeights(ArrayList<Double> weights) {
		// find sum of grid fillable. 
		int totalWeight = 0; 
		for (int i = 0; i < this.regions.size(); i++) { 
			totalWeight += this.regions.get(i).filled; 
			
			a.printf("tot:%d - c(i):%d \n", 
					totalWeight, 
					this.regions.get(i).filled);
		}

		
		
		
		// find weight for each region. 
		for (int i = 0; i < this.regions.size(); i++) { 
			weights.add(
					((double) this.regions.get(i).filled / (double) totalWeight));
		}
		

	}


	
	
}
