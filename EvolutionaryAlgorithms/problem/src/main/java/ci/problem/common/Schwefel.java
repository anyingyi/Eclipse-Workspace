package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Schwefel extends Problem {

    /** Some problem with!!!
     * x*={420.9687,420.9687,...,420.9687};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Schwefel(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Schwefel");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-500.0);
            upperLimit.add(500.0);
        }
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }


    @Override
    public void evaluate(Solution solution) {
        int numberOfVariables = getNumberOfVariables() ;

        double[] x = new double[numberOfVariables] ;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double sum1 = 0.0 ;
        for (int i = 0; i < numberOfVariables; i++) {
            double value = x[i];
            sum1 += value * Math.sin(Math.pow(Math.abs(value),0.5));
        }

        double sum=418.9829*numberOfVariables-sum1;

        solution.setObjectives(0,sum);
    }
}
