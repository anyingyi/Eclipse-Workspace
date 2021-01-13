import java.util.Arrays;
import java.util.Random;

public class Methods {
	public static Random rand=new Random();
	
	/**
	 * basic Cuckoo Search algorithm.
	 * 
	 * @return best solution
	 */
	public static Solution basicABC() {
		
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		
		Solution[] pop=new Solution[popSize];
		Solution[] newPop=new Solution[popSize];
		double[] fitness=new double[pop.length];
		Solution best=new Solution();
		for(int i=0;i<popSize;i++) {
			pop[i]=new Solution();
		}
		
		for (int q = 0; q < MAX_G; q++) {
			int[] ids;
			
			//employed bees phase
			for(int i=0;i<popSize;i++) {
				
				newPop[i]=ArtificialBeeColony.employedBee(pop[i],pop[Methods.rand.nextInt(popSize)]);
				if(newPop[i].getCost()<pop[i].getCost()) {
					pop[i]=newPop[i];
					pop[i].trailCount=0;
				}
				else {
					pop[i].trailCount++;
					if(pop[i].trailCount>=Simulations.threshold) {
						pop[i]=ArtificialBeeColony.scoutBee();
					}
				}
				
				//fitness
				fitness[i]=pop[i].getCost();
			}
			
			//onlooker bees phase
			for(int i=0;i<popSize;i++) {
				
				ids=ArtificialBeeColony.rouletteWheel(fitness, 1);
				newPop[ids[0]]=ArtificialBeeColony.employedBee(pop[ids[0]],pop[Methods.rand.nextInt(popSize)]);
				if(newPop[ids[0]].getCost()<pop[ids[0]].getCost()) {
					pop[ids[0]]=newPop[ids[0]];
					pop[ids[0]].trailCount=0;
				}
				else {
					pop[ids[0]].trailCount++;
					if(pop[ids[0]].trailCount>=Simulations.threshold) {
						pop[ids[0]]=ArtificialBeeColony.scoutBee();
					}
				}
				fitness[ids[0]]=pop[ids[0]].getCost();
			}
				
			for(int i=0;i<popSize;i++)
				if(pop[i].getCost()<best.getCost()) {
					best=pop[i];
				}
			
		}
		
		return best;
		
	}
	
