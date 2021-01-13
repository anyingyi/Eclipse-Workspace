package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Sphere extends Problem {
    public Sphere(){
        super();
    }

    /**
     * x*={0,0,...,0};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Sphere(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Sphere");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-5.0);//-100.0);
            upperLimit.add(5.0);//100.0);
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
            double value = x[i];
            sum += value * value;
        }

        solution.setObjectives(0,sum);
    }
}
