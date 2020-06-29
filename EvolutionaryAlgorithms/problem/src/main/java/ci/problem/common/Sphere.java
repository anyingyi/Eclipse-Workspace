package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Sphere extends Problem {
    public Sphere(){
        super();
    }

    /** Constructor */
    public Sphere(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Sphere");
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

        double sum = 0.0 ;
        for (int var = 0; var < numberOfVariables; var++) {
            double value = x[var];
            sum += value * value;
        }

        solution.setObjectives(0,sum);
    }
}
