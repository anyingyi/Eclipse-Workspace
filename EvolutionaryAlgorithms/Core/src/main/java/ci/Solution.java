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
            } else if (fit1 == fit2) {
                return 0;
            } else {
                return 1;
            }
        }
    };


}
