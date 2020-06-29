
public class ArtificialBeeColony {

	public static Solution employedBee(Solution pop, Solution pop2) {
		double[] pos=pop.getPosition().clone();
		int id=Methods.rand.nextInt(Simulations.dimension);
		pos[id]=pos[id]+(pos[id]-pop2.getPosition()[id]);
		return new Solution(pos,null);
		
	}
	
	/** 
     * use roulette wheel to generate an array of mating pool
     * 
     * @param fitness  fitness of parent solution
     * @param num  the number of generating offspring
     */
	public static int[] rouletteWheel(double[] fitness, int num) {
		// TODO Auto-generated method stub
		double[] fites=new double[fitness.length];
		for(int i=0;i<fitness.length;i++)
		{
			fites[i]=fitness[i];
			if(i!=0) {
				fites[i]+=fites[i-1];
			}
		}
		
		return rw(fites,num);
	}

	private static int[] rw(double[] fites, int num) {
		// TODO Auto-generated method stub
		int[] ids=new int[num];
		for(int id=0;id<ids.length;id++) {
			double fit=Solution.rand.nextDouble()*fites[fites.length-1];
			for(int i=0;i<fites.length;i++) {
				if(fit<fites[i]) {
					ids[id]=i;
					break;
				}
			}
		}
		return ids;
	}

	public static Solution scoutBee() {
		// TODO Auto-generated method stub
		return new Solution();
	}

}
