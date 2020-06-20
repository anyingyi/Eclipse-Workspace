import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public class SolutionTwoList extends Solution {
	
    public SolutionTwoList() {
    	Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	int[] p = Tools.randomPermutation(facNum);
    	
    	for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        }
    	int pos = facNum / 2;
    	for (int i = 0; i < pos; i++) {
    		twoList[0].add(p[i]);
    	}
    	for (int i = pos; i < p.length; i++) {
    		twoList[1].add(p[i]);
    	}
   		eval();
    }
    
    public SolutionTwoList(List<Integer>[] twoList) {
    	this.twoList = twoList;
    	eval();
    }
    

	/*  
	 * This method don't need to do anything for this class
	 */
	@Override
	public void decoding() {}

	/* (non-Javadoc)
	 * @see Solution#neighbor()
	 */
	@Override
	public Solution neighbor() {
		List<Integer>[] twoList = new List[this.twoList.length];
    	for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        	for (int fac : this.twoList[i]) {
        		twoList[i].add(fac);
        	}
        	//twoList[i].addAll(this.twoList[i]);
        }
    	
    	int i, j;
    	i = rand.nextInt(twoList[0].size() + twoList[1].size());
    	j = rand.nextInt(twoList[0].size() + twoList[1].size());
    	while ( j == i) {
    		j = rand.nextInt(twoList[0].size() + twoList[1].size());
    	}
		if (i > j) {
			int temp = i; i = j; j = temp;
		}
		
		if (twoList[0].size() < 1) {
			//j -= twoList[0].size();
			twoList[0].add(twoList[1].remove(j));
		} else if (twoList[1].size() < 1) {
			twoList[1].add(twoList[0].remove(i));
		} else	if ( i < twoList[0].size() && j < twoList[0].size()) {
    		Collections.swap(twoList[0], i, j);
    	} else if ( i >= twoList[0].size() && j >= twoList[0].size()) {
    		i -= twoList[0].size();
    		j -= twoList[0].size();
    		Collections.swap(twoList[1], i, j);
    	} else {
   			j -= twoList[0].size();
   			if (rand.nextDouble() < 0.5) {
   				if (rand.nextDouble() < 0.5) {//Move from list[0] to list[1]
   					int fac = twoList[0].remove(i);
   					twoList[1].add(j, fac);
   				} else {
   					int fac = twoList[1].remove(j);
   					twoList[0].add(i, fac);
   				}
   			} else {//swap
                int fac1 = twoList[0].remove(i);
                int fac2 = twoList[1].remove(j);
                twoList[0].add(i, fac2);
                twoList[1].add(j, fac1);
   			}
    	}
//    	if (!isValid()) System.out.println("Invalid solution in neighbor of SolutionTwoList");
		return new SolutionTwoList(twoList);
	}

	/* (non-Javadoc)
	 * @see Solution#localSearch()
	 */
	@Override
	public Solution localSearch() {
		// TODO Auto-generated method stub
		return this;
	}

}
