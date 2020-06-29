package ci.algorithm.nsgaii;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.algorithm.de.DE;

public class NSGAII extends DE{

	public NSGAII(Problem problem, long maxFunctionEvaluations, int populationSize) {
		super(problem, maxFunctionEvaluations, populationSize);
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		List<Solution> offsprings = new ArrayList<Solution>(populationSize);
		
		for(int q=0;q<maxFunctionEvaluations;q++) {
			
			for(int i=0;i<populationSize;i++) {
				int[] ids;
				//mutation
				Solution[] mutaVector=new Solution[populationSize];
				ids=randomNum(populationSize,3);
				mutaVector[i]=makeMutaVector(population.get(ids[0]),population.get(ids[1]),population.get(ids[2]));
				
				//crossover
				offsprings.add(makeCrossOver(population.get(i),mutaVector[i]));	
			}
			population=selection(population,offsprings);
			
		}
	}

	private List<Solution> selection(List<Solution> population, List<Solution> offsprings) {
		// TODO Auto-generated method stub
		List<Solution> temp=new ArrayList<Solution>(2*populationSize);
		for(int i=0;i<populationSize;i++) {
			temp.add(population.get(i));
		}
		for(int i=0;i<populationSize;i++) {
			temp.add(offsprings.get(i));
		}
		return null;
	}
}
