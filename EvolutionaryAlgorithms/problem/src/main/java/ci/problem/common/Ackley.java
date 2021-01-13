package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Ackley extends Problem {
    /**
     * x*={0,0...0};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Ackley(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Ackley");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-32.0);
            upperLimit.add(32.0);
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

        double sum1 = 0.0,sum2=0.0 ;
        for (int i = 0; i < numberOfVariables; i++) {
            double value = x[i];
            sum1 += value * value;

            sum2+=Math.cos(2*Math.PI*value);
        }

        double sum=20+Math.exp(1)
                -20*Math.exp(-0.2*Math.pow(1.0/numberOfVariables*sum1,0.5))
                -Math.exp(1.0/numberOfVariables*sum2);

        solution.setObjectives(0,sum);
    }
}
