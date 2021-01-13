package ci.algorithm.cs;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.common.Sphere;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimplexMethod extends EvolutionaryAlgorithm {

    public SimplexMethod(Problem problem, long maxFunctionEvaluations, int populationSize) {
        super(problem, maxFunctionEvaluations, populationSize);
    }

    @Override
    public void run() {
        this.initialPopulation();

        List<Solution> simplexPop=new ArrayList<Solution>(2*this.populationSize);
        for(Solution i:population){
            simplexPop.add(i.copy());
        }


        int n=30;
        double alpha=1,gamma=1.5,beta=0.5;
        int[] ids = randomNum(populationSize, n);

        for(int q=0;q<maxFunctionEvaluations;q++) {

            Solution xBest, xWorst, xCent, temp1, temp2;
            xCent = population.get(0).copy();
            temp1 = xCent.copy();
            temp2 = xCent.copy();

            double value = 0;

            List<Solution> randPolulation = new ArrayList<>();
            for (int i : ids) {
                randPolulation.add(population.get(i));
            }
            Collections.sort(randPolulation,Solution.fitnessComparator);
            xBest = randPolulation.get(0).copy();
            xWorst = randPolulation.get(n - 1).copy();

            //calculate central point xCent
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

                if(temp2.getObjective(0)<temp1.getObjective(0)){
                    randPolulation.set(n-1, temp2.copy());
                }
                else{
                    randPolulation.set(n-1, temp1.copy());
                }
            } else if (temp1.getObjective(0) <= xWorst.getObjective(0)) {  //contraction
                for (int j = 0; j < dimensionSize; j++) {
                    value = xCent.getVariableValue(j) + beta * (-xCent.getVariableValue(j) + xWorst.getVariableValue(j));
                    temp2.setVariableValue(j, value);
                }
                problem.evaluate(temp2);

                if(temp2.getObjective(0)<xWorst.getObjective(0)){
                    randPolulation.set(n-1, temp2.copy());
                }
            } else {                                                       //reduction
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < dimensionSize; j++) {
                        value = xBest.getVariableValue(j) + 0.5 * (randPolulation.get(i).getVariableValue(j) - xBest.getVariableValue(j));
                        randPolulation.get(i).setVariableValue(j, value);
                    }
                    problem.evaluate(randPolulation.get(i));
                }

            }

            Collections.sort(randPolulation,Solution.fitnessComparator);

            bestSolution=randPolulation.get(0);
            System.out.print(bestSolution.getObjective(0)+"\t");

        }


    }

    public static void main(String[] args){
        Problem problem=new Sphere(30);
        SimplexMethod sm=new SimplexMethod(problem,5000,30);
        sm.run();
    }
}
