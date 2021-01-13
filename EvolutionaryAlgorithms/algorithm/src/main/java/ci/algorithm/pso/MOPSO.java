package ci.algorithm.pso;

import java.util.ArrayList;
import java.util.List;

import ci.Problem;
import ci.Solution;

public class MOPSO extends PSO{
	List<Solution> gBest=new ArrayList<>();
	List<Solution>[] pBest;
	public MOPSO(Problem problem, long maxFunctionEvaluations, int populationSize) {
		super(problem, maxFunctionEvaluations, populationSize);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void run() {
		
		//initialize
		velocity=new List[populationSize];
		this.initialPopulation();
		for(int i=0;i<problem.getNumberOfObjectives();i++) {
			gBest.add(population.get(i));
		}
		bestSolution=population.get(0).copy();
		for(int i=0;i<populationSize;i++) {
			velocity[i]=new ArrayList<>();
			for (int k = 0; k < dimensionSize; k++) {
	            double value = randGenerator.nextDouble()*(problem.getLowerBound(k) + randGenerator.nextDouble() * (problem.getUpperBound(k) - problem.getLowerBound(k)));
	            velocity[i].add(value);
	        }
			pBestSolution.add(population.get(i).copy());
			for(int k=0;k<problem.getNumberOfObjectives();k++) {
				if(population.get(i).getObjective(k)<gBest.get(k).getObjective(k)) {
					gBest.set(k, population.get(i).copy());
				}
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

		/*
		//for multi-object to get it's pBest and gBest
		double[][] gBpos=new double[2][];
		double[][] pBpos=new double[2][];
		double[] gBpo=new double[dimensionSize];
		double[] pBpo=new double[dimensionSize];
		double[] dgBpo=new double[dimensionSize];
		double[] dpBpo=new double[dimensionSize];
		for(int i=0;i<gBest.length;i++) {
			gBpos[i]=gPos;
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

		for(int i=0;i<dimensionSize;i++) {
			double value=w*velocity[k].get(i)+c1*randGenerator.nextDouble()*(pPos.get(i)-cPos.get(i))+c2*randGenerator.nextDouble()*(gPos.get(i)-cPos.get(i));
			velocity[k].set(i, value);
			temp.setVariableValue(i, temp.getVariableValue(i)+velocity[k].get(i));
			if (this.problem.isRepairSolution()) {
	            temp.repairSolutionVariableValue(i);
	        }
		}
		problem.evaluate(temp);*/
		
		return temp;
	}

}
