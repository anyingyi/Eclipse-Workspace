import java.util.*;

public class Solution {
	Problem prob;
	int[] match;
	double value;
	boolean isValid;
	double weight;
	static List<Integer> densityList;
	static List<Integer> valueList;
	static Random rand=new Random();
	int lastImproving=0;
	
	public int getLastImproving() {return lastImproving;}
	public void setLastImproving(int n) { this.lastImproving = n; }
	
	public Solution(boolean init)
	{
		prob=Problem.getProblem();
		match=new int[prob.getItemNumber()];
		if(init) {
			densityList=prob.getDensityList();
			valueList=prob.getValueList();
			this.randPick();
		}
	}
	
	public Solution(Solution solution)
	{
		prob=Problem.getProblem();
		match=solution.match.clone();
		lastImproving=solution.lastImproving;
		value=solution.value;
		weight=solution.weight;
	}
	
	private void randPick()
	{
		for(int i=0;i<match.length;i++) {
			if(Solution.rand.nextDouble()<0.5)
				match[i]=1;
			else
				match[i]=0;
		}
		evalWithGreedyRepair();
		optimization(Solution.densityList);
	}
	
	
	
	/**
	 * greedy remove elements
	 * 
	 * @return
	 */
	public void evalWithGreedyRepair()
	{
		int[] items=prob.getDensityOrder();
		double capacity=prob.getCapacity();
		value=0;
		weight=0;
		for(int i=0;i<items.length;i++) {
			int item=items[i];
			if(match[item]==1&&(weight+prob.getItemWeight(item)<=capacity))
			{
				value+=prob.getItemValue(item);
				weight+=prob.getItemWeight(item);
			}
			else
				match[item]=0;
		}
	}
	
	
	
	public Solution randNeighbor()
	{
		Solution s=new Solution(this);
		int idx=Solution.rand.nextInt(match.length);
		if(s.match[idx]==1)
			s.match[idx]=0;
		else
			s.match[idx]=1;
		s.evalWithGreedyRepair();
		/*if(Solution.rand.nextDouble()<=1)
			s.optimization(Solution.densityList);
		else
			s.optimization(Solution.valueList);*/
		return s;
	}
	
	
	/**
	 * greedy add elements
	 * 
	 * @return
	 */
	public void optimization(List<Integer> itemList)
	{
		for(int i=0;i<itemList.size();i++) {
			int item=itemList.get(i);
			if(match[item]==0 && weight+prob.getItemWeight(item)<=prob.getCapacity())
			{
				match[item]=1;
				value+=prob.getItemValue(item);
				weight+=prob.getItemWeight(item);
			}
		}
	}
	
	/**
	 * mainly process function of SA
	 * 
	 * @return the best solution
	 */
	public static Solution SA(Solution solution)
	{
		Solution current=new Solution(solution);
		Solution best=new Solution(solution);
		final int MAX_G=10000;
		//final int SCHEDULE_LENGTH=1000;
		double t=1000;
		double alpha=0.99;
		for(int q=0;q<MAX_G;q++)
		{
			//for(int k=0;k<SCHEDULE_LENGTH;k++)
			{
				Solution neighbor=current.randNeighbor();
				double p=Math.random();
				double d=neighbor.getValue()-current.getValue();
				if(d>0||p<1.0/Math.exp(Math.abs(d)/t))
				{
					current=neighbor;
					if(current.getValue()>best.getValue()) {
						best.update(current);
						best.setLastImproving(q);
					}
				}
			}
			t*=alpha;
		}
		return best;
	}
	
	/**
	 * Use parameter to update this object
	 * 
	 * @param s
	 */
	public void update(Solution s)
	{
		for(int i=0;i<match.length;i++) {
			match[i]=s.match[i];
		}
		lastImproving=s.lastImproving;
		isValid=s.isValid;
		value=s.value;
		weight=s.weight;
	}
	
	public double getValue(){	return value;}
	
	public static void main(String[] args)
	{
		Solution s=new Solution(true);
		Solution best;
		best=SA(s);
		for(int i=0;i<s.prob.itemNumber;i++)
		{
			System.out.print(best.match[i]+" ");
		}
		System.out.println(best.value);
	}
}
