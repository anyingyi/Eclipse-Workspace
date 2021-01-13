package ci.algorithm.cs;

import java.util.ArrayList;

import ci.Problem;
import ci.Solution;
import ci.problem.CEC2005;
import ci.problem.common.Sphere;

public class OCS extends CS_me{
	double Jr;
	public OCS(Problem problem, long maxFunctionEvaluations, int populationSize, double Pa, double beta,double Jr) {
		super(problem, maxFunctionEvaluations, populationSize, Pa, beta);
		this.Jr=Jr;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		this.initPopOppoBased();
		
		
		newNest=new ArrayList<>();
		bestSolution=population.get(0).copy();
		for(Solution i:population) {
			newNest.add(i.copy());
			if(i.getObjective(0)<bestSolution.getObjective(0))
				bestSolution=i.copy();
		}

		
		for(int q=0;q<maxFunctionEvaluations;q++) {
				
				//global random walk
				levyRandomWalk();
				
				//selection
				selection();
				
				//local random walk
				LocalRandomWalk();
				
				//selection
				selection();
				
				if(randGenerator.nextDouble()<Jr) {
					this.initPopOppoBased();
				}
		}
	}

	
	public static void main(String[] args) {
        //Problem problem =new Sphere(30);
		Problem problem =new CEC2005(3,30);

        OCS cs = new OCS(problem, 5000, 30,0.25,1.5,0.3);
        cs.run();
        System.out.println(cs.getBestSolution());
    }
}
