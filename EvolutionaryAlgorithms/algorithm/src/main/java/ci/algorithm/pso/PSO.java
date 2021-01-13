package ci.algorithm.pso;

import java.util.ArrayList;
import java.util.List;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.algorithm.de.DE;
import ci.problem.common.Sphere;

public class PSO extends EvolutionaryAlgorithm{
	protected double c1=1.5;
	protected double c2=1.5;
	protected double w=0.5;
	protected List<Double>[] velocity;
	protected List<Solution> pBestSolution=new ArrayList<>();

	public PSO(Problem problem, long maxFunctionEvaluations, int populationSize) {
		super(problem, maxFunctionEvaluations, populationSize);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// initialize
		velocity=new List[populationSize];
		this.initialPopulation();
		bestSolution=population.get(0).copy();
		for(int i=0;i<populationSize;i++) {
			velocity[i]=new ArrayList<>();
			for (int k = 0; k < dimensionSize; k++) {
	            double value = randGenerator.nextDouble()*(problem.getLowerBound(k) + randGenerator.nextDouble() * (problem.getUpperBound(k) - problem.getLowerBound(k)));
	            velocity[i].add(value);
	        }
			pBestSolution.add(population.get(i).copy());
			if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
				bestSolution=population.get(i).copy();
			}
		}
		
		
		
		for (int q = 0; q < maxFunctionEvaluations; q++) {
			for(int i=0;i<populationSize;i++) {
				
				population.set(i, popUpdate(population.get(i),pBestSolution.get(i),bestSolution,i));
				if(population.get(i).getObjective(0)<pBestSolution.get(i).getObjective(0)) {
					pBestSolution.set(i, population.get(i).copy());
				}
				if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
					bestSolution=population.get(i).copy();
				}
			}
		}

	}
	
	
	/**
	 * update the particle situation of PSO.
	 * 
	 * @return the solution after update.
	 */
	protected Solution popUpdate(Solution solution,Solution pBest, Solution gBest,int k) {
		List<Double> cPos=solution.getVariable();
		List<Double> pPos=pBest.getVariable();
		List<Double> gPos=gBest.getVariable();
		Solution temp=solution.copy();

		for(int i=0;i<dimensionSize;i++) {
			double value=w*velocity[k].get(i)+c1*randGenerator.nextDouble()*(pPos.get(i)-cPos.get(i))+c2*randGenerator.nextDouble()*(gPos.get(i)-cPos.get(i));
			velocity[k].set(i, value);
			temp.setVariableValue(i, temp.getVariableValue(i)+velocity[k].get(i));
			if (this.problem.isRepairSolution()) {
	            temp.repairSolutionVariableValue(i);
	        }
		}
		problem.evaluate(temp);
		
		return temp;
	}
	
	
	public static void main(String[] args) {
        Problem problem =new Sphere(30);

        PSO pso = new PSO(problem, 3000, 30);
        pso.run();
        System.out.println(pso.getBestSolution().getObjective(0));
    }
}
