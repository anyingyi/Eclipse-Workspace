import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Solution{
	private double[] x;
	private double[] velocity;
	private double[] cost;
	static Random rand=new Random();
	
	Solution(){
		double xMin=Simulations.xMin;
		double xMax=Simulations.xMax;
		int dimension=Simulations.dimension;
		x=new double[dimension];
		velocity=new double[dimension];
		for(int i=0;i<dimension;i++) {
			x[i]=xMin+rand.nextDouble()*(xMax-xMin);
			velocity[i]=rand.nextDouble()*(xMin+rand.nextDouble()*(xMax-xMin));
		}
		
		evaluate();
	}
	
	Solution(double[] x,double[] velocity){
		this.x=x;
		this.velocity=velocity;
		evaluate();
	}
	
	
	void evaluate() {
		testFunc3();
	}
	
	
	
	private void testFunc1() {
		// TODO Auto-generated method stub
		double[] y=new double[2];
		y[0]=x[0]*x[0];
		y[1]=Math.pow(x[0]-2, 2);
		cost=y.clone();
	}
	
	private void testFunc3() {
		// TODO Auto-generated method stub
		double[] y=new double[2];
		y[0]=Math.pow((x[0]*x[0]+x[1]*x[1]),0.125);
		y[1]=Math.pow(Math.pow(x[0]-0.5, 2)+Math.pow(x[1]-0.5, 2),0.25);
		cost=y.clone();
	}

	public static double[] adjustValue(double x,double v) {
		double xMin=Simulations.xMin;
		double xMax=Simulations.xMax;
		if(x>xMax)x=xMax;
		if(x<xMin)x=xMin;
		if(v>xMax)v=xMax;
		if(v<xMin)v=xMin;
		return new double[] {x,v};
	}
	
	double[] getCost() {return cost;};
	double[] getPosition(){return x;};
	double[] getVelocity() {return velocity;};
	
	@Override
	public String toString(){
		String str="[ ";
		for(int i=0;i<Simulations.dimension;i++) {
			str+=x[i]+" ";
		}
		str+="]"+"\ncost:";
		for(int i=0;i<Simulations.objFuncNum;i++) {
			str+=cost[i]+"\t";
		}
		str+="\n";
		
		return str;
	}
	
	/*@Override
	public int compareTo(Solution s) {
		if(this.cost<s.cost) {
			return -1;
		}
		else if(this.cost>s.cost) {
			return 1;
		}
		else {
			return 0;
		}
	}*/
	
	public static void main(String[] args) {
		Solution[] pop=Methods.MOPSO();
		for(int i=0;i<Simulations.popSize;i++)
			System.out.println(pop[i]);
		
	}
}


