import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Problem {
	
	
	
	int itemNumber;
	double capacity;
	double bestValue;
	double[] values;
	double[] weights;
	double[] density;
	int[] densityOrder;
	int[] valueOrder;
	List<Integer> densityList;
	List<Integer> valueList;
	
	
	public Problem() throws Exception
	{
		readFile("01f1.txt");
		initial();
	}
	
	void readFile(String fileName) throws Exception
	{
		File data=new File(fileName);
		Scanner in=new Scanner(data);
		itemNumber=in.nextInt();
		in.nextInt();
		bestValue=in.nextDouble();
		values=new double[itemNumber];
		weights=new double[itemNumber];
		for(int i=0;i<itemNumber;i++)
		{
			values[i]=in.nextDouble();
		}
		for(int i=0;i<itemNumber;i++)
		{
			weights[i]=in.nextDouble();
		}
		capacity=in.nextDouble();
		in.close();
	}
	
	//sort item
	private void initial()
	{
		//sort density order
		List<Item> list=new ArrayList<>();
		for(int i=0;i<itemNumber;i++)
			list.add(new Item(i,values[i],weights[i]));
		Collections.sort(list);
		Collections.reverse(list);
		densityOrder=new int[itemNumber];
		densityList=new ArrayList<>();
		for(int i=0;i<itemNumber;i++)
		{
			densityOrder[i]=list.get(i).getID();
			densityList.add(densityOrder[i]);
		}
		
		//sort value order
		Collections.sort(list,Comparator.comparing(u->u.getValue()));
		Collections.reverse(list);
		valueOrder=new int[itemNumber];
		valueList=new ArrayList<>();
		for(int i=0;i<list.size();i++) {
			valueOrder[i]=list.get(i).getID();
			valueList.add(valueOrder[i]);
		}
	}
	
	public static Problem getProblem() 
	{ 
		
		Problem prob = null;
		try {
			prob = new Problem();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prob;
	}
	public int getItemNumber(){	return itemNumber;}
	
	public double getCapacity(){	return capacity;}
	
	public double getItemValue(int x){	return values[x];}
	
	public double getItemWeight(int x){	return weights[x];}
	
	public double getBestValue(){	return bestValue;}
		
	public int[] getDensityOrder(){	return densityOrder;}
	
	public int[] getValueOrder(){	return valueOrder;}
	
	public List<Integer> getDensityList(){	return densityList;}
	
	public List<Integer> getValueList(){	return valueList;}
}

/**
 * "implements Comparable<class>" to compare objects a natural ordering
 * 
 * @param null
 */
class Item implements Comparable<Item>
{
	int id;
	double value;
	double weight;
	double density;
	public Item(int id,double value,double weight) {
		this.id=id;
		this.value=value;
		this.weight=weight;
		density=value/weight;
	}
	
	public int getID() { return id; }
	public double getValue() { return value;}
	public double getWeight() { return weight;}
	public double getDensity() { return density; }
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Item o) {
		if (this.density > o.density) {
			return 1;
		} else if (this.density == o.density) {
			return 0;
		} else {
			return -1;
		}
	}
}
