package ci.algorithm.de;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.common.*;

public class DE extends EvolutionaryAlgorithm{
	double Cr;
	double F;
	double threshold=1e-8;
	long NFE;
	
	
	public DE(Problem problem, long maxFunctionEvaluations, int populationSize,double Cr,double F) {
		super(problem, maxFunctionEvaluations, populationSize);
		this.Cr=Cr;
		this.F=F;
		this.NFE=maxFunctionEvaluations;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		
		bestSolution = population.get(0).copy();
		
		for (long q = populationSize; q < maxFunctionEvaluations; q=q+populationSize) {
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
					population.set(i, trialVector[i].copy());
				}
				
				if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
					bestSolution=population.get(i).copy();
				}
			}
			/*
			if(bestSolution.getObjective(0)<=threshold){
				NFE=q;
				break;
			}

			 */
			
		}			
	
	}

	protected Solution makeCrossOver(Solution solution, Solution solution2) {
		// TODO Auto-generated method stub
		Solution temp=solution.copy();
		int k=randGenerator.nextInt(dimensionSize);
		for(int i=0;i<dimensionSize;i++) {
			if(randGenerator.nextDouble()<Cr||i==k) {
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
		return temp;
	}
	
	public static void main(String[] args) {
        Problem problem =new Rastrigin(30);
        
        DE de = new DE(problem, (long) 1e+6, 100,0.9,0.5);
        de.run();
        System.out.println(de.getBestSolution());
    }
}
