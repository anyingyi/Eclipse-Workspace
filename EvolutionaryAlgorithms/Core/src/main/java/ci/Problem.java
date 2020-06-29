package ci;

import java.util.List;

public abstract class Problem {
    private int numberOfObjectives;
    protected int numberOfVariables = 0;
    protected String name = null;
    protected List<Double> lowerLimit;
    protected List<Double> upperLimit;
    protected boolean repairSolution;

    public int getNumberOfObjectives() {
        return numberOfObjectives;
    }

    public void setNumberOfObjectives(int numberOfObjectives) {
        this.numberOfObjectives = numberOfObjectives;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(List<Double> lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public List<Double> getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(List<Double> upperLimit) {
        this.upperLimit = upperLimit;
    }

    public Double getUpperBound(int index) {
        return upperLimit.get(index);
    }

    public Double getLowerBound(int index) {
        return lowerLimit.get(index);
    }


    public boolean isRepairSolution() {
        return repairSolution;
    }

    public void setRepairSolution(boolean repairSolution) {
        this.repairSolution = repairSolution;
    }

    public abstract void evaluate(Solution solution);
}
