package main.java;

import java.util.ArrayList;
public class Sphere extends Problem{


    /** Constructor */
    public Sphere() {
        super(); ;
    }

    /** Constructor */
    public Sphere(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Sphere");

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-5.12);
            upperLimit.add(5.12);
        }

        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }



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

        solution.setObjective(sum);
    }

}
