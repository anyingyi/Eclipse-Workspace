package ci.algorithm.abc;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.common.Sphere;

import java.util.ArrayList;
import java.util.List;

public class ABC extends EvolutionaryAlgorithm {

    private int limit;

    private List<Integer> trial;
    private List<Double> fits;
    private List<Double> p;

    public ABC(Problem problem, long maxFES, int popSize, int limit) {
        super(problem, maxFES, popSize);
        this.limit = limit;
    }

    @Override
    public void run() {
        int ifes;
        trial = new ArrayList<>(populationSize);
        fits = new ArrayList<>(populationSize);
        p = new ArrayList<>(populationSize);
        this.initialPopulation();
        for (int i = 0; i < populationSize; i++) {
            trial.add(0);
            fits.add(CalculateFitness(this.population.get(i).getObjective(0)));
            p.add(0.0);
        }

        ifes = this.populationSize;
        bestSolution = population.get(0).copy();
        MemorizeBestSource();

        listMinf = new ArrayList<>(this.dimensionSize + 1);
        for (int i = 0; i <= dimensionSize; i++) {
            listMinf.add(Double.MAX_VALUE);
        }

        listMinf.set(0, bestSolution.getObjective(0));

        int iter = 0;
        int gen = (int) (this.maxFunctionEvaluations/ this.populationSize / 2 /this.dimensionSize);

        while (ifes < maxFunctionEvaluations) {
            EmployedBees();
            ifes = ifes + this.populationSize;
            CalculateProbabilities();
            OnlookerBees();
            ifes = ifes + this.populationSize;
            ifes = ifes + ScoutBees();
            MemorizeBestSource();
            iter++;
            if (iter % gen ==0){
                listMinf.set(iter / gen, bestSolution.getObjective(0));
                System.out.println(String.format("During %d iteration, and obtain the best fitness is %e", iter/gen,
                        bestSolution.getObjective(0)));
            }
        }
        listMinf.set(listMinf.size() - 1, bestSolution.getObjective(0));
    }

    protected double CalculateFitness(double objValue) {
        double result = 0;
        if (objValue >= 0) {
            result = 1 / (objValue + 1);
        } else {
            result = 1 + Math.abs(objValue);
        }
        return result;
    }


    protected void EmployedBees() {
        int i, j, k;
        Solution v0;
        /*Employed Bee Phase*/
        for (i = 0; i < populationSize; i++) {

            double r = this.randGenerator.nextDouble();
            j = (int) (r * dimensionSize);

            k = this.randGenerator.nextInt(populationSize);
            while (k == i) {
                k = this.randGenerator.nextInt(populationSize);
            }
            v0 = this.population.get(i).copy();


            r = this.randGenerator.nextDouble();
            double value =
                    v0.getVariableValue(j) + (v0.getVariableValue(j) - this.population.get(k).getVariableValue(j)) * (r - 0.5) * 2;

            v0.setVariableValue(j, value);
            if (this.problem.isRepairSolution()) {
                v0.repairSolutionVariableValue(j);
            }
            this.problem.evaluate(v0);
            double v0Fitness = CalculateFitness(v0.getObjective(0));

            /*a greedy selection is applied between the current solution i and its mutant*/
            if (v0Fitness > fits.get(i)) {
                trial.set(i, 0);
                this.population.get(i).setVariableValue(j, v0.getVariableValue(j));
                this.population.get(i).setObjectives(0, v0.getObjective(0));
                fits.set(i,v0Fitness);
            } else {   /*if the solution i can not be improved, increase its trial counter*/
                trial.set(i, trial.get(i) + 1);
            }
        }

        /*end of employed bee phase*/
    }

    void CalculateProbabilities() {
        int i;
        double sumfit=0.0;

        for (i = 1; i < populationSize; i++) {

            sumfit=sumfit+fits.get(i);
        }

        for (i = 0; i < populationSize; i++) {
            p.set(i, fits.get(i)/sumfit);
        }
    }

    protected void OnlookerBees() {

        int i, j, t, k;
        Solution v0;
        i = 0;
        t = 0;
        /*onlooker Bee Phase*/
        while (t < populationSize) {

            double r = this.randGenerator.nextDouble();
            if (r < p.get(i)) /*choose a food source depending on its probability to be chosen*/ {
                t++;
                /*The parameter to be changed is determined randomly*/
                j = this.randGenerator.nextInt(dimensionSize);

                k = this.randGenerator.nextInt(populationSize);

                while (k == i) {
                    k = this.randGenerator.nextInt(populationSize);
                }

                v0 = this.population.get(i).copy();

                r = this.randGenerator.nextDouble();
                double value =
                        v0.getVariableValue(j) + (v0.getVariableValue(j) - this.population.get(k).getVariableValue(j)) * (r - 0.5) * 2;

                v0.setVariableValue(j, value);
                if (this.problem.isRepairSolution()) {
                    v0.repairSolutionVariableValue(j);
                }
                this.problem.evaluate(v0);
                double v0Fitness = CalculateFitness(v0.getObjective(0));

                /*a greedy selection is applied between the current solution i and its mutant*/
                if (v0Fitness > fits.get(i)) {
                    trial.set(i, 0);
                    this.population.get(i).setVariableValue(j, v0.getVariableValue(j));
                    this.population.get(i).setObjectives(0, v0.getObjective(0));
                    fits.set(i,v0Fitness);
                } else {   /*if the solution i can not be improved, increase its trial counter*/
                    trial.set(i, trial.get(i) + 1);
                }
            } /*if */
            i++;
            if (i == populationSize)
                i = 0;
        }/*while*/

        /*end of onlooker bee phase     */
    }

    protected int ScoutBees() {
        int maxtrialindex, i;
        maxtrialindex = 0;
        for (i = 1; i < populationSize; i++) {
            if (trial.get(i) > trial.get(maxtrialindex)) {
                maxtrialindex = i;
            }
        }
        if (trial.get(maxtrialindex) >= limit) {
            for (int j = 0; j < dimensionSize; j++) {
                this.population.get(maxtrialindex).setVariableValue(j,
                        randGenerator.nextDouble() * (this.problem.getUpperBound(j) - this.problem.getLowerBound(j)));
            }
            this.problem.evaluate(this.population.get(maxtrialindex));
            fits.set(maxtrialindex,CalculateFitness(this.population.get(maxtrialindex).getObjective(0)));
            return 1;
        }
        return 0;
    }


    protected void MemorizeBestSource() {
        int i, j = -1;

        for (i = 0; i < populationSize; i++) {
            if (this.population.get(i).getObjective(0) < bestSolution.getObjective(0)) {
                j = i;
            }
        }
        if (j > -1) {
            bestSolution = this.population.get(j).copy();
        }
    }

    public static void main(String[] args) {
        Problem problem =new Sphere(30);

        ABC abc = new ABC(problem, 30 * 3000, 25, 100);
        abc.run();
        System.out.println(abc.getBestSolution().getObjective(0)+"          "+abc.getListMinf().get(abc.getListMinf().size()-1));
    }

}
