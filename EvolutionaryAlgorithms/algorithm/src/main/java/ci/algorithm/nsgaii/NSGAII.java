package ci.algorithm.nsgaii;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ci.Problem;
import ci.Solution;
import ci.algorithm.de.DE;
import ci.problem.multiobjective.SCH;

public class NSGAII extends DE{


	public NSGAII(Problem problem, long maxFunctionEvaluations, int populationSize,double Cr,double F) {
		super(problem, maxFunctionEvaluations, populationSize,Cr,F);
		
	}

	/*
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
		List<Solution> pop=new ArrayList<Solution>();
		List<Solution> temp=new ArrayList<Solution>();
		List<Integer>[] S=new List[2*populationSize];
		List<Integer>[] F = new List[2*populationSize];
		int[] n=new int[2*populationSize];
		
		for(int i=0;i<populationSize;i++) {
			temp.add(population.get(i));
		}
		for(int i=0;i<populationSize;i++) {
			temp.add(offsprings.get(i));
		}
		
		for(int p=0;p<2*populationSize;p++) {
			F[p]=new ArrayList<>();
			S[p]=new ArrayList<Integer>();
			n[p]=0;
			for(int q=0;q<2*populationSize;q++) {
				if(isDominate(temp.get(p),temp.get(q))) {
					S[p].add(q);
				}
				else if(isDominate(temp.get(q),temp.get(p))) {
					n[p]++;
				}
			}
			
			if(n[p]==0) {
				F[0].add(p);
			}
		}
		
		int i=0;
		while(!F[i].isEmpty()) {
			for(int p:F[i]) {
				for(int q:S[p]) {
					n[q]--;
					if(n[q]==0) {
						F[i+1].add(q);
					}
				}
			}
			i++;
		}

		int flag=0;
		for(i=0;i<F.length;i++) {
			if(pop.size()+F[i].size()<=populationSize) {
				for(int p:F[i]) {
					pop.add(temp.get(p));
				}	
			}
			else {
				List<Integer> lis=F[i];
				double[] distance=new double[2*populationSize];
				for(int k=0;k<2*populationSize;k++) {
					distance[k]=0.0;
				}
				for(int l=0;l<this.problem.getNumberOfObjectives();l++) {
					double fMax=10000;
					double fMin=0;
					for(int j=lis.size();j>0;j--) {
						for(int k=0;k<j-1;k++) {
							if(temp.get(lis.get(k)).getObjective(l)>temp.get(lis.get(k+1)).getObjective(l)) {
								int te=lis.get(j);
								lis.set(j, lis.get(k));
								lis.set(k, te);
							}
						}
					}
					distance[lis.get(0)]=99999;
					distance[lis.get(lis.size()-1)]=99999;
					for(int k=1;k<lis.size()-1;k++) {
						distance[lis.get(k)]+=(temp.get(lis.get(k+1)).getObjective(l)-temp.get(lis.get(k-1)).getObjective(l))/(fMax-fMin);
					}
				}
				
			}
			
			
		}
		
		return pop;
	}
	
	
	
	
	private boolean isDominate(Solution solution, Solution solution2) {
		// TODO Auto-generated method stub
		int flag=0;
		for(int i=0;i<this.problem.getNumberOfObjectives();i++) {
			if(solution.getObjective(i)<=solution2.getObjective(i)) {
				if(solution.getObjective(i)<solution2.getObjective(i)) {
					flag=1;
				}
			}
			else {
				flag=0;
				break;
			}
		}
		if(flag==1) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void main(String[] args) {
        Problem problem =new SCH(1);

        NSGAII nsgaii = new NSGAII(problem,  3000, 30);
        nsgaii.run();
        for(Solution p:nsgaii.population)
        	System.out.println(p.getObjective(0)+"\t"+p.getObjective(1));
    }*/
}
