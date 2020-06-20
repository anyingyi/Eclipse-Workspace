import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CuckooSearch {
	
	static Random rand=new Random();
	/**
	 * levy distribution.
	 * 
	 * @return a random number generate by levy distribution
	 */
	static double levy(double beta) {
		double x,y;
		x=doSigma(beta)*Solution.rand.nextGaussian();
		y=Math.pow(Math.abs(Solution.rand.nextGaussian()), 1/beta);
		return x/y;
	}
	
	private static double doSigma(double beta){
        double term1 = logGamma(beta+1)*Math.sin((Math.PI*beta)/2);
        double term2 = logGamma((beta+1)/2)*beta*Math.pow(2,(beta-1)/2);
        return Math.pow((term1/term2),(1/beta));
    }
    
    private static Double logGamma(double x){
        double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
        double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
        return Math.exp(tmp + Math.log(ser * Math.sqrt(2 * Math.PI)));
    }

	public static Solution levyFlight(Solution s) {
		// TODO Auto-generated method stub
		int[] order;
		double prob=vShapedFunc(CuckooSearch.levy(1.5));
		order=((SolutionOrder) s).getP();
		
		if(prob<0.6)
			moveNumOpt(s,2);
		else if(prob<0.8)
			moveNumOpt(s,3);
		else 
			moveNumOpt(s,4);

		return new SolutionOrder(order);
	}
	
	public static int[] randomNum(int popSize, int randVectorNum) {
		// TODO Auto-generated method stub
		int[] ids=new int[randVectorNum];
		List<Integer> p=new ArrayList<Integer>();
		for(int i=0;i<popSize;i++) {
			p.add(i);
		}
		Collections.shuffle(p);
		for(int i=0;i<randVectorNum;i++) {
			int temp=Methods.rand.nextInt(p.size());
			ids[i]=p.remove(temp);
		}
		
		return ids;
	}

	public static Solution localWalk(Solution solution, Solution solution2, Solution solution3) {
		// TODO Auto-generated method stub
		double deviate=solution2.getCost()-solution3.getCost();
		double prob=vShapedFunc(deviate);
		if(deviate<0.95) {
			return moveNumOpt(solution,2);
		}
		else {
			return moveNumOpt(solution,4);
		}
	}

	
	
	private static Solution moveNumOpt(Solution solution,int num) {
		// TODO Auto-generated method stub
		int[] ids;
		int[] order=((SolutionOrder) solution).getP();
		ids=randomNum(order.length,num);
		
		//swap
		int temp=order[ids[0]];
		for(int i=0;i<ids.length-1;i++) {
			order[ids[i]]=order[ids[i+1]];
		}
		order[ids[ids.length-1]]=temp;
		
		return new SolutionOrder(order);
	}
	
	static double vShapedFunc(double x) {
		double y;
		y=Math.abs(Math.tanh(x));
		return y;
	}
	
	static double sShapedFunc(double x) {
		double y=1/(1+Math.pow(Math.E, -x));
		return y;
	}


	
}
