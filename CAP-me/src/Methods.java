import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class Methods {
	
	/**
	 * random search algorithm
	 * It is a base algorithm, a metaheuristic must be better than this algorithm
	 * 
	 */
	public static Solution randomSearch() {
		Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 

		for (int q = 0; q < MAX_G; q++) {
			for (int k = 0; k < SCHEDULE_LENGTH; k++) {
				//To accept new solution always
				s = s.neighbor();
				if (s.cost < best.cost) {
					best = s;
					best.setLastImprove(q);
				} 
			}
		}
		return best;	
	}
	
	public static Solution basicHC() {
		Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 

		for (int q = 0; q < MAX_G; q++) {
			for (int k = 0; k < SCHEDULE_LENGTH; k++) {
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				if(d<0)
				{
					s=neighbor;
				}
				if (s.cost < best.cost) {
					best = s;
					best.setLastImprove(q);
				} 
			}
		}
		return best;	
	}
	
	public static Solution basicHC(Solution s) {
		//Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 

		for (int q = 0; q < MAX_G; q++) {
			//for (int k = 0; k < SCHEDULE_LENGTH; k++) 
			{
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				if(d<0)
				{
					s=neighbor;
				}
				if (s.cost < best.cost) {
					best = s;
					best.setLastImprove(q);
				} 
			}
		}
		return best;	
	}
	
	public static Solution basicSA() {
   		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        double t=100;
        
        for(int q=0;q<MAX_G;q++)
		{
			for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=s.neighbor();
				double p=Math.random();
				double d=neighbor.getCost()-s.getCost();
				if(d<0||p<1.0/Math.exp(Math.abs(d)/t))
				{
					s=neighbor;
					if(s.getCost()<best.getCost()) {
						best=s;
						best.setLastImprove(q);
					}
				}
			}
			t*=Simulations.alpha;
		}
        
 		return best;
	}
	
	
	

	
	public static Solution basicTA() {
   		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        double t=100;
        
        for(int q=0;q<MAX_G;q++)
		{
			for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				if(d<t)
				{
					s=neighbor;
					if(s.getCost()<best.getCost()) {
						best=s;
						best.setLastImprove(q);
					}
				}
			}
			t*=Simulations.alpha;
		}
        
 		return best;
	}
	
	
	public static Solution basicNM() {
   		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        double t =100;
        
        for(int q=0;q<MAX_G;q++)
		{
			for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				if(d< t* rand.nextDouble())
				{
					s=neighbor;
					if(s.getCost()<best.getCost()) {
						best=s;
						best.setLastImprove(q);
					}
				}
			}
			t*=Simulations.alpha;
		}
        
 		return best;
	}
	
	public static Solution LAHC() {
   		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        double [] hist=new double[6];
        for(int i=0;i<hist.length;i++)
        	hist[i]=best.getCost();        
        for(int q=0;q<MAX_G;q++)
		{
			for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				double d2=neighbor.getCost()-hist[q%hist.length];
				if(d<0||d2<0)
				{
					s=neighbor;
					if(s.getCost()<best.getCost()) {
						best=s;
						best.setLastImprove(q);
					}
				}				
			}
			hist[q%hist.length]=s.getCost();
		}
        
 		return best;
	}
	
	public static Solution LBSA() {
   		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        
        //construct temperature list
        PriorityQueue<Double> tempLists=new PriorityQueue<>();
        while(tempLists.size()<MAX_G)
        {
        	Solution neighbor=s.neighbor();
        	double d=neighbor.getCost()-s.getCost();
        	tempLists.offer(-Math.abs(d));
        	if(d<0)
        		s=neighbor;
        }
        
        for(int q=0;q<MAX_G;q++)
		{
        	double t=-tempLists.peek();
        	int counter=0;
			double totalTemp=0;
			for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=s.neighbor();
				double d=neighbor.getCost()-s.getCost();
				double p=Math.random();
				
				if(d<0||p<1.0/Math.exp(Math.abs(d)/t))
				{
					s=neighbor;
					if(s.getCost()<best.getCost()) {
						best=s;
						best.setLastImprove(q);
					}
					if(d>0)
					{
						totalTemp+=d/Math.log(1.0/p);
						counter++;
					}
				}				
			}
			if(counter!=0)
			{
				double x=-totalTemp/(10*counter);
				tempLists.remove();
				tempLists.offer(x);
				System.out.println(x);
			}
			
		}
        
 		return best;
	}
	
	public static Solution tabuSearch() {
		Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 
		Solution tabuList;
		

		for (int q = 0; q < MAX_G; q++) {
			for (int k = 0; k < SCHEDULE_LENGTH; k++) {
				//To accept new solution always
				s = s.neighbor();
				if (s.cost < best.cost) {
					best = s;
					best.setLastImprove(q);
				} 
			}
		}
		return best;	
	}
	
	/*
	public static Solution ILS() {
		Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 

		for (int q = 0; q < MAX_G; q++) {
			//for (int k = 0; k < SCHEDULE_LENGTH; k++) {
				
				best=basicHC(s);
				s=best;
				s.cost+=1000;
			//}
		}
		return best;	
	}*/
	
	public static Solution MLS() {
		Solution s = Solution.make(); 
		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 

		for (int q = 0; q < 3; q++) {
			//for (int k = 0; k < SCHEDULE_LENGTH; k++) {
				
				best=basicHC(s);
				s=Solution.make();
			//}
		}
		return best;	
	}
	
	public static Solution basicGeneticAlgotithm()
	{
		final int POP_SIZE=Simulations.popSize;
		final int MAX_G=Simulations.MAX_G;
		
		Solution[] pop=new Solution[POP_SIZE];
		Solution gBest=null;
		for(int i=0;i<pop.length;i++) {
			pop[i]=Solution.make();
		}
		Arrays.sort(pop);
		gBest=pop[0];
		for(int q=0;q<MAX_G;q++) {
			Solution[] offsprings=new Solution[Simulations.offs];
			
			//Select parents into mating pool
			int[] ids;
			double []fitness=new double[POP_SIZE];
			for(int i=0;i<POP_SIZE;i++) {
				fitness[i]=1.0/(pop[i].cost+1);
			}
			ids=GeneticAlgorithmOperators.tournamentSelection(fitness, offsprings.length, Simulations.tour);
			
			//Crossover and mutation
			int idx=0;
			while(idx<offsprings.length) {
				Solution[] ss=pop[ids[idx]].makeChild(pop[ids[idx]],pop[ids[idx+1]]);
				offsprings[idx]=ss[0];
				offsprings[idx+1]=ss[1];
				idx+=2;
			}
			
			//The elitist combined with fitness-based reinsertion
			Arrays.sort(offsprings);
			for(int i=0;i<Simulations.learningNum;i++) {
				offsprings[i]=basicHC(offsprings[i]);
			}
			Arrays.sort(offsprings);
			pop=replacement(pop,offsprings,Simulations.top);
			
			//Save best solution found
			if(pop[0].getCost()<gBest.getCost()) {
				gBest=pop[0];
				gBest.setLastImprove(q+1);
			}
			
		}

		return gBest;
	}
	
    
	private static Solution[] replacement(Solution[] pop, Solution[] off, int top) {
		// TODO Auto-generated method stub
		Solution[] merged=pop.clone();
		for(int i=merged.length-top,j=0;i<merged.length;i++,j++)
			merged[i]=off[j];
		Arrays.sort(merged);
		
		return merged;
	}

	/*
	private static Solution[] makeChild(Solution p1, Solution p2) {
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
	}*/
	
	/**
	 * basic Max-Min ant colony algorithm.
	 * 
	 * 
	 */
	public static Solution basicACO() {
		double alpha=Simulations.alpha_aco;
		double beta=Simulations.beta;
		double rho=Simulations.rho;
		
		Solution best = Solution.make(); 
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 
		double[][] pher=new double[facilityNumber+1][facilityNumber+1];
		double[][] heur=new double[facilityNumber+1][facilityNumber+1];
		pher=AntMethod.initPher(pher);

		for (int q = 0; q < MAX_G; q++) {
			//for (int k = 0; k < SCHEDULE_LENGTH; k++) 
			{
				Solution s=AntMethod.constructAntSolution(pher,heur,alpha,beta);
				s=basicHC(s);
				if (s.cost < best.cost) {
					best = s;
					best.setLastImprove(q);
				} 
				AntMethod.evaporatePher(pher, rho);
				AntMethod.depositPher(pher,s,rho);
			}
		}
		return best;	
	}
	
	public static Solution basicPSO() {
		final int POP_SIZE = Simulations.popSize;
		final int MAX_G = Simulations.MAX_G;
		final double W = 0.5;
		Solution[] pop = new Solution[POP_SIZE]; 
		Solution[] pBest = new Solution[POP_SIZE];
		Solution gBest = pop[0];
		ArrayList<SO> Vi;
		int len;
		for (int i = 0; i < pop.length; i++) {
			pop[i] = Solution.make(); 
			pBest[i] = pop[i];
		}
		Arrays.sort(pop);
		gBest = pop[0];
		List<ArrayList<SO>> ListV = PsoOperators.initListV();
		for(int t=0;t<MAX_G;t++) {
			for(int i = 0;i < POP_SIZE;i++) {
				ArrayList<SO> Vi_new = new ArrayList<SO>();
				Vi = ListV.get(i);
				//���Բ���W*Vi
				len = (int)(Vi.size()*W);
				for(int j = 0;j < len;j++) {
					Vi_new.add(Vi.get(j));
				}
				//������֪����pbest[i]-pop[i]
				ArrayList<SO> a = PsoOperators.minus(Parray(pBest[i]), Parray(pop[i]));
				double ra = rand.nextDouble();
				len = (int)(a.size()*ra);
				for(int j = 0;j < len ;j++) {
					if(!Vi_new.contains(a.get(j))) {
					Vi_new.add(a.get(j));}
					}
				//�����֪����gBest-pop[i]
				ArrayList<SO> b = PsoOperators.minus(Parray(gBest), Parray(pop[i]));
				double rb = rand.nextDouble();
				len = (int)(b.size()*rb);
				for(int j = 0;j < len ;j++) {
					if(!Vi_new.contains(b.get(j))) {
					Vi_new.add(b.get(j));}}
				ListV.add(i,Vi_new);
				//����λ��
				int[] s = PsoOperators.add(Parray(pop[i]), Vi_new);
				pop[i] = new SolutionOrder(s);
				//����pBest��gBest
				if(pop[i].getCost()<pBest[i].getCost())
					pBest[i] = new SolutionOrder(Parray(pop[i]));
				if(pop[i].getCost()<gBest.getCost()) {
					gBest = new SolutionOrder(Parray(pop[i]));
					gBest.setLastImprove(t);}}}
		return gBest;
	}
	
	public static int[] Parray(Solution s) {
		int[] p = null;
		if (s instanceof SolutionOrder) {
			 p = ((SolutionOrder)s).getP();
		}
		return p;
	}
	
	/**
	 * basic  particle swarm optimization.
	 *
	 * 
	 */
	public static Solution basicPSO_XW() {
		
		Solution best = Solution.make(); 
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 
		int popSize=Simulations.popSize;
		
		Solution[] pop=new Solution[popSize];
		for(int i=0;i<pop.length;i++) {
			pop[i]=Solution.make();
		}
		
		for (int q = 0; q < MAX_G; q++) {
			for (int k = 0; k < SCHEDULE_LENGTH; k++) 
			{
				
			}
		}
		return best;	
	}
	
	/**
	 * discrete cuckoo search
	 *
	 * 
	 */
	public static Solution basicCS() {
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
		final int MAX_G = Simulations.MAX_G; 
		
		double Pa=Simulations.Pa;
		int popSize=Simulations.popSize;
		Solution[] pop=new Solution[popSize];
		for(int i=0;i<pop.length;i++) {
			pop[i]=Solution.make();
		}
		Solution best=pop[0];

		for (int q = 0; q < MAX_G; q++) 
		{
			//search with smart cuckoo
			Arrays.sort(pop);
			for(int i=0;i<5;i++) {
				pop[i]=basicHC(pop[i]);
			}
			for (int i=0;i<pop.length;i++) {
				int[] ids;
				
				//levy flight
				Solution neighbor=CuckooSearch.levyFlight(pop[i]);
				if(neighbor.cost<pop[i].cost)
					pop[i]=neighbor;
				
				//random local walk
				if(rand.nextDouble()<Pa) {
					ids=CuckooSearch.randomNum(popSize,2);
					neighbor=CuckooSearch.localWalk(pop[i],pop[ids[0]],pop[ids[1]]);
					if(neighbor.cost<pop[i].cost)
						pop[i]=neighbor;
				}
				
				//selection
				if (pop[i].cost < best.cost) {
					best = pop[i];
					best.setLastImprove(q);
				} 
				
			}
			
			
		}
		return best;	
	}


	public static Random rand = new Random();
	
	
    public static void main(String[] args) {
    	String name = "01S9";
    	String fileName = (new File("")).getAbsolutePath() + "/datas/Mao24/" + name + ".txt";
    	Problem problem = null;
   	    problem = Problem.readProblem(fileName);
    	
    	double duration = (new java.util.Date()).getTime();
       	final int TIMES = 25;
       	double[] costs = new double[TIMES];
       	double totalCost = 0;
       	Solution bs = null;
       	for (int t = 0; t < TIMES; t++) {
       		Solution s = randomSearch();
       		System.out.println( t + "\t" + s.getCost() + "\t" + s.getLastImprove() + "\t" );
       		totalCost += s.cost;
       		costs[t] = s.cost;
       		if (bs == null || s.cost < bs.cost) {
       			bs = s;
       		}
       	}
     	duration = (new java.util.Date()).getTime()-duration;
		duration = Math.round(duration/1000*1000)/1000.0;
    	System.out.println(bs.cost + ", " + totalCost/TIMES + ", best known solution:" + problem.getBestCost());
    	System.out.println(bs);
        if (bs.cost < problem.getBestCost()) {
        	System.out.println("New best solution found!");
            bs.save((new File("")).getAbsolutePath() + "/results/" + name + ".txt" );
        }
     }
}


