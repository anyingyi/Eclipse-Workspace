package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Rastrigin extends Problem {

    /**
     * x*={0,0...0};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Rastrigin(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Rastrigin");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            //lowerLimit.add(-5.0);
            //upperLimit.add(5.0);
            lowerLimit.add(-5.12);
            upperLimit.add(5.12);
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

        double sum = 0.0 ;
        for (int i = 0; i < numberOfVariables; i++) {
            sum+=(Math.pow(x[i],2)-10*Math.cos(2*Math.PI*x[i]));
        }
        sum+=10*numberOfVariables;

        solution.setObjectives(0,sum);
    }
}
