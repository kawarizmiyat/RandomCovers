package structures;

public class IntegerPair {

	public int x, y; 
	
	public IntegerPair(int x, int y) {
		this.x = x; 
		this.y = y;
	}

	public IntegerPair(IntegerPair p) {
		this.x = p.x; 
		this.y = p.y; 
	}
	
}
