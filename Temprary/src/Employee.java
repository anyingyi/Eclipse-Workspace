//import java.util.Date;

class Employee
{
	//instance fields
	String name;
	private double salary;
	//private Date hireDay;
	
	//constructor
	public Employee(String n,double s)
	{
		name=n;
		salary=s;
		//hireDay=new Date();
	}
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}

	//method
	public String getName()
	{
		name=name+" he";
		return name;
	}
	
	public static void main(String[] args) {	//static method belong to class,not instance
		
		Employee[] staff=new Employee[3];
		staff[0]=new Employee("xiao",8500);
		staff[1]=new Employee("yang",9500);
		staff[2]=new Employee();
		
		for(Employee i:staff)
			System.out.println(i.name);//+" "+staff[i].salary+" "+staff[i].hireDay);
	}
	
}
