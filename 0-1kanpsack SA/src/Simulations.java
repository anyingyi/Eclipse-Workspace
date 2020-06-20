import java.io.File;
import java.util.Scanner;



public class Simulations {
	int itemNumber;
	double capacity;
	double[] values;
	double[] weights;
	double bestValue;
	
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
	
	public static void main(String[] args) 
	{
		Simulations s = new Simulations();
		try {
			s.readFile("01f1.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<s.itemNumber;i++)
		{
			System.out.println(s.values[i]);
		}
	}
}


