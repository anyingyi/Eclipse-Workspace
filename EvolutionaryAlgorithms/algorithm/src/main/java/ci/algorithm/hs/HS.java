package ci.algorithm.hs;

import ci.EvolutionaryAlgorithm;
import ci.Problem;
import ci.Solution;
import ci.problem.common.Sphere;

import java.util.ArrayList;

public class HS extends EvolutionaryAlgorithm {
    private double par;
    private double hmcr;
    private double bw;
    public HS(Problem problem, long maxFES,int popoluationSize,double hmcr,double par,double bw){
        super(problem,maxFES,popoluationSize);
        this.hmcr=hmcr;
        this.par=par;
        this.bw=bw;
    }
    @Override
    public void run(){
        int ifes;
        this.initialPopulation();
        ifes = this.populationSize;

        listMinf = new ArrayList<>(this.dimensionSize + 1);
        for (int i = 0; i <= dimensionSize; i++) {
            listMinf.add(Double.MAX_VALUE);
        }
        this.sortPopulation();
        listMinf.set(0, population.get(0).getObjective(0));
        bestSolution = population.get(0).copy();

        int iter = 0;
        int gen = (int) (this.maxFunctionEvaluations/ this.dimensionSize);

        Solution solution=bestSolution.copy();

        while(ifes<maxFunctionEvaluations){
            for (int j=0;j<dimensionSize;j++){
                if(randGenerator.nextDouble()>hmcr){
                    solution.setVariableValue(j,randGenerator.nextDouble() * (this.problem.getUpperBound(j) - this.problem.getLowerBound(j)));
                }else{
                    int selInd=randGenerator.nextInt(populationSize);
                    solution.setVariableValue(j,this.population.get(selInd).getVariableValue(j));
                    if (randGenerator.nextDouble()<par){
                        solution.setVariableValue(j,
                                this.population.get(selInd).getVariableValue(j)*(randGenerator.nextDouble()-0.5)*2);
                        if (this.problem.isRepairSolution()){
                            solution.repairSolutionVariableValue(j);
                        }
                    }
                }
            }
            this.problem.evaluate(solution);
            ifes++;
            this.sortPopulation();
            if(solution.getObjective(0)<this.population.get(populationSize-1).getObjective(0)){
                this.population.set(populationSize-1,solution);
            }
            if (solution.getObjective(0)<bestSolution.getObjective(0)){
                bestSolution=solution.copy();
            }
            iter++;
            if (iter % gen ==0){
                listMinf.set(iter / gen, bestSolution.getObjective(0));
                System.out.println(String.format("During %d iteration, and obtain the best fitness is %e", iter/gen,
                        bestSolution.getObjective(0)));
            }
        }
        listMinf.set(listMinf.size()-1,bestSolution.getObjective(0));

    }

    public static void main(String[] args) {
        Problem problem =new Sphere(30);

        HS hs = new HS(problem,  3000, 5, 0.9,0.3,0.001);
        hs.run();
        System.out.println(hs.getBestSolution().getObjective(0)+"          "+hs.getListMinf().get(hs.getListMinf().size()-1));
    }
}
