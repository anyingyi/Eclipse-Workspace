package ci.problem.common;

import ci.Problem;
import ci.Solution;

import java.util.ArrayList;

public class PenalizedFunc1 extends Problem {

    /**
     * x*={-1,-1,...,-1};
     * f*=0;
     *
     * @param numberOfVariables
     */
    public PenalizedFunc1(Integer numberOfVariables) {
        setNumberOfVariables(numberOfVariables);
        setName("PenalizedFunc1");
        setNumberOfObjectives(1);
        setRepairSolution(true);

        lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(-50.0);
            upperLimit.add(50.0);
        }
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }


    @Override
    public void evaluate(Solution solution) {
        int numberOfVariables = getNumberOfVariables() ;

        double[] x = new double[numberOfVariables] ;
        double[] y=new double[numberOfVariables];
        double[] u=new double[numberOfVariables];

        for (int i = 0; i < numberOfVariables; i++) {
            x[i] = solution.getVariableValue(i) ;
        }

        for(int i = 0; i < numberOfVariables; i++){
            y[i]=1+1.0/4*(x[i]+1);
        }

        for(int i = 0; i < numberOfVariables; i++){
            double a=10.0,k=100.0,m=4.0;
            if(x[i]>a){
                u[i]=k*Math.pow((x[i]-a),m);
            }
            else if(x[i]<-a){
                u[i]=k*Math.pow((-x[i]-a),m);
            }
            else{
                u[i]=0;
            }
        }

        double sum1 = 0.0,sum2=0.0 ;
        for (int i = 0; i < numberOfVariables-1; i++) {

            sum1 += Math.pow((y[i]-1),2)*(1+10*Math.pow(Math.sin(Math.PI*y[i+1]),2));
        }

        for(int i = 0; i < numberOfVariables; i++){
            sum2+=u[i];
        }

        double sum=Math.PI/numberOfVariables*(10*Math.pow(Math.sin(Math.PI*y[0]),2)
                +sum1
                +Math.pow(y[numberOfVariables-1]-1,2))
                +sum2;

        solution.setObjectives(0,sum);
    }
}
