package algorithms.cover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SetCover {

	private static boolean D = false;
	
	public static ArrayList<Integer> execute(
			ArrayList<ArrayList<Integer>> sets, ArrayList<Integer> elements) {



		if (D) { System.out.println("at Set cover"); }
		
		ArrayList<ArrayList<Integer> > copySets = new ArrayList<ArrayList<Integer> >();
		copySets.addAll(sets);
		
		ArrayList<Integer> coverSets = new ArrayList<Integer>();
		ArrayList<Boolean> markedElements = new ArrayList<Boolean>();
		for (int i = 0; i < elements.size(); i++) markedElements.add(false); 
		
		
		int iteration = 1; 
		while (! allMarked(markedElements)) {
			
			if (D) { System.out.printf("Starting iteration %d \n" , iteration); }
			
			int maxIndex = findMaxSizeList(copySets); 
			
			
			if (D) { System.out.printf("Adding %d \n", maxIndex); }
			coverSets.add(maxIndex);
			
	
			for (int i = 0; i < copySets.get(maxIndex).size(); i++) { 
				markedElements.set(copySets.get(maxIndex).get(i), true);
				System.out.printf("Marking element %d \n", copySets.get(maxIndex).get(i));
			}
			
			// do the intersection.
			for (int i = 0; i< copySets.size(); i++) {
				
				ArrayList<Integer> re = removeElements(copySets.get(maxIndex), copySets.get(i)); 
			
				// TODO: Do we really need this ?
				copySets.set(i, re);
			}
			
			if (D) {
				// After deletion: 
				System.out.println("After deletion");
				for (int i = 0; i < copySets.size(); i++) { 
					for (int j = 0; j < copySets.get(i).size(); j++) {
						System.out.printf("%d ", copySets.get(i).get(j));
					}
					System.out.println();
				}
			}

			iteration ++;
		}
		
		
		return coverSets;
	
	}

	
	
	private static ArrayList<Integer> removeElements(ArrayList<Integer> smallList, ArrayList<Integer> bigList) { 

		// TODO: In order to improve execution of this function and the 
		// algorithm overall, make sure that these two sets are sorted 
		// before entering this method
		Collections.sort(bigList);
		Collections.sort(smallList);
		
		if (D) { 
			System.out.println("removing ");
			for (int i = 0; i < smallList.size(); i++) 
				System.out.printf("%d ", smallList.get(i));
			System.out.println();
		
		
			System.out.println("from : "); 
			for (int i = 0; i < bigList.size(); i++) 
				System.out.printf("%d ", bigList.get(i));
			System.out.println();
		}
		
		
		// There is nothing to remove.
		if (smallList.size() == 0) { return bigList; }
		
		
		// Else:
		ArrayList<Integer> result = new ArrayList<Integer>(); 
		int i = 0, j = 0;
		while ( i < bigList.size() && j < smallList.size())  { 
			
			if (D) {
			System.out.printf("Comparing (s(%d)): %d to l(%d): %d \n", 
					j, smallList.get(j), 
					i, bigList.get(i));
			
			}
			
			if (smallList.get(j) > bigList.get(i)) { 
				result.add(bigList.get(i)); 
				if (D) { System.out.printf("Add %d \n", bigList.get(i)); }  
				i ++; 

			} else if (smallList.get(j) == bigList.get(i)) { 
				i++; 
				j++; 
			} else { 
				j++; 
			}
			 
		}

		// Special case: do not remove these elements from bigList, as we 
		// know for sure that they will not be removed.
		// Hint: note that the upper loop terminates when we reach the limit of 
		// the smallList. Also, note that an element is not removed only if 
		// smallList(j) > bigList(i). 
		
		for (;i < bigList.size(); i++)
			result.add(bigList.get(i)); 
		
		return result; 
	}
	

	// TODO: this could be fixed for performance issues. 
	// Example: use a counter. 
	// Note that this assumes that elements are from a range of 1:n.
	private static boolean allMarked(ArrayList<Boolean> markedElements) {

		for (int i = 0; i < markedElements.size(); i++ ) { 
			if (markedElements.get(i) == false) { 
				if (D) { 
					System.out.printf("element %d is not marked yet \n", i); 
				}
				return false; 
			}
		}
		
		return true;
	}

	// This finds the maximum sized list in S
	private static int findMaxSizeList(
			ArrayList<ArrayList<Integer>> S) {

		int max = 0, maxIndex = -1;
		for (int i = 0; i < S.size(); i++) {
			if (S.get(i).size() > max) { 
				max = S.get(i).size();
				maxIndex = i; 
			}
		}
		
		if (maxIndex == -1) { 
			System.err.println("There must be an error in findMaxSizeList");
			System.exit(0);
		}
		
		
		return maxIndex;
	}

	
	public static boolean isSetCover(
			ArrayList<ArrayList<Integer> > setCover, 
			ArrayList<Integer> elements) {
	

		HashMap<Integer, Boolean> hmap = new HashMap<Integer, Boolean>();
		for (int i = 0; i < elements.size(); i ++) { 
			hmap.put(elements.get(i), false);
		}
		
		// mark elements as marked
		for (int i = 0; i < setCover.size(); i ++) { 
			for (int j = 0; j < setCover.get(i).size(); j++) {
				if (hmap.get(setCover.get(i).get(j)) != null) {
					hmap.put(setCover.get(i).get(j), true);
				} else {

					System.err.printf("Elemenet %d found in set %d " +
							"is not found in hmap (i.e. elements)", 
							setCover.get(i).get(j), 
							i);
					
					System.exit(0);
				}
			}
		}
		
		ArrayList<Boolean> hv = (ArrayList<Boolean>) hmap.values();
		for (int i = 0; i < hv.size(); i++) { 
			if (hv.get(i) == false) return false;
		}
				
		return true;
		
	}

	
}
