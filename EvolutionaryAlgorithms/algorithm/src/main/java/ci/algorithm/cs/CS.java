package ci.algorithm.cs;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.CEC2005;
import ci.problem.common.Sphere;

import java.util.ArrayList;
import java.util.List;

public class CS extends EvolutionaryAlgorithm {
    private double pa;
    private double beta;
    private List<Solution> newNest;

    public CS(Problem problem, long maxFES, int popSize,double pa, double beta){
        super(problem,maxFES,popSize);
        this.pa=pa;
        this.beta=beta;

    }
    protected void LevyRandomWalk(double sigma, double beta){
        double u, v, step, newx, currentx, bestx;
        int i, j;
        for (i = 0; i < this.populationSize; i++) {
            for (j = 0; j < this.dimensionSize; j++) {
                u = this.randGenerator.nextGaussian() * sigma;
                v = this.randGenerator.nextGaussian();
                step = u / Math.pow(Math.abs(v), 1.0 / beta);
                currentx = this.population.get(i).getVariableValue(j);
                bestx = this.bestSolution.getVariableValue(j);
                newx = currentx + 0.01 * step * this.randGenerator.nextGaussian() * (currentx - bestx);
                newNest.get(i).setVariableValue(j, newx);
                if (this.problem.isRepairSolution()) {
                    newNest.get(i).repairSolutionVariableValue(j);
                }
            }
            this.problem.evaluate(newNest.get(i));
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
                if (this.randGenerator.nextDouble() > this.pa) {
                    r1value = population.get(r1.get(i)).getVariableValue(j);
                    r2value = population.get(r2.get(i)).getVariableValue(j);
                    nvalue = cvalue + rvalue * (r1value - r2value);
                } else {
                    nvalue = cvalue;
                }
                newNest.get(i).setVariableValue(j, nvalue);
                if (this.problem.isRepairSolution()) {
                    newNest.get(i).repairSolutionVariableValue(j);
                }
            }
            this.problem.evaluate(newNest.get(i));
        }
    }

    protected void selectPopulation() {
        for (int i = 0; i < populationSize; i++) {
            if (newNest.get(i).getObjective(0) < population.get(i).getObjective(0)) {
                population.set(i,newNest.get(i).copy());
            }
        }
    }



    @Override
    public void run() {
        int ifes;
        this.initialPopulation();

        newNest = new ArrayList<>(this.populationSize);
        for (int i = 0; i < this.populationSize; i++) {
            newNest.add(population.get(i).copy());
        }
        ifes = this.populationSize;

        listMinf = new ArrayList<>(this.dimensionSize + 1);
        for (int i = 0; i <= dimensionSize; i++) {
            listMinf.add(Double.MAX_VALUE);
        }
        this.sortPopulation();
        listMinf.set(0, population.get(0).getObjective(0));
        bestSolution = population.get(0).copy();
        int iter = 0;
        int gen = (int) (this.maxFunctionEvaluations/ this.populationSize / 2 /this.dimensionSize);
        double dSigmaValue =
                Math.pow((Gamma(1 + beta) * Math.sin(Math.PI * beta / 2.0)) / (Gamma((1 + beta) / 2.0) * beta * Math.pow(2,
                        (beta - 1) / 2)),
                        1.0 / beta);

        while (ifes < maxFunctionEvaluations) {
            LevyRandomWalk(dSigmaValue,beta);
            ifes = ifes + populationSize;
            selectPopulation();
            LocalRandomWalk();
            ifes = ifes + populationSize;
            selectPopulation();
            sortPopulation();
            if (bestSolution.getObjective(0) > population.get(0).getObjective(0)) {
                bestSolution = population.get(0).copy();
                //bestSolution.copy(population.get(0));
            }
            iter++;
            if (iter % gen ==0){
                //listMinf.set(iter / gen, bestSolution.getObjective(0));
                //System.out.println(String.format("During %d iteration, and obtain the best fitness is %e", iter/gen,
                //        bestSolution.getObjective(0)));
            }

        }
        listMinf.set(listMinf.size()-1,bestSolution.getObjective(0));
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
        Problem problem =new Sphere(30);
        //Problem problem=new CEC2005(3,30);
        CS cs = new CS(problem, 30 * 10000, 30, 0.25, 1.5);
        cs.run();
        System.out.println(cs.getBestSolution().getObjective(0)+"          "+cs.getListMinf().get(cs.getListMinf().size()-1));
    }

}
