package ci.algorithm.hillclimb;

import java.util.ArrayList;
import java.util.List;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.CEC2005;
import ci.problem.common.*;

public class HillClimb extends EvolutionaryAlgorithm{

	private double Pa;
	double beta;
	double alpha0=0.01;
	double threshold=1e-2;
	List<Solution> newNest;

	double dogma;

	public HillClimb(Problem problem, long maxFunctionEvaluations, int populationSize,double Pa,double beta) {
		super(problem, maxFunctionEvaluations, populationSize);
		this.Pa=Pa;
		this.beta=beta;
		this.dogma =doSigma(beta);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.initialPopulation();
		this.initChaotic();
		//this.initLHS();
		//this.initQuadraticInterpolation();
		//this.initNonlinearSimplex();
		//this.initPopOppoBased();


		newNest=new ArrayList<>();
		listMinf=new ArrayList<>();
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

			/*
			if(randGenerator.nextDouble()<0.2){
				int i=randGenerator.nextInt(populationSize);
				population.set(i,localSearch());
			}*/




			//selection
			selection();

			if(bestSolution.getObjective(0)<threshold)
			{
				//System.out.println(q);
				//break;
			}

			//save 30 points in listMinf to draw convergence graph
			if(q%(maxFunctionEvaluations/30)==0)
				this.listMinf.add(bestSolution.getObjective(0));
			//sortPopulation();
		}


	}

	protected Solution localSearch(){
		Solution s=bestSolution.copy();
		int maxIter=10;

		for(int i=0;i<maxIter;i++){
			Solution neighbor=s.neighbor(population);
			if(neighbor.getObjective(0)<s.getObjective(0)){
				s=neighbor;
			}
		}
		bestSolution=s;
		return bestSolution;
	}

	protected Solution localSearch(Solution solution){
		Solution s=solution.copy();
		int maxIter=100;

		for(int i=0;i<maxIter;i++){
			Solution neighbor=s.neighbor(population);
			if(neighbor.getObjective(0)<s.getObjective(0)){
				s=neighbor;
			}
		}
		return s;
	}

	protected void selection() {
		// TODO Auto-generated method stub
		for(int i=0;i<populationSize;i++) {
			if(newNest.get(i).getObjective(0)<population.get(i).getObjective(0)) {
				population.set(i, newNest.get(i).copy());
			}
			if(population.get(i).getObjective(0)<bestSolution.getObjective(0)) {
				bestSolution=population.get(i).copy();
			}
		}
	}

	protected void LocalRandomWalk() {
		double r1value, r2value, cvalue, nvalue, rvalue;
		List<Integer> r1 = this.randPerm(this.populationSize);
		List<Integer> r2 = this.randPerm(this.populationSize);
		rvalue = randGenerator.nextDouble();
		for (int i = 0; i < this.populationSize; i++) {
			for (int j = 0; j < this.dimensionSize; j++) {
				cvalue = this.population.get(i).getVariableValue(j);
				if (this.randGenerator.nextDouble() > this.Pa) {
					r1value = population.get(r1.get(i)).getVariableValue(j);
					r2value = population.get(r2.get(i)).getVariableValue(j);
					nvalue = cvalue + rvalue * (r1value - r2value);
				} else {
					nvalue = cvalue;
				}
				newNest.get(i).setVariableValue(j, nvalue);
				if (this.problem.isRepairSolution()) {
					//newNest.get(i).repairSolutionVariableValueOpposite(j);
					newNest.get(i).repairSolutionVariableValue(j);
				}
			}
			this.problem.evaluate(newNest.get(i));
		}
	}

	protected void localRandomWalk() {
		double newX,currentX,randX1,randX2;
		double rValue=this.randGenerator.nextDouble();
		for(int i=0;i<populationSize;i++) {
			int[] ids=this.randomNum(populationSize, 2);
			for(int j=0;j<dimensionSize;j++) {
				currentX=population.get(i).getVariableValue(j);
				if(randGenerator.nextDouble()>this.Pa) {
					randX1 = population.get(ids[0]).getVariableValue(j);
					randX2 = population.get(ids[1]).getVariableValue(j);
					newX = currentX + rValue * (randX1 - randX2);
				}
				else {
					newX = currentX;
				}
				newNest.get(i).setVariableValue(j, newX);
				if (this.problem.isRepairSolution()) {
					//newNest.get(i).repairSolutionVariableValueOpposite(j);
					newNest.get(i).repairSolutionVariableValue(j);

				}

			}
			this.problem.evaluate(newNest.get(i));
		}

	}

	protected void localRandomWalkDimension() {
		double newX,currentX,randX1,randX2;

		double rValue=this.randGenerator.nextDouble();
		for(int i=0;i<populationSize;i++) {
			int[] ids=this.randomNum(populationSize, 2);
			if(randGenerator.nextDouble()>Pa) {
				Solution temp;
				Solution temp2=newNest.get(i).copy();
				for(int j=0;j<dimensionSize;j++) {
					temp=temp2.copy();
					currentX=population.get(i).getVariableValue(j);
					randX1=population.get(ids[0]).getVariableValue(j);
					randX2=population.get(ids[1]).getVariableValue(j);
					newX=currentX+rValue*(randX1-randX2);
					temp.setVariableValue(j, newX);
					if (this.problem.isRepairSolution()) {
						temp.repairSolutionVariableValue(j);
					}
					this.problem.evaluate(temp);
					if(temp.getObjective(0)<newNest.get(i).getObjective(0))
					{
						newNest.get(i).setVariableValue(j, newX);
					}

				}
				this.problem.evaluate(newNest.get(i));
			}
		}

	}

	protected void levyRandomWalk() {
		// TODO Auto-generated method stub
		double newX,currentX,bestX;
		for(int i=0;i<populationSize;i++) {
			for(int j=0;j<dimensionSize;j++) {
				currentX=this.population.get(i).getVariableValue(j);
				bestX=this.bestSolution.getVariableValue(j);
				newX=currentX+alpha0*levyDistribution()* (currentX-bestX);
				newNest.get(i).setVariableValue(j, newX);
				if (this.problem.isRepairSolution()) {
					//newNest.get(i).repairSolutionVariableValueOpposite(j);
					newNest.get(i).repairSolutionVariableValue(j);

				}
			}
			this.problem.evaluate(newNest.get(i));
		}
	}


	/**
	 * levy distribution.
	 *
	 * @return a random number generate by levy distribution
	 */
	private double levyDistribution() {
		double x,y;
		x= dogma *randGenerator.nextGaussian();
		y=Math.pow(Math.abs(randGenerator.nextGaussian()), 1.0/beta);
		return x/y;
	}

	protected  double doSigma(double beta){
		double term1 = Gamma(beta+1)*Math.sin((Math.PI*beta)/2.0);
		double term2 = Gamma((beta+1)/2.0)*beta*Math.pow(2,(beta-1)/2);
		return Math.pow((term1/term2),(1.0/beta));
	}



	protected double Gamma(double x) {
		double k1_factr1 = 1.0;
		double[] c_space = new double[12];
		c_space[0] = Math.sqrt(2.0 * Math.PI);
		for (int k = 1; k < 12; k++) {
			c_space[k] = Math.exp(12 - k) * Math.pow(12 - k, k - 0.5) / k1_factr1;
			k1_factr1 *= -k;
		}
		double accm = c_space[0];
		for (int k = 1; k < 12; k++) {
			accm += c_space[k] / (x + k);
		}
		accm *= Math.exp(-(x + 12)) * Math.pow(x + 12, x + 0.5);
		return accm / x;
	}

	public static void main(String[] args) {
		Problem problem=new Rastrigin(100);
		//Problem problem=new Michaelwicz();
		//Problem problem =new CEC2005(3,30);



		HillClimb cs = new HillClimb(problem, 5000, 30,0.25,1.5);
		cs.run();
		System.out.println(cs.getBestSolution());
	}
}
