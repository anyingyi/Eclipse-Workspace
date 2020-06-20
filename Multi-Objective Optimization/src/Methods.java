import java.util.Arrays;
import java.util.Random;

public class Methods {
	public static Random rand=new Random();
	
	
	
	
	/**
	 * basic particle swarm optimization.
	 * 
	 * @return best solution
	 */
	public static Solution[] MOPSO()
	{
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		int objFuncNum=Simulations.objFuncNum;
		
		Solution[] pop=new Solution[popSize];
		Solution[][] pBest = new Solution[popSize][objFuncNum];
		Solution[] gBest = new Solution[objFuncNum];
		for(int i=0;i<objFuncNum;i++) {
			gBest[i]=new Solution();
		}
		for(int i=0;i<pop.length;i++) {
			pop[i]=new Solution();
			
			for(int j=0;j<objFuncNum;j++) {
				pBest[i][j]=pop[i];
				if(pop[i].getCost()[j]<gBest[j].getCost()[j]) {
					gBest[j]=pop[i];
				}
			}
				
		}
		
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				
				pop[i]=popUpdate(pop[i],pBest[i],gBest);
				
				for(int k=0;k<objFuncNum;k++)
				{
					if(pop[i].getCost()[k]<pBest[i][k].getCost()[k]) {
						pBest[i][k]=pop[i];
					}
					if(pop[i].getCost()[k]<gBest[k].getCost()[k]) {
						gBest[k]=pop[i];
					}
				}
					
			}
		}
		//System.out.println(gBest[0]);
		//System.out.println(gBest[1]);
		return pop;
	}

	/**
	 * update the particle situation of PSO.
	 * 
	 * @return the solution after update.
	 */
	private static Solution popUpdate(Solution solution,Solution[] pBest, Solution[] gBest) {
		double c1=Simulations.c1;
		double c2=Simulations.c2;
		double w=Simulations.w;
		
		//for multi-object to get it's pBest and gBest
		double[][] gBpos=new double[2][];
		double[][] pBpos=new double[2][];
		double[] gBpo=new double[Simulations.dimension];
		double[] pBpo=new double[Simulations.dimension];
		double[] dgBpo=new double[Simulations.dimension];
		double[] dpBpo=new double[Simulations.dimension];
		for(int i=0;i<gBest.length;i++) {
			gBpos[i]=gBest[i].getPosition();
		}
		for(int i=0;i<gBest.length;i++) {
			pBpos[i]=pBest[i].getPosition();
		}
		for(int i=0;i<Simulations.dimension;i++) {
			gBpo[i]=(gBpos[0][i]+gBpos[1][i])/2;
			dgBpo[i]=Math.pow((gBpos[0][i]-gBpos[1][i]),2);
			pBpo[i]=(pBpos[0][i]+pBpos[1][i])/2;
			dpBpo[i]=Math.pow((pBpos[0][i]-pBpos[1][i]),2);
			if(dpBpo[i]<dgBpo[i]) {
				if(rand.nextDouble()<0.5)
					pBpo[i]=pBpos[0][i];
				else
					pBpo[i]=pBpos[1][i];
			}
			else
			{
				pBpo[i]=(pBpos[0][i]+pBpos[1][i])/2;
			}
		}
		
		double[] pos=solution.getPosition();
		
		double[] velocity=new double[pos.length] ;
		double[] position=new double[pos.length];
		for(int i=0;i<pos.length;i++) {
			velocity[i]=w*solution.getVelocity()[i]+c1*rand.nextDouble()*(pBpo[i]-pos[i])+c2*rand.nextDouble()*(gBpo[i]-pos[i]);
			position[i]=pos[i]+velocity[i];
			position[i]=Solution.adjustValue(position[i], velocity[i])[0];
			velocity[i]=Solution.adjustValue(position[i], velocity[i])[1];
		}
		
		return new Solution(position,velocity);
	}

}