	/**
	 * basic Cuckoo Search algorithm.
	 * 
	 * @return best solution
	 */
	public static Solution basicCuckoo() {
		
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		final double Pa=Simulations.Pa;
		final double Pc=Simulations.Pc;
		
		Solution[] pop=new Solution[popSize];
		Solution[] mutaVector=new Solution[popSize];
		Solution best=new Solution();
		for(int i=0;i<popSize;i++) {
			pop[i]=new Solution();
		}
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				int[] ids;
				
				//smart cuckoo search
				if(rand.nextDouble()<Pc) {
					
				}
				
				//global random walk
				mutaVector[i]=CuckooSearch.makeMutaVector(pop[i],best);
				if(mutaVector[i].getCost()<pop[i].getCost()) {
					pop[i]=mutaVector[i].copy();
				}
				
				//local random walk
				if(Solution.rand.nextDouble()<Pa) {
					ids=DifferentialEvolution.randomNum(popSize,2);
					mutaVector[i]=CuckooSearch.localRandom(pop[i],pop[ids[0]],pop[ids[1]]);	
					if(mutaVector[i].getCost()<pop[i].getCost()) {
						pop[i]=mutaVector[i].copy();
					}
				}

				if(pop[i].getCost()<best.getCost()) {
					best=pop[i].copy();
				}
			}
			if(q%30==0) {
				System.out.println("the times:"+q/30+"\t cost:"+best.getCost());
			}
		}
		
		return best;
		
	}
	
	/**
	 * basic Cuckoo Search algorithm.
	 * 
	 * @return best solution
	 */
	public static Solution dimensionCuckoo() {
		
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		final double Pa=Simulations.Pa;
		
		Solution[] pop=new Solution[popSize];
		Solution[] mutaVector=new Solution[popSize];
		Solution best=new Solution();
		for(int i=0;i<popSize;i++) {
			pop[i]=new Solution();
		}
		//pop=DifferentialEvolution.oppoBasedPopInit(pop);
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				int[] ids;
				
				//global random walk
				mutaVector[i]=CuckooSearch.makeMutaVector(pop[i],best);
				if(mutaVector[i].getCost()<pop[i].getCost()) {
					pop[i]=mutaVector[i];
				}
				
				//local random walk
				if(Solution.rand.nextDouble()<Pa) {
					ids=DifferentialEvolution.randomNum(popSize,2);
					mutaVector[i]=CuckooSearch.localRandomDimension(pop[i],pop[ids[0]],pop[ids[1]]);	
					if(mutaVector[i].getCost()<pop[i].getCost()) {
						pop[i]=mutaVector[i];
					}
				}

				if(pop[i].getCost()<best.getCost()) {
					best=pop[i];
				}
			}
		}
		
		return best;
		
	}
	
	/**
	 * basic differential evolution algorithm.
	 * DE/best/1/bin.
	 * @return best solution
	 */
	public static Solution basicDE() {
		
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		int diffVectorNum=Simulations.diffVectorNum;
		int randVectorNum=1+2*diffVectorNum;
		final double Cr=Simulations.Cr;
		
		Solution[] pop=new Solution[popSize];
		Solution[] mutaVector=new Solution[popSize];
		Solution best=new Solution();
		for(int i=0;i<popSize;i++) {
			pop[i]=new Solution();
		}
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				int[] ids;
				//mutation
				ids=DifferentialEvolution.randomNum(popSize,randVectorNum);
				mutaVector[i]=DifferentialEvolution.makeMutaVector(best,pop[ids[1]],pop[ids[2]]);
				
				//crossover
				Solution[] trialVector=new Solution[popSize];
				trialVector[i]=DifferentialEvolution.makeCrossOver(pop[i],mutaVector[i]);
				
				//selection
				if(trialVector[i].getCost()<pop[i].getCost()) {
					pop[i]=trialVector[i];
				}
				
				if(pop[i].getCost()<best.getCost()) {
					best=pop[i];
				}
			}
		}
		
		return best;
		
	}
	
	/**
	 * opposition differential evolution algorithm.
	 * DE/best/1/bin.
	 * @return best solution
	 */
	public static Solution ODE() {
		
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		int diffVectorNum=Simulations.diffVectorNum;
		int randVectorNum=1+2*diffVectorNum;
		double Jr=Simulations.Jr;
		
		//opposition-based population initialize
		Solution[] pop=new Solution[popSize];
		Solution[] mutaVector=new Solution[popSize];
		Solution best=new Solution();
		for(int i=0;i<popSize;i++) {
			pop[i]=new Solution();
		}
		pop=DifferentialEvolution.oppoBasedPopInit(pop);
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				int[] ids;
				//mutation
				ids=DifferentialEvolution.randomNum(popSize,randVectorNum);
				mutaVector[i]=DifferentialEvolution.makeMutaVector(pop[ids[0]],pop[ids[1]],pop[ids[2]]);
				
				//crossover
				Solution[] trialVector=new Solution[popSize];
				trialVector[i]=DifferentialEvolution.makeCrossOver(pop[i],mutaVector[i]);
				
				//selection
				if(trialVector[i].getCost()<pop[i].getCost()) {
					pop[i]=trialVector[i];
				}
				
				//
				if(Math.random()<Jr) {
					pop=DifferentialEvolution.oppoBasedPopInit(pop);
				}
				
				
				if(pop[i].getCost()<best.getCost()) {
					best=pop[i];
				}
			}
		}
		
		return best;
		
	}
	
	
	/**
	 * basic particle swarm optimization.
	 * 
	 * @return best solution
	 */
	public static Solution basicPSO()
	{
		int popSize=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		
		Solution[] pop=new Solution[popSize];
		Solution[] pBest = new Solution[popSize];
		Solution gBest = new Solution();
		for(int i=0;i<pop.length;i++) {
			pop[i]=new Solution();
			pBest[i]=pop[i];
			if(pop[i].getCost()<gBest.getCost()) {
				gBest=pop[i];
			}
		}
		
		Arrays.sort(pop);
		
		for (int q = 0; q < MAX_G; q++) {
			for(int i=0;i<popSize;i++) {
				
				pop[i]=popUpdate(pop[i],pBest[i],gBest);
				
				if(pop[i].getCost()<pBest[i].getCost()) {
					pBest[i]=pop[i];
				}
				if(pop[i].getCost()<gBest.getCost()) {
					gBest=pop[i];
				}
			}
		}
		
		return gBest;
	}

	/**
	 * update the particle situation of PSO.
	 * 
	 * @return the solution after update.
	 */
	private static Solution popUpdate(Solution solution,Solution pBest, Solution gBest) {
		double c1=Simulations.c1;
		double c2=Simulations.c2;
		double w=Simulations.w;
		
		double[] pos=solution.getPosition();
		double[] pos2=gBest.getPosition();
		
		double[] velocity=new double[pos.length] ;
		double[] position=new double[pos.length];
		for(int i=0;i<pos.length;i++) {
			velocity[i]=w*solution.getVelocity()[i]+c1*rand.nextDouble()*(pBest.getPosition()[i]-pos[i])+c2*rand.nextDouble()*(pos2[i]-pos[i]);
			position[i]=pos[i]+velocity[i];
			position[i]=Solution.adjustValue(position[i], velocity[i])[0];
			velocity[i]=Solution.adjustValue(position[i], velocity[i])[1];
		}
		
		return new Solution(position,velocity);
	}

}
