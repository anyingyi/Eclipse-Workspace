import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionOrder extends Solution {
	int[] p; //p[i] == p.length - 1 means flag
	public int[] getP() { return p; }
	
    public SolutionOrder() {
    	Problem problem = Problem.get();
    	p = Tools.randomPermutation(problem.getFacNum()+1);
   		eval();
    }
    
    public SolutionOrder(int[] p) {
    	this.p = p;
    	eval();
    }
    
	@Override
	public void decoding() {
		Problem problem = Problem.get();
        for (int i = 0; i < twoList.length; i++) {
        	twoList[i] = new ArrayList<>();
        }
        int i = 0;
        while (p[i] != problem.getFacNum()) {
        	twoList[0].add(p[i]);
        	i++;
        }
        i++;//Skip the flag
        while (i < p.length) {
        	twoList[1].add(p[i]);
        	i++;
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
		int flag = 0;
		for(int k = 0; k < p.length; i++) 
			if(p[k] == Problem.get().getFacNum())
				flag = k;
			 if((i<flag&&j<flag)||(i>flag&&j>flag)){
				 p[i] = this.p[j];
				 p[j] = this.p[i];
		    }else {
			   if(i<j) {//Move from list[0] to list[1]
  					p[i] = this.p[flag];
  					p[flag] = this.p[i]; 					
  				} else {
  					p[j] = this.p[flag];
  					p[flag] = this.p[j];
  				}
		   }
		}else {
				 p[i] = this.p[j];
				 p[j] = this.p[i];
			 }
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
