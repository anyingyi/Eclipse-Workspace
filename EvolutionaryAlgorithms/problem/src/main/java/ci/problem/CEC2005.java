package ci.problem;

import ci.Problem;
import ci.Solution;
import ci.problem.cec2005.Benchmark;
import ci.problem.cec2005.TestFunc;

import java.util.ArrayList;
import java.util.List;

public class CEC2005 extends Problem {
    TestFunc testFunc;
    public CEC2005(int problemID, int numberOfVariables){

        setNumberOfObjectives(1);
        setNumberOfVariables(numberOfVariables);
        setName("CEC2005");
        Benchmark cec2005Factory =new Benchmark();
        testFunc=cec2005Factory.testFunctionFactory(problemID,numberOfVariables);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        double ulimit = 0;
        double llimit = 0;

        switch (problemID) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 14:
                ulimit = 100;
                llimit = -100;
                break;
            case 7:
            case 25:
                ulimit = 600;
                llimit = 0;
                break;
            case 8:
                ulimit = 32;
                llimit = -32;
                break;
            case 9:
            case 10:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
                ulimit = 5;
                llimit = -5;
                break;
            case 11:
                ulimit = 0.5;
                llimit = -0.5;
                break;
            case 12:
                ulimit = Math.PI;
                llimit = -Math.PI;
                break;
            case 13:
                ulimit = 3;
                llimit = 1;
                break;
            default:
                System.err.println("Invalid problem value");
                System.exit(-1);
        }
        for (int i = 0; i < this.getNumberOfVariables(); i++) {
            lowerLimit.add(llimit);
            upperLimit.add(ulimit);
        }
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);

        if (problemID==7 || problemID==25) {
            setRepairSolution(false);
        }else{
            setRepairSolution(true);
        }

    }

    /** Evaluate() method */
    @Override
    public void evaluate(Solution solution) {
        int numberOfVariables = getNumberOfVariables() ;

        double[] x = new double[numberOfVariables] ;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }
        double result;
        result = testFunc.f(x);
        solution.setObjectives(0, result);
    }

}
