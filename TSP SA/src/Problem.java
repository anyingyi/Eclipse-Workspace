import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Problem {
	
	
	boolean isSymmetric;
	int cityNumber;
	int nearCityNumber;
	double[][] cityPosition;
	float[][] cityDistance;
	int[][] nearCityList;
	int[] bestTour;
	double bestTourLength;
	
	
	public int getCityNumber(){	return cityNumber;}
	
	public double[][] getCityPosition(){	return cityPosition;}
	
	public double getEdge(int x,int y) {return cityDistance[x][y];}
	public double getBestTourLength(){	return bestTourLength;}
	
	public int getNearCityNumber(){	return nearCityNumber;}
	
	public int[][] getNearCityList(){	return nearCityList;}
		
	boolean isSymmetric() {return isSymmetric;}
	
	
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
	
	public double evaluate(int[] tour)
	{
		double tourLength=0;
		boolean[] visited=new boolean[tour.length];
		for(int i=0;i<tour.length;i++)
		{
			if(visited[tour[i]])
				System.out.println("wrong solution");
			tourLength+=getEdge(i,tour[i]);
			visited[tour[i]]=true;
		}
		for(int i=0;i<cityNumber;i++)
		{
			if(!visited[i])
				System.out.println("wrong solution");
		}
		return tourLength;
	}
	
	public int[] randomTour() {
		int cityNumber=Problem.getProblem().getCityNumber();
		int[] tour=new int[cityNumber];
		Vector<Integer> rdyCity=new Vector<Integer>();
		for(int i=0;i<cityNumber;i++)
			rdyCity.add(new Integer(i));
		for(int i=0;i<cityNumber;i++) {
			int idx=Solution.rand.nextInt(rdyCity.size());
			tour[i]=rdyCity.remove(idx);		
		}
		int[] linkedTour=new int[cityNumber];
		for(int i=0;i<cityNumber;i++) {
			linkedTour[tour[i]]=tour[i+1];
		}
		linkedTour[tour[cityNumber-1]]=tour[0];
		return linkedTour;
	}
	
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
