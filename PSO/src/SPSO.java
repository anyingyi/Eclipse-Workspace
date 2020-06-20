

//import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class SPSO {
    private int np;
    private int maxFunctionEvaluations;
    private double C1;
    private double C2;
    private double iteriaW;
    private List<Solution> population;
    private Solution gBest;
    private Random randGenerator;
    private Problem problem;

    public SPSO(Problem problem,int np, int maxFES,double c1, double c2, double iw){
        this.problem=problem;
        this.np=np;
        this.maxFunctionEvaluations=maxFES;
        this.C1=c1;
        this.C2=c2;
        this.iteriaW=iw;
        randGenerator=new Random();
    }

    public void initialStage(){
        population=new ArrayList<Solution>(this.np);
        double gBestFit=Double.MAX_VALUE;
        int mini=-1;
        for(int i=0;i<this.np;i++)
        {
            Solution s= new Solution(problem);
            problem.evaluate(s);
            double sFit=s.getObjective();
            population.add(s);
            s.setPBest();
            if (sFit<gBestFit)
            {
                gBestFit=sFit;
                mini=i;
            }
        }
        gBest=population.get(mini).copy();
    }

    public void run(){
        int iFes;
        double v_temp,pbest_temp,x_temp,r1,r2;
        initialStage();
        iFes=this.np;
        while(iFes<maxFunctionEvaluations){
            for(int i=0;i<np;i++){
                for(int j=0;j<problem.getNumberOfVariables();j++) {
                    v_temp=population.get(i).getVelValue(j);
                    pbest_temp=population.get(i).getPBestValue(j);
                    x_temp=population.get(i).getVariableValue(j);
                    r1=randGenerator.nextDouble();
                    r2=randGenerator.nextDouble();
                    v_temp=iteriaW*v_temp+C1*r1*(pbest_temp-x_temp)+C2*r2*(gBest.getVariableValue(j)-x_temp);
                    population.get(i).setVelValue(j,v_temp);
                    population.get(i).repairVelValue(j);
                    x_temp=x_temp+population.get(i).getVelValue(j);
                    population.get(i).setVariableValue(j,x_temp);
                    population.get(i).repairSolutionVariableValue(j);
                }
                //System.out.println("Before Evaluating: "+population.get(i).getObjective()+"    "+population.get(i)
                // .getPBestObjective());
                problem.evaluate(population.get(i));
                //System.out.println("After Evaluating: "+population.get(i).getObjective()+"    "+population.get(i)
                // .getPBestObjective());
                iFes++;
                if(population.get(i).getObjective()<population.get(i).getPBestObjective()){
                    population.get(i).setPBest();
                }
                if(population.get(i).getObjective()<gBest.getObjective()){
                    gBest=population.get(i).copy();
                }
                //System.out.println("After the "+iFes+" iteration, the best Fitness is "+gBest.getObjective());
            }
        }

    }

    public double getBestFit(){
        return gBest.getObjective();
    }
}
