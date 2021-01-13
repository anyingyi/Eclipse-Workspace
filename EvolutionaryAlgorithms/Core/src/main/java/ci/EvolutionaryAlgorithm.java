package ci;

import util.NormalizeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class EvolutionaryAlgorithm {
    protected int populationSize;
    protected int dimensionSize;
    protected long maxFunctionEvaluations;
    protected Problem problem;
    protected List<Solution> population;
    protected Random randGenerator;
    protected List<Double> listMinf;
    protected Solution bestSolution;

    public EvolutionaryAlgorithm(Problem problem, long maxFunctionEvaluations, int populationSize) {
        this.populationSize = populationSize;
        this.maxFunctionEvaluations = maxFunctionEvaluations;
        this.problem = problem;
        this.dimensionSize = problem.getNumberOfVariables();
        randGenerator = new Random(System.currentTimeMillis());
    }

    protected List<Integer> randPerm(int n) {
        List<Integer> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            res.add(i);
        }
        for (int i = 0; i < n; i++) {
            int j = (int) (randGenerator.nextDouble() * Integer.MAX_VALUE);
            j = j % (n - i) + i;
            int t = res.get(j);
            res.set(j, res.get(i));
            res.set(i, t);
        }
        return res;
    }

    protected void sortPopulation() {
        Collections.sort(population, Solution.fitnessComparator);
    }

    protected void initialPopulation() {
        population = new ArrayList<Solution>(this.populationSize);
        for (int i = 0; i < populationSize; i++) {
            Solution s = new Solution(this.problem);
            problem.evaluate(s);
            population.add(s);
        }
    }

    protected void initChaotic(){

		double value;

		for(int i=0;i<populationSize;i++){

			Solution temp=population.get(i);
			double ch=randGenerator.nextDouble();

			for(int j=0;j<dimensionSize;j++){
				ch=Math.sin(Math.PI*ch);
				value=temp.getLowerBound(j)+ch*(temp.getUpperBound(j)-temp.getLowerBound(j));
				temp.setVariableValue(j,value);
			}
			problem.evaluate(temp);
		}
	}

    protected void initNonlinearSimplex()
    {
        List<Solution> simplexPop=new ArrayList<Solution>(2*this.populationSize);
        for(Solution i:population){
            simplexPop.add(i.copy());
        }


        int n=5;
        double alpha=1,gamma=1.5,beta=0.5;

        for(int q=0;q<populationSize;q++) {

            Solution xBest, xWorst, xCent, temp1, temp2;
            xCent = population.get(0).copy();
            temp1 = xCent.copy();
            temp2 = xCent.copy();

            double value = 0;
            int[] ids = randomNum(populationSize, n);
            List<Solution> randPolulation = new ArrayList<>();
            for (int i : ids) {
                randPolulation.add(population.get(i));
            }
            Collections.sort(randPolulation,Solution.fitnessComparator);
            xBest = randPolulation.get(0).copy();
            xWorst = randPolulation.get(n - 1).copy();

            //calculate central point
            for (int i = 0; i < dimensionSize; i++) {
                for (int j = 0; j < n - 1; j++) {
                    value += randPolulation.get(j).getVariableValue(i);
                }
                value -= randPolulation.get(n - 1).getVariableValue(i);
                value /= (n + 1);
                xCent.setVariableValue(i, value);
            }
            problem.evaluate(xCent);

            //reflection
            for (int j = 0; j < dimensionSize; j++) {
                value = xCent.getVariableValue(j) + alpha * (xCent.getVariableValue(j) - xWorst.getVariableValue(j));
                temp1.setVariableValue(j, value);
            }
            problem.evaluate(temp1);


            if (temp1.getObjective(0) <= xBest.getObjective(0)) {    //expansion
                for (int j = 0; j < dimensionSize; j++) {
                    value = xCent.getVariableValue(j) + gamma * (temp1.getVariableValue(j) - xCent.getVariableValue(j));
                    temp2.setVariableValue(j, value);
                }
                problem.evaluate(temp2);
            } else if (temp1.getObjective(0) <= xWorst.getObjective(0)) {  //contraction
                for (int j = 0; j < dimensionSize; j++) {
                    value = xCent.getVariableValue(j) + beta * (-xCent.getVariableValue(j) + xWorst.getVariableValue(j));
                    temp2.setVariableValue(j, value);
                }
                problem.evaluate(temp2);
            } else {                                                       //reduction
                temp2=new Solution(problem);
                problem.evaluate(temp2);
            }
            simplexPop.add(temp2.copy());
        }

        Collections.sort(simplexPop,Solution.fitnessComparator);

        for(int i=0;i<populationSize;i++){
            population.set(i,simplexPop.get(i).copy());
        }

    }
    
    protected void initEqualDivided() {

    	double part=(problem.getUpperBound(0)-problem.getLowerBound(0))/dimensionSize;
    	for (int i = 0; i < populationSize; i++) {
    		for(int j=0;j<dimensionSize;j++) {
    			double lowBound=problem.getLowerBound(0)+part*j;
    			double upBound=lowBound+part;
    			double value=lowBound+randGenerator.nextDouble()*(upBound-lowBound);
    			population.get(i).setVariableValue(j, value);
    		}
    		problem.evaluate(population.get(i));
    	}
    }

    protected void initLHS() {
        int[][] latinHypercube = new int[populationSize][problem.getNumberOfVariables()];
        for (int dim = 0; dim < problem.getNumberOfVariables(); dim++) {
            List<Integer> permutation = getPermutation(populationSize);
            for (int v = 0; v < populationSize; v++) {
                latinHypercube[v][dim] = permutation.get(v);
            }
        }

        List<Solution> solutionList = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Solution newSolution =population.get(i);

            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                // newSolution.setVariable(j, (double)latinHypercube[i][j]/numberOfSolutionsToCreate);
                newSolution.setVariableValue(
                        j,
                        NormalizeUtils.normalize(
                                latinHypercube[i][j],
                                problem.getLowerBound(j),
                                problem.getUpperBound(j),
                                0,
                                populationSize));
            }

            problem.evaluate(newSolution);
        }
    }


    public void initQuadraticInterpolation(){
        int[] ids=new int[2];
        Solution a,b,c,temp;
        List<Solution> quaPop=new ArrayList<Solution>(2*this.populationSize);
        sortPopulation();
        for(Solution i:population){
            quaPop.add(i.copy());
        }

        for(int i=0;i<populationSize;i++){
            ids=randomNum(populationSize,2);
            a=population.get(0).copy();
            b=population.get(ids[0]).copy();
            c=population.get(ids[1]).copy();
            temp=population.get(i).copy();
            for(int j=0;j<dimensionSize;j++){
                double a_val=a.getVariableValue(j);
                double b_val=b.getVariableValue(j);
                double c_val=c.getVariableValue(j);
                double t1=(b_val*b_val-c_val*c_val)*a.getObjective(0)+(c_val*c_val-a_val*a_val)*b.getObjective(0)
						+(a_val*a_val-b_val*b_val)*c.getObjective(0);
                double t2=(b_val-c_val)*a.getObjective(0)
						+(c_val-a_val)*b.getObjective(0) +(a_val-b_val)*c.getObjective(0);
                double value=0.5*(t1)/(t2);
                temp.setVariableValue(j,value);
            }
            this.problem.evaluate(temp);
            if(Double.isNaN(temp.getObjective(0)))
                quaPop.add(temp.copy());
        }

        Collections.sort(quaPop,Solution.fitnessComparator);

        for(int i=0;i<populationSize;i++){
            population.set(i,quaPop.get(i).copy());
        }
    }
    
    public void initPopOppoBased() {
    	
    	List<Double> lowerLimit=problem.getLowerLimit();
    	List<Double> upperLimit=problem.getUpperLimit();
		double xMin;
		double xMax;
		List<Double> variable;
		Solution[] oPop=new Solution[populationSize*2];
		
		for(int i=0;i<populationSize;i++) {
			Solution temp=population.get(i).copy();
            variable=population.get(i).getVariable();
			for(int j=0;j<dimensionSize;j++) {
                xMin=lowerLimit.get(j);
                xMax=upperLimit.get(j);

				double value=xMin+xMax-variable.get(j);
				temp.setVariableValue(j, value);
			}
			problem.evaluate(temp);
			oPop[i]=temp.copy();
		}
		
		for(int i=populationSize;i<populationSize*2;i++) {
			oPop[i]=population.get(i-populationSize);
		}
		Arrays.sort(oPop, Solution.fitnessComparator);
		for(int i=0;i<populationSize;i++) {
			population.set(i, oPop[i]);
		}
	}

    public Solution getBestSolution() {
        return bestSolution;
    }

    public List<Double> getListMinf() {
        return listMinf;
    }

    public abstract void run();
    
    public int[] randomNum(int popSize, int randVectorNum) {
		// TODO Auto-generated method stub
		int[] ids=new int[randVectorNum];
		List<Integer> p=new ArrayList<Integer>();
		for(int i=0;i<popSize;i++) {
			p.add(i);
		}
		Collections.shuffle(p);
		for(int i=0;i<randVectorNum;i++) {
			int temp=randGenerator.nextInt(p.size());
			ids[i]=p.remove(temp);
		}
		
		return ids;
	}

    private List<Integer> getPermutation(int permutationLength) {
        List<Integer> randomSequence = new ArrayList<>(permutationLength);

        for (int j = 0; j < permutationLength; j++) {
            randomSequence.add(j);
        }

        java.util.Collections.shuffle(randomSequence);

        return randomSequence;
    }
}
