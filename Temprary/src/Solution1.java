import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Solution1 {
	static Random rand=new Random();
	
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		
		double[] values= new double[] {3,4,5,6};
		double[] weights=new double[] {2,3,4,5};
		
		String a;
		a="fdhj";
		List<Item> list=new ArrayList<>() ;
		for(int i=0;i<4;i++)
		{
			list.add(new Item(i,values[i],weights[i]));
		}
		Collections.sort(list);
		Collections.reverse(list);
		for(int i=0;i<4;i++)
			System.out.println(list.get(i).density+"  "+list.get(i).id);
	}
}

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
	
	@Override
	public int compareTo(Item o) {
		if(this.density>o.density)
			return 1;
		else if(this.density==o.density)
			return 0;
		else
			return -1;
	}
}