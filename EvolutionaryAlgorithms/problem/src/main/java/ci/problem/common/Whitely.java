package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Whitely extends Problem {

    /**
     * x*={1,1,...,1};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public Whitely(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("Whitely");
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
        double[][] y=new double[numberOfVariables][numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        for(int i = 0; i < numberOfVariables; i++){
            for(int j = 0; j < numberOfVariables; j++){
                y[i][j]=100*Math.pow(x[j]-Math.pow(x[i],2),2)+Math.pow(1-x[i],2);
            }
        }

        double sum = 0.0 ;
        for(int j = 0; j < numberOfVariables; j++) {
            for (int i = 0; i < numberOfVariables; i++) {

                sum += Math.pow(y[i][j],2)/4000.0-Math.cos(y[i][j])+1;
            }
        }

        solution.setObjectives(0,sum);
    }
}
