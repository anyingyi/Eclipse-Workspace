
public class CuckooSearch {

	public static Solution makeMutaVector(Solution solution, Solution best) {
		// TODO Auto-generated method stub
		double alpha0=Simulations.alpha0;
		double beta=Simulations.beta;
		double[] position=new double[solution.getPosition().length];
		
		//levy flight
		for(int i=0;i<solution.getPosition().length;i++) {
			position[i]=solution.getPosition()[i]+alpha0*(solution.getPosition()[i]-best.getPosition()[i])*levy(beta);
			position[i]=Solution.adjustValue(position[i], 0)[0];
		}
		return new Solution(position,null);
	}
	
	
	/**
	 * levy distribution.
	 * 
	 * @return a random number generate by levy distribution
	 */
	private static double levy(double beta) {
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


	public static Solution localRandom(Solution solution, Solution solution2, Solution solution3) {
		double[] position=new double[solution.getPosition().length];
		for(int i=0;i<solution.getPosition().length;i++) {
			position[i]=solution.getPosition()[i]+Solution.rand.nextDouble()*(solution2.getPosition()[i]-solution3.getPosition()[i]);
			position[i]=Solution.adjustValue(position[i], 0)[0];
		}
		
		return new Solution(position,null);
	}
	
	public static Solution localRandomDimension(Solution solution, Solution solution2, Solution solution3) {
		double[] position=new double[solution.getPosition().length];
		double[] pos=solution.getPosition().clone();
		double[] pos1=solution.getPosition().clone();
		Solution s;
		for(int i=0;i<solution.getPosition().length;i++) {
			position[i]=solution.getPosition()[i]+Solution.rand.nextDouble()*(solution2.getPosition()[i]-solution3.getPosition()[i]);
			
			position[i]=Solution.adjustValue(position[i], 0)[0];
			pos[i]=position[i];
			s=new Solution(pos,null);
			if(s.getCost()<solution.getCost())
				pos[i]=position[i];
			else {
				pos[i]=pos1[i];
			}
		}
		
		return new Solution(pos,null);
	}

}
