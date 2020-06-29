package ci.algorithm.de;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.common.Sphere;

public class DE extends EvolutionaryAlgorithm{
	double Cr=0.5;
	double F=0.5;
	public DE(Problem problem, long maxFunctionEvaluations, int populationSize) {
		super(problem, maxFunctionEvaluations, populationSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		bestSolution = population.get(0).copy();
		
		for (int q = 0; q < maxFunctionEvaluations; q++) {
			for(int i=0;i<populationSize;i++) {
				
				int[] ids;
				//mutation
				Solution[] mutaVector=new Solution[populationSize];
				ids=randomNum(populationSize,3);
				mutaVector[i]=makeMutaVector(population.get(ids[0]),population.get(ids[1]),population.get(ids[2]));
				
				//crossover
				Solution[] trialVector=new Solution[populationSize];
				trialVector[i]=makeCrossOver(population.get(i),mutaVector[i]);
				
				//selection
				if(trialVector[i].getObjective(0)<population.get(i).getObjective(0)) {
					population.set(i, trialVector[i]);
				}
				
				if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
					bestSolution=population.get(i).copy();
				}
			}
		}			
	
	}

	protected Solution makeCrossOver(Solution solution, Solution solution2) {
		// TODO Auto-generated method stub
		Solution temp=solution.copy();

		for(int i=0;i<dimensionSize;i++) {
			if(randGenerator.nextDouble()<Cr) {
				temp.setVariableValue(i, solution2.getVariableValue(i));
			}
			if (this.problem.isRepairSolution()) {
	            temp.repairSolutionVariableValue(i);
	        }
		}
		
		this.problem.evaluate(temp);
		return temp;
	}

	protected Solution makeMutaVector(Solution solution, Solution solution2, Solution solution3) {
		// TODO Auto-generated method stub
		Solution temp=solution.copy();

		for(int i=0;i<dimensionSize;i++) {
			temp.setVariableValue(i, solution.getVariableValue(i)+F*(solution2.getVariableValue(i)-solution3.getVariableValue(i)));
			if (this.problem.isRepairSolution()) {
	            temp.repairSolutionVariableValue(i);
	        }
		}
		this.problem.evaluate(temp);
		return temp;
	}
	
	public static void main(String[] args) {
        Problem problem =new Sphere(30);

        DE abc = new DE(problem, 3000, 30);
        abc.run();
        System.out.println(abc.getBestSolution().getObjective(0));
    }
}
