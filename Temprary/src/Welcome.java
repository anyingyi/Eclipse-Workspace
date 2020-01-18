import java.util.Scanner;

public class Welcome{
	public static void main(String[] args) {
		/*Scanner in=new Scanner(System.in);
		
		String name =in.nextLine();
		
		int age=in.nextInt();
		
		System.out.println("hello, "+name+" age: "+age);*/
		
		Scanner in=new Scanner(System.in);
		int k=in.nextInt();
		int n=in.nextInt();
		
		int numbers[]=new int[n];
		for(int i=0;i<numbers.length;i++)
			numbers[i]=i+1;
		
		int[] result=new int[k];
		for(int i=0;i<result.length;i++)
		{
			int r=(int)(Math.random()*n);
			result[i]=numbers[r];
			numbers[r]=numbers[n-1];
			n--;
		}
		//Arrays.sort(result);
		for(int r:result)
			System.out.println(r);
	}
}
