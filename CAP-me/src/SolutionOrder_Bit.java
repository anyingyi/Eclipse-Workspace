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
public class SolutionOrder_Bit extends Solution {
	
	private int[] order;
	private int[] index;
	
	
    public SolutionOrder_Bit() {
    	Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	order=Tools.randomPermutation(facNum);
    	index=new int[order.length];
    	
    	for (int i = 0; i < index.length; i++) {
    		if(rand.nextDouble()<0.5)
    		{
    			index[i]=1;
    		}
    		else
    			index[i]=0;
    	}
   		eval();
    }
    
    public SolutionOrder_Bit(int[] order,int[] index) {
    	this.order = order;
    	this.index=index;
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
		while(i<order.length) {
			if(index[i]==1)
				twoList[1].add(order[i]);
			else
				twoList[0].add(order[i]);
			i++;
		}
    	
	}

	/* (non-Javadoc)
	 * @see Solution#neighbor()
	 */
	@Override
	public Solution neighbor() {
    	int[] order=this.order.clone();
    	int[] index=this.index.clone();
    	int i;
    	i = rand.nextInt(index.length);
 
    	if(rand.nextDouble()<0.5)
    	{
    		index[i]=1-index[i];
    	}
    	else {		//swap
	    	GeneticAlgorithmOperators.swap(order, 1);
    	}
//    	if (!isValid()) System.out.println("Invalid solution in neighbor of SolutionTwoList");
		return new SolutionOrder_Bit(order,index);
	}
	
	int[] getP(){return order;}
	int[] getRow() {return index;}

	/* (non-Javadoc)
	 * @see Solution#localSearch()
	 */
	@Override
	public Solution localSearch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Solution[] makeChild(Solution p1, Solution p2) {
		// TODO Auto-generated method stub
		int[][] pPer=new int[2][];
		int[][] pRow=new int[2][];
		if(p1 instanceof SolutionOrder_Bit && p2 instanceof SolutionOrder_Bit) {
			pPer[0]=((SolutionOrder_Bit)p1).getP();
			pRow[0]=((SolutionOrder_Bit)p1).getRow();
			pPer[1]=((SolutionOrder_Bit)p2).getP();
			pRow[1]=((SolutionOrder_Bit)p2).getRow();
		}
		
		//Crossover
		int[][] cPer=GeneticAlgorithmOperators.positionBasedCrossover(pPer[0],pPer[1]);
		int[][] cRow=GeneticAlgorithmOperators.twoPorintCrossover(pRow[0],pRow[1]);
		
		//mutation
		for(int i=0;i<cPer.length;i++) {
			GeneticAlgorithmOperators.swap(cPer[i], 1);
			int j=rand.nextInt(cRow[i].length);
			cRow[i][j]=1-cRow[i][j];
		}
		return new Solution[] {new SolutionOrder_Bit(cPer[0],cRow[0]),new SolutionOrder_Bit(cPer[1],cRow[1])};
	}

}
