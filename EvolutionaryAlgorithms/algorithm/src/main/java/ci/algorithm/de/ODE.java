package ci.algorithm.de;

import ci.Problem;
import ci.Solution;
import ci.problem.common.*;

public class ODE extends DE{

	double Jr;
	double threshold=1e-8;
	
	public ODE(Problem problem, long maxFunctionEvaluations, int populationSize,double Cr,double F,double Jr) {
		super(problem, maxFunctionEvaluations, populationSize,Cr,F);
		this.Jr=Jr;
	}
	
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		this.initPopOppoBased();
		
		bestSolution = population.get(0).copy();
		
		for (int q = populationSize; q < maxFunctionEvaluations; q=q+populationSize) {
			for(int i=0;i<populationSize;i++) {
				
				int[] ids;
				//mutation
				Solution[] mutaVector=new Solution[populationSize];
				ids=randomNum(populationSize,3);
				mutaVector[i]=makeMutaVector(bestSolution,population.get(ids[1]),population.get(ids[2]));
				
				//crossover
				Solution[] trialVector=new Solution[populationSize];
				trialVector[i]=makeCrossOver(population.get(i),mutaVector[i]);
				
				//selection
				if(trialVector[i].getObjective(0)<population.get(i).getObjective(0)) {
					population.set(i, trialVector[i].copy());
				}
				
				if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
					bestSolution=population.get(i).copy();
				}
			}
			
			if(randGenerator.nextDouble()<Jr) {
				this.initPopOppoBased();
				q=q+populationSize;
			}

			if(bestSolution.getObjective(0)<=threshold){
				NFE=q;
				System.out.println(NFE);
				break;
			}
		}
	}
	
	public static void main(String[] args) {
        Problem problem =new Griewank(30);

        ODE ode = new ODE(problem, (long) 1e+6, 100,0.9,0.5,0.3);
        ode.run();
        System.out.println(ode.getBestSolution());
    }

}
