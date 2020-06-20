

import java.util.List;

public class Problem {
    protected int numberOfVariables = 0 ;
    protected String name = null ;
    protected List<Double> lowerLimit ;
    protected List<Double> upperLimit ;
    public Problem() {
        this.setNumberOfVariables(10);
    }
    public int getNumberOfVariables() {
        return numberOfVariables ;
    }
    protected void setName(String name) {
        this.name = name;
    }
    protected void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }
    public Double getUpperBound(int index) {
        return upperLimit.get(index);
    }
    public Double getLowerBound(int index) {
        return lowerLimit.get(index);
    }
    protected void setLowerLimit(List<Double> lowerLimit) {
        this.lowerLimit = lowerLimit;
    }
    protected void setUpperLimit(List<Double> upperLimit) {
        this.upperLimit = upperLimit;
    }
    public void evaluate(Solution solution) {
        return;
    }
}
