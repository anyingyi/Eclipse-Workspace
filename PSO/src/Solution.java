

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class Solution {
    protected Problem problem;
    private double objective;
    private List<Double> particle;
    private List<Double> v;
    private List<Double> vmax;
    private List<Double> pBest;
    private double pBestFit;
    private static Random randGenerator = new Random();


    /** Constructor */
    public Solution(Problem problem) {
        this.problem=problem;
        particle = new ArrayList<>(problem.getNumberOfVariables()) ;
        v=new ArrayList<>(problem.getNumberOfVariables());
        pBest=new ArrayList<>(problem.getNumberOfVariables());
        vmax=new ArrayList<>(problem.getNumberOfVariables());
        for(int i=0;i<problem.getNumberOfVariables();i++) {
            double value=getLowerBound(i)+randGenerator.nextDouble()*(getUpperBound(i)-getLowerBound(i));
            particle.add(value);
            pBest.add(value);
            value=getLowerBound(i)+randGenerator.nextDouble()*(getUpperBound(i)-getLowerBound(i));
            v.add(0.5*value);
            vmax.add(0.25*(getUpperBound(i)-getLowerBound(i)));
        }
        objective=Double.MAX_VALUE;
        pBestFit=objective;
    }

    /** Copy constructor */
    public Solution(Solution solution) {
        this.problem=solution.problem;
        particle = new ArrayList<>(solution.particle);
        pBest=new ArrayList<>(solution.pBest);
        v=new ArrayList<>(solution.v);
        vmax=new ArrayList<>(solution.vmax);
        this.objective=solution.objective;
        this.pBestFit=solution.pBestFit;
    }


    public Double getUpperBound(int index) {
        return problem.getUpperBound(index);
    }


    public Double getLowerBound(int index) {
        return problem.getLowerBound(index) ;
    }
    public Solution copy() {
        return new Solution(this);
    }


    public void setVariableValue(int index, double value) {
        particle.set(index, value);
    }

    public double getVariableValue(int index){
        return particle.get(index);
    }

    public void setVelValue(int index, double value){
        v.set(index,value);
    }
    public double getVelValue(int index){
        return v.get(index);
    }

    public void setPBestValue(int index,double value){
        pBest.set(index,value);
    }
    public double getPBestValue(int index){
        return pBest.get(index);
    }

    public void setPBest(){
        for(int i=0;i<problem.getNumberOfVariables();i++){
            pBest.set(i,getVariableValue(i));
        }
        setPBestObjective(getObjective());
    }

    public int getNumberOfVariables() {
        return particle.size();
    }

    public void setObjective(double value) {
        objective = value ;
    }

    public double getObjective() {
        return objective;
    }

    public void setPBestObjective(double value){
        pBestFit=value;
    }

    public double getPBestObjective(){
        return pBestFit;
    }

    private double getVMax(int index){
        return vmax.get(index);
    }

    public void repairSolutionVariableValue(int jth){
        if (jth>=0 && jth<problem.getNumberOfVariables())
        {
            if(getVariableValue(jth)>getUpperBound(jth))
                setVariableValue(jth, getUpperBound(jth)) ;
            if (getVariableValue(jth)<getLowerBound(jth))
                setVariableValue(jth, getLowerBound(jth));
        }
    }
    public void repairSolutionVariableValue(){
         for (int i = 0 ; i < problem.getNumberOfVariables(); i++) {
            repairSolutionVariableValue(i);
        }
    }
    public void repairVelValue(int jth){
        if (jth>=0 && jth<problem.getNumberOfVariables())
        {
            if(getVariableValue(jth)>getVMax(jth))
                setVariableValue(jth, getVMax(jth)) ;
            if (getVariableValue(jth)<-getVMax(jth))
                setVariableValue(jth, -getVMax(jth));
        }
    }
}
