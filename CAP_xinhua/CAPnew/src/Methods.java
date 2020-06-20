import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
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
	
	public static Solution basicSA() {
		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        final int t0 = 500;
        final double Alpha = Simulations.alpha;
        double[] cost = new double[MAX_G];
        double[] temperatures = new double[MAX_G];
        double[] bcost = new double[MAX_G];
        double t = t0;
        for(int q = 0;q < MAX_G;q++) {
        	temperatures[q] = t;
        	for(int k = 0;k < facilityNumber *SCHEDULE_LENGTH;k++) {
        		Solution neighbor = s.neighbor();
				double d = neighbor.getCost()-s.getCost();
				if(d < 0||rand.nextDouble()<Math.exp(d/t)) {
					s = neighbor;
					if(s.getCost() < best.getCost()) {
						best = s;
						best.setLastImprove(q);
					}		
				}			
				cost[q] += s.getCost();
			}
			t *= Alpha;	
			cost[q] /= SCHEDULE_LENGTH;
			bcost[q] = best.getCost();
		}
		if (Simulations.isSavingProcessData()) Methods.saveConvergenceData(temperatures, cost, bcost);
 		return best;
	}	
	
	public static Solution basicTA() {
		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        final int t0 = 500;
        final double Alpha = Simulations.alpha;
        double[] cost = new double[MAX_G];
        double[] temperatures = new double[MAX_G];
        double[] bcost = new double[MAX_G];
        double t = t0;
        for(int q = 0;q < MAX_G;q++) {
        	temperatures[q] = t;
        	for(int k = 0;k < SCHEDULE_LENGTH;k++) {
        		Solution neighbor = s.neighbor();
				double d = neighbor.getCost()-s.getCost();
				if(d < t) {
					s = neighbor;
					if(s.getCost() < best.getCost()) {
						best = s;
						best.setLastImprove(q);
					}		
				}			
				cost[q] += s.getCost();
			}
			t *= Alpha;	
			cost[q] /= SCHEDULE_LENGTH;
			bcost[q] = best.getCost();
		}
		if (Simulations.isSavingProcessData()) Methods.saveConvergenceData(temperatures, cost, bcost);
 		return best;
	}
	
	public static Solution basicNM() {
		Solution s = Solution.make(); 
   		Solution best = s;
		int facilityNumber = Problem.get().getFacNum();
		final int SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
        final int MAX_G = Simulations.MAX_G; 
        final int t0 = 500;
        final double Alpha = Simulations.alpha;
        double[] cost = new double[MAX_G];
        double[] temperatures = new double[MAX_G];
        double[] bcost = new double[MAX_G];
        double t = t0;
        for(int q = 0;q < MAX_G;q++) {
        	temperatures[q] = t;
        	for(int k = 0;k < SCHEDULE_LENGTH;k++) {
        		Solution neighbor = s.neighbor();
				double d = neighbor.getCost()-s.getCost();
				if(d < t*rand.nextDouble()) {
					s = neighbor;
					if(s.getCost() < best.getCost()) {
						best = s;
						best.setLastImprove(q);
					}		
				}			
				cost[q] += s.getCost();
			}
			t *= Alpha;	
			cost[q] /= SCHEDULE_LENGTH;
			bcost[q] = best.getCost();
		}
		if (Simulations.isSavingProcessData()) Methods.saveConvergenceData(temperatures, cost, bcost);
 		return best;
	}
	
	public static Solution basicGeneticAlgorithm() {
		final int POP_SIZE = Simulations.popSize;
		final int MAX_G = Simulations.MAX_G;

		Solution[] pop = new Solution[POP_SIZE]; //
		Solution gBest = null;
		for (int i = 0; i < pop.length; i++) {
			pop[i] = Solution.make(); 
		}
		Arrays.sort(pop);
		gBest = pop[0];
		for (int q = 0; q < MAX_G; q++) {
			Solution[] offsprings = new Solution[Simulations.offspringSize];
			
			//Select parents into mating pool
			int[] ids;
			double[] fitness = new double[POP_SIZE];
			for (int i = 0; i < POP_SIZE; i++) {
				fitness[i] = 1.0 / (pop[i].cost + 1.0);
			}
			ids = GeneticAlgorithmOperators.rouletteWheel(fitness, offsprings.length);

			//Crossover and mutation
			int idx = 0;
			while (idx < offsprings.length) {
				Solution[] ss = makeChild(pop[ids[idx]], pop[ids[idx+1]]);
				offsprings[idx] = ss[0];
				offsprings[idx+1] = ss[1];
				idx += 2;
			}

			//The elitist combined with fitness-based reinsertion 
			Arrays.sort(offsprings);
			for (int i = 0; i < Simulations.learningNum; i++) {
				offsprings[i] = offsprings[i].localSearch();
			}
			Arrays.sort(offsprings);
			pop = replacement(pop, offsprings, Simulations.top);
			
			//Save best solution found
			if ( pop[0].getCost() < gBest.getCost()) {
				gBest = pop[0];
				gBest.setLastImprove(q+1);
			}
		}//for each iteration
		return gBest;
	}
	
    private static Solution[] makeChild(Solution p1, Solution p2) {
    	int[][] pPer = new int[2][];
    	int[][] pRow = new int[2][];
    	if (p1 instanceof SolutionOrderBit && p2 instanceof SolutionOrderBit) {
    		pPer[0] = ((SolutionOrderBit)p1).getP();
    		pRow[0] = ((SolutionOrderBit)p1).getRow();
    		pPer[1] = ((SolutionOrderBit)p2).getP();
    		pRow[1] = ((SolutionOrderBit)p2).getRow();
    	}
    	
    	//Crossover
   		int[][] cPer = GeneticAlgorithmOperators.positionBasedCrossover(pPer[0], pPer[1]);//
        int[][] cRow = GeneticAlgorithmOperators.twoPointCrossover(pRow[0], pRow[1]);
    	
        //mutation
    	for (int i = 0; i < cPer.length; i++) {
     		GeneticAlgorithmOperators.swap(cPer[i], 1);
     		int j = rand.nextInt(cRow[i].length);
     		cRow[i][j] = 1 - cRow[i][j];
      	}
    	return new Solution[]{new SolutionOrderBit(cPer[0], cRow[0]), new SolutionOrderBit(cPer[1], cRow[1])};
    }	
    
    private static Solution[] replacement(Solution[] pop, Solution[] off, int top) {
		Solution[] merged = new Solution[pop.length];
		int idx = 0;
		int newIdx = 0;
		for (int i = 0; i < merged.length;) {
			if (pop[idx].getCost() < off[newIdx].getCost()) {
				merged[i++] = pop[idx++];
			} else if (pop[idx].getCost() > off[newIdx].getCost()) {
				merged[i++] = off[newIdx++];
			} else if (pop[idx].getCost() == off[newIdx].getCost()) {
				if (rand.nextDouble() < 0.5) {
					merged[i++] = pop[idx++];
				} else {
					merged[i++] = off[newIdx++];
				}
			}
			
			if (newIdx >= top) {
				while (i < merged.length) {
					merged[i++] = pop[idx++];
				}
			}
			if (idx >= pop.length - top) {
				while (i < merged.length) {
					merged[i++] = off[newIdx++];
				}
			}
		}
		return merged;   	
    }
    
	/**
	 * A permutation can be obtained if the ith variable is not allowed to take the values instantiated by the previous variables. 
	 * To do that, when ith variable has to be sampled, the probability of the previous sampled values is set to 0 
	 * and the local probabilities of the rest of the values are normalized to sum 1.
	 * 
	 * @return
	 */

	public static Solution populationBasedIncrementalLearning() {
		int facNumber = Problem.get().getFacNum();
        final int POP_SIZE = Simulations.popSize;
        final int MAX_GEN = Simulations.MAX_G;// * facNumber;
 		double[][] perModel = new double[facNumber][facNumber]; //probability model for permutation
		for (int i = 0; i < facNumber; i++) {
			for (int j = 0; j < facNumber; j++) {
			    perModel[i][j] = 1.0/facNumber; 
			}
		}

		double[][] rowModel = new double[facNumber][2]; //probability model for row

		for (int i = 0; i < rowModel.length; i++) {
			for (int j = 0; j < rowModel[i].length; j++) {
			    rowModel[i][j] = 0.5;
			}
		}
		
		Solution[] pop = new Solution[POP_SIZE]; //
		Solution gBest = null;
		
		for (int ite = 0; ite < MAX_GEN ; ite++) {
			//construct new solution using probability model
			for (int i = 0; i < POP_SIZE; i++) {
				List<Integer> neighbors = new LinkedList<>();
				for (int j = 0; j < facNumber; j++) {
					neighbors.add(j);
				}
				int[] y = new int[facNumber];	
				int pos = -1;
				while (!neighbors.isEmpty()) {
					pos++;
					double[] ph = new double[neighbors.size()]; //use candidate list 
					for (int j = 0; j < ph.length; j++) {
						ph[j] = perModel[neighbors.get(j)][pos];
						if (j > 0) {
							ph[j] += ph[j - 1];
						}
					}
					
					double p = ph[ph.length-1] * Methods.rand.nextDouble();
					for (int j = 0; j < ph.length; j++) {
						if (p <= ph[j]) {
							y[pos] = neighbors.get(j);
							neighbors.remove(j);
							break;
						}
					}
				}

				int[] row = new int[facNumber];
				for (int j = 0; j < row.length; j++) {
                    double p0 = rowModel[j][0]/(rowModel[j][0] + rowModel[j][1]);
                    if (rand.nextDouble() < p0) {
                    	row[j] = 0;
                    } else {
                    	row[j] = 1;
                    }
				}

				Solution s = new SolutionOrderBit(y, row);
				pop[i] = s;
			}
			Arrays.sort(pop);
			for (int i = 0; i < Simulations.learningNum; i++) {
                pop[i] = pop[i].localSearch();//.swapHC();//.VND();
			}
			Arrays.sort(pop);
			if (gBest == null || pop[0].cost < gBest.cost) {
				gBest = pop[0];
				gBest.setLastImprove(ite);
			}

			for (int i = 0; i < perModel.length; i++) {
			    for (int j = 0; j < perModel[i].length; j++) {
			    	perModel[i][j] = (1-Simulations.rou) * perModel[i][j];
			    }
            }
			for (int i = 0; i < Simulations.bestSize; i++) {
				int[] p = ((SolutionOrderBit)pop[i]).getP();
				double scale = 1.0;//pop[Simulations.bestSize/2].getCost() / pop[i].getCost();
				for (int j = 0; j < p.length; j++) {
					perModel[p[j]][j] += Simulations.rou / Simulations.bestSize * scale;
				}
			}

			for (int i = 0; i < rowModel.length; i++) {
			    for (int j = 0; j < rowModel[i].length; j++) {
			    	rowModel[i][j] = (1-Simulations.rou) * rowModel[i][j];
			    }
            }
			for (int i = 0; i < Simulations.bestSize; i++) {
				int[] row;
				row = ((SolutionOrderBit)pop[i]).getRow();
				double scale = 1.0;//pop[Simulations.bestSize/2].getCost() / pop[i].getCost();
				for (int j = 0; j < row.length; j++) {
					rowModel[j][row[j]] += Simulations.rou / Simulations.bestSize * scale;
				}
			}			
		}//for each iteration
		return gBest;
	}
	
	public static Solution basicPSO() {
		final int POP_SIZE = Simulations.popSize;
		final int MAX_G = Simulations.MAX_G;
		final double W = Simulations.W_value;
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
				//惯性部分W*Vi
				len = (int)(Vi.size()*W);
				for(int j = 0;j < len;j++) {
					Vi_new.add(Vi.get(j));
				}
				//自我认知部分pbest[i]-pop[i]
				ArrayList<SO> a = PsoOperators.minus(Parray(pBest[i]), Parray(pop[i]));
				double ra = rand.nextDouble();
				len = (int)(a.size()*ra);
				for(int j = 0;j < len ;j++) {
					if(!Vi_new.contains(a.get(j))) {
					Vi_new.add(a.get(j));}
					}
				//社会认知部分gBest-pop[i]
				ArrayList<SO> b = PsoOperators.minus(Parray(gBest), Parray(pop[i]));
				double rb = rand.nextDouble();
				len = (int)(b.size()*rb);
				for(int j = 0;j < len ;j++) {
					if(!Vi_new.contains(b.get(j))) {
					Vi_new.add(b.get(j));}}
				ListV.add(i,Vi_new);
				//更新位置
				int[] s = PsoOperators.add(Parray(pop[i]), Vi_new);
				pop[i] = new SolutionOrder(s);
				//更新pBest和gBest
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
	private static void saveConvergenceData( double[] ts, double[] cs, double[] bs) {
		try {
			String f = Problem.fileName;
			File file = new File(f);
			f = (new File("")).getAbsolutePath() + "\\results\\Convergence\\" + file.getName();
			f += " " +Simulations.MAX_G+","+" for KP results.csv";
			PrintWriter printWriter = new PrintWriter(new FileWriter(f));
			for (int idx=0; idx<ts.length; idx++) {
				printWriter.println(ts[idx] + "," + cs[idx] + "," + bs[idx]);
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
	private static Random rand = new Random();
	
	
    public static void main(String[] args) {
    	String name = "QAP_sko42_05_n";
    	String fileName = (new File("")).getAbsolutePath() + "/datas/Mao24/" + name + ".txt";
    	Problem problem = null;
   	    problem = Problem.readProblem(fileName);
    	
    	double duration = (new java.util.Date()).getTime();
       	final int TIMES = 1;
       	double[] costs = new double[TIMES];
       	double totalCost = 0;
       	Solution bs = null;
       	for (int t = 0; t < TIMES; t++) {
       		Solution s = basicPSO();
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


