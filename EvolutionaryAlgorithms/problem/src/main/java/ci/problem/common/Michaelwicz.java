package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class Michaelwicz extends Problem {


    /**
     * x*={2.20319,1.57049};
     * f*=-1.8013034100985537;
     *
     * @param numberOfVariables
     */
    public Michaelwicz(){
        setName("Michaelwicz");
        setNumberOfObjectives(1);
        setNumberOfVariables(2);
        setRepairSolution(true);
        lowerLimit=new ArrayList<>();
        upperLimit=new ArrayList<>();

        for(int i=0;i<getNumberOfVariables();i++){
            lowerLimit.add(0.0);
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
        sum=-Math.sin(x[0])*Math.pow(Math.sin(x[0]*x[0]/Math.PI), 20)-Math.sin(x[1])*Math.pow(Math.sin(2*x[1]*x[1]/Math.PI), 20);

        solution.setObjectives(0,sum);

    }
}
