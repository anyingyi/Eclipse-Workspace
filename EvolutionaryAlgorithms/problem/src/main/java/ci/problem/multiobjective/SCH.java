package ci.problem.multiobjective;

import java.util.ArrayList;

import ci.Problem;
import ci.Solution;

public class SCH extends Problem{
	SCH(){
		
	}
	
	public SCH(int numberOfVariables){
		setNumberOfVariables(numberOfVariables);
        setName("SCH");
        setNumberOfObjectives(2);
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
		// TODO Auto-generated method stub
		int numberOfVariables = getNumberOfVariables() ;

        double[] x = new double[numberOfVariables] ;

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        double sum = 0.0 ;
        double sum2=0.0;
        for (int var = 0; var < numberOfVariables; var++) {
            double value = x[var];
            sum += value * value;
            sum2+=(value-2) * (value-2);
        }

        solution.setObjectives(0,sum);
        solution.setObjectives(1, sum2);
	}

}
