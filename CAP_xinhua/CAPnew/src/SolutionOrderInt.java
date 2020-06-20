import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionOrderInt extends Solution{
	int[] p; 
	int flag;
	public int[] getP() { return p; }
	public int getFlag() { return flag; }
	
    public SolutionOrderInt() {
    	Problem problem = Problem.get();
    	int facNum = problem.getFacNum();
    	p = Tools.randomPermutation(facNum);
    	flag = facNum / 2;
   		eval();
    }
    
    public SolutionOrderInt(int[] p,int flag) {
    	this.p = p;
    	this.flag = flag;
    	eval();
    }
    
	@Override
	public void decoding() {
		for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        }
    	for (int i = 0; i < p.length; i++) {
    		if(i < flag)
    			twoList[0].add(p[i]);
    		else
    			twoList[1].add(p[i]);   		      
    	 }
	}

	@Override
	public Solution neighbor() {
		int[] p = this.p.clone();
		int i = rand.nextInt(p.length);

		int j =  rand.nextInt(p.length);
		while (j == i) {
			j =  rand.nextInt(p.length);
		}   		
		p[i] = this.p[j];
		p[j] = this.p[i];
        return new SolutionOrder(p);
	}

	@Override
	public Solution localSearch() {
		// TODO Auto-generated method stub
		return this;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        
	}
}
