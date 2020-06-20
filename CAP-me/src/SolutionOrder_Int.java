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
public class SolutionOrder_Int extends Solution {
	
	int[] order;
	int pos;
	
    public SolutionOrder_Int() {
    	Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	
    	order=Tools.randomPermutation(facNum);
    	pos = facNum / 2;
    	
   		eval();
    }
    
    public SolutionOrder_Int(int[] order,int pos) {
    	this.order = order;
    	this.pos=pos;
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
		while(i<pos) {
			twoList[0].add(order[i]);
			i++;
		}
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
		int pos=this.pos;
		
    	int i, j;
    	i = rand.nextInt(order.length);
    	j = rand.nextInt(order.length);
    	while ( j == i) {
    		j = rand.nextInt(order.length);
    	}
    	
    	//swap
    	if(rand.nextDouble()<0.5)
    	{
    		int temp=order[i];
    		order[i]=order[j];
    		order[j]=temp;
    	}
    	else
    	{
    		pos=i;
    	}

   	
//    	if (!isValid()) System.out.println("Invalid solution in neighbor of SolutionTwoList");
		return new SolutionOrder_Int(order,pos);
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

	public int getR() {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public Solution[] makeChild(Solution p1, Solution p2) {
		// TODO Auto-generated method stub
		int[][] pPer=new int[2][];
		int[] pRow=new int[2];
		if(p1 instanceof SolutionOrder_Int && p2 instanceof SolutionOrder_Int) {
			pPer[0]=((SolutionOrder_Int)p1).getP();
			pRow[0]=((SolutionOrder_Int)p1).getR();
			pPer[1]=((SolutionOrder_Int)p2).getP();
			pRow[1]=((SolutionOrder_Int)p2).getR();
		}
		
		//Crossover
		int[][] cPer=GeneticAlgorithmOperators.positionBasedCrossover(pPer[0],pPer[1]);
		int x=rand.nextInt(pPer[0].length);
		int y=rand.nextInt(pPer[0].length);
		int[] cRow=new int[]{x,y};
		
		
		
		//mutation
		for(int i=0;i<cPer.length;i++) {
			GeneticAlgorithmOperators.swap(cPer[i], 1);
		}
		return new Solution[] {new SolutionOrder_Int(cPer[0],cRow[0]),new SolutionOrder_Int(cPer[1],cRow[1])};
	}

}
