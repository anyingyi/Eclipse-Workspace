package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Rosenbrock extends Problem {
    /**
     * x*={1,1...1};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Rosenbrock(int numberOfVariables){
        setNumberOfVariables(numberOfVariables);
        setName("Rosenbrock");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            //lowerLimit.add(-100.0);
            //upperLimit.add(100.0);
            lowerLimit.add(-5.0);
            upperLimit.add(5.0);
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
        for (int i = 0; i < numberOfVariables-1; i++) {
            sum+=(100.0*Math.pow(x[i+1]-x[i]*x[i],2)+Math.pow(1.0-x[i],2));
        }

        solution.setObjectives(0,sum);
    }
}
