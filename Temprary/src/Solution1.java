import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Parent{
	void show() throws FileNotFoundException
	{
		System.out.println("Parent's show()");
	}
}

class Child extends Parent{
	void show() throws FileNotFoundException
	{
		File fr=new File("01f1.txt");
		Scanner in= new Scanner(fr);
		in.nextLine();
		for(int i=0;i<10;i++)
			System.out.println(in.nextInt());
		System.out.println("Child's show()");
	}
}

public class Solution1 {
	public static void main(String[] args)
	{
		Child obj=new Child();
		try {
			obj.show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
