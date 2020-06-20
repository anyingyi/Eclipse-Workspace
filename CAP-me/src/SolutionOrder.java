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
public class SolutionOrder extends Solution {
	
	int[] order;
	
    public SolutionOrder() {
    	Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	List<Integer> list = new ArrayList<>();
    	for (int i = 0; i < facNum+1; i++) {
    		list.add(i);
    	}
    	Collections.shuffle(list);
    	order=new int[list.size()];
    	for(int i=0;i<list.size();i++) {
    		order[i]=list.get(i);
    	}
   		eval();
    }
    
    public SolutionOrder(int[] order) {
    	this.order = order;
    	eval();
    }
    

	/*  
	 * This method don't need to do anything for this class
	 */
	@Override
	public void decoding() {
		for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        }

    	int i=0;
		while(order[i]!=order.length-1&&i<order.length) {
			twoList[0].add(order[i]);
			i++;
		}
		i++;
		while(i<order.length) {
			twoList[1].add(order[i]);
			i++;
		}
    	
	}

	/* (non-Javadoc)
	 * @see Solution#neighbor()
	 */
	@Override
	public Solution neighbor() {
    	int[] order=this.order.clone();
    	
    	int i, j;
    	i = rand.nextInt(order.length);
    	j = rand.nextInt(order.length);
    	while ( j == i) {
    		j = rand.nextInt(order.length);
    	}
    	//swap
 
		int temp=order[i];
		order[i]=order[j];
		order[j]=temp;
   	
//    	if (!isValid()) System.out.println("Invalid solution in neighbor of SolutionTwoList");
		return new SolutionOrder(order);
	}

	/* (non-Javadoc)
	 * @see Solution#localSearch()
	 */
	@Override
	public Solution localSearch() {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getP() {
		// TODO Auto-generated method stub
		return order;
	}

	@Override
	public Solution[] makeChild(Solution p1,Solution p2) {
		// TODO Auto-generated method stub
		int[][] pPer=new int[2][];
		if(p1 instanceof SolutionOrder && p2 instanceof SolutionOrder) {
			pPer[0]=((SolutionOrder)p1).getP();
			pPer[1]=((SolutionOrder)p2).getP();
		}
		
		//Crossover
		int[][] cPer=GeneticAlgorithmOperators.linearOrderCrossover(pPer[0],pPer[1]);
		
		//mutation
		for(int i=0;i<cPer.length;i++) {
			GeneticAlgorithmOperators.swap(cPer[i], 1);
		}
		return new Solution[] {new SolutionOrder(cPer[0]),new SolutionOrder(cPer[1])};
	}


}
