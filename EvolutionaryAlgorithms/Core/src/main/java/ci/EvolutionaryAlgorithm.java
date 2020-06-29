package ci;

import java.util.ArrayList;
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
}
