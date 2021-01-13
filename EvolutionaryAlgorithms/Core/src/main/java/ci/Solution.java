package ci;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class Solution {
    protected Problem problem;
    protected List<Double> variables;
    private int numberOfObjectives;
    private List<Double> objectives;

    protected static final Random randGenerator = new Random();

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public double getObjective(int i) {
        return objectives.get(i);
    }

    public void setObjectives(int i, double objective) {
        this.objectives.set(i, objective);
    }

    public void setVariableValue(int index, double value) {
        variables.set(index, value);
    }

    public double getVariableValue(int index) {
        return variables.get(index);
    }
    
    public List<Double> getVariable() {
    	return variables;
    }

    public double getUpperBound(int index) {
        return problem.getUpperBound(index);
    }

    public double getLowerBound(int index) {
        return problem.getLowerBound(index);
    }

    public Solution(Problem problem) {
        this.problem = problem;
        variables = new ArrayList<>(problem.getNumberOfVariables());
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            double value = getLowerBound(i) + randGenerator.nextDouble() * (getUpperBound(i) - getLowerBound(i));
            variables.add(value);
        }

        numberOfObjectives = problem.getNumberOfObjectives();
        objectives = new ArrayList<>(problem.getNumberOfObjectives());
        for (int i = 0; i < numberOfObjectives; i++) {
            objectives.add(Double.MAX_VALUE);
        }

    }



    /**
     * Copy constructor
     */
    public Solution(Solution solution) {
        this.problem = solution.problem;
        this.numberOfObjectives = solution.numberOfObjectives;
        this.objectives = new ArrayList<>(solution.objectives);
        this.variables = new ArrayList<>(solution.variables);
    }

    public Solution copy() {
        return new Solution(this);
    }

    public void repairSolutionVariableValueOpposite(int jth) {
        double alpha= randGenerator.nextDouble();
        if (jth >= 0 && jth < problem.getNumberOfVariables()) {
            if (getVariableValue(jth) > getUpperBound(jth)) {
                double value=alpha*(getUpperBound(jth)-getVariableValue(jth))+getUpperBound(jth);
                setVariableValue(jth, value);
            }
            if (getVariableValue(jth) < getLowerBound(jth)) {
                double value=alpha*(getLowerBound(jth)-getVariableValue(jth))+getLowerBound(jth);
                setVariableValue(jth, value);
            }
        }
    }

    public void repairSolutionVariableValueDE(int jth) {
        double alpha= randGenerator.nextDouble();
        if (jth >= 0 && jth < problem.getNumberOfVariables()) {
            if (getVariableValue(jth) > getUpperBound(jth)) {
                double value=alpha*(getUpperBound(jth)-getVariableValue(jth))+getUpperBound(jth);
                setVariableValue(jth, value);
            }
            if (getVariableValue(jth) < getLowerBound(jth)) {
                double value=alpha*(getLowerBound(jth)-getVariableValue(jth))+getLowerBound(jth);
                setVariableValue(jth, value);
            }
        }
    }


    public void repairSolutionVariableValue(int jth) {
        if (jth >= 0 && jth < problem.getNumberOfVariables()) {
            if (getVariableValue(jth) > getUpperBound(jth)) {
                setVariableValue(jth, getUpperBound(jth));
            }
            if (getVariableValue(jth) < getLowerBound(jth)) {
                setVariableValue(jth, getLowerBound(jth));
            }
        }
    }

    public void repairSolutionVariableValue() {
        for (int i = 0; i < problem.getNumberOfVariables(); i++) {
            repairSolutionVariableValue(i);
        }
    }


    public static Comparator fitnessComparator = new Comparator<Solution>() {
        @Override
        public int compare(Solution o1, Solution o2) {
            double fit1 = o1.getObjective(0);
            double fit2 = o2.getObjective(0);

            if (fit1 < fit2) {
                return -1;
            } else if (fit1 > fit2) {
                return 1;
            } else {
                return 0;
            }


        }
    };
    
    @Override
	public String toString(){
		String str="[";
		for(int i=0;i<problem.getNumberOfVariables();i++) {
			str+=this.getVariableValue(i)+" ";
		}
		str+="]"+"\ncost:";
		for(int i=0;i<problem.getNumberOfObjectives();i++) {
			str+=String.format("%e\t",this.getObjective(i));
		}
		
		return str;
	}


    public Solution neighbor(List<Solution> population) {
        Solution temp=this.copy();
        double pa=0.2;
        int r1=randGenerator.nextInt(population.size());
        int r2=randGenerator.nextInt(population.size());
        Solution randSolution1=population.get(r1);
        Solution randSolution2=population.get(r2);

        for(int j=0;j<this.problem.numberOfVariables;j++){
            if(randGenerator.nextDouble()<pa){
                double value=this.getVariableValue(j)+ randGenerator.nextDouble()*
                        (randSolution1.getVariableValue(j)-randSolution2.getVariableValue(j));
                temp.setVariableValue(j,value);
            }
            problem.evaluate(temp);
        }
        return temp;
    }
}
