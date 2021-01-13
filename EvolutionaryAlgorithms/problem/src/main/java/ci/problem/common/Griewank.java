package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Griewank extends Problem {
    /**
     * x*={0,0...0};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Griewank(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Griewank");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-600.0);
            upperLimit.add(600.0);
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
            sum1 += value * value;
        }

        double sum2=1.0;
        for (int i = 0; i < numberOfVariables; i++) {
            sum2*= Math.cos(x[i]/Math.pow(i+1,0.5));
        }

        double sum=1.0/4000*sum1-sum2+1;

        solution.setObjectives(0,sum);
    }
}
