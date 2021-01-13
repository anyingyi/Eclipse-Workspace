package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Salomon extends Problem {

    /**
     * x*={0,0...0};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Salomon(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Salomon");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-100.0);
            upperLimit.add(100.0);
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

        double sum=-Math.cos(2*Math.PI*Math.pow(sum1,0.5))+0.1*Math.pow(sum1,0.5)+1;

        solution.setObjectives(0,sum);
    }
}
