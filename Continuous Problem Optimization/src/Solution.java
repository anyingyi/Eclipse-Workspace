import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Solution implements Comparable<Solution> {
	private double[] x;
	private double[] velocity;
	private double cost=0;
	protected int trailCount=0;
	static Random rand=new Random();
	
	Solution(){
		double xMin=Simulations.xMin;
		double xMax=Simulations.xMax;
		int dimension=Simulations.dimension;
		this.x=new double[dimension];
		this.velocity=new double[dimension];
		for(int i=0;i<dimension;i++) {
			this.x[i]=xMin+rand.nextDouble()*(xMax-xMin);
			this.velocity[i]=rand.nextDouble()*(xMin+rand.nextDouble()*(xMax-xMin));
		}
		
		evaluate();
	}
	
	Solution(double[] x,double[] velocity){
		this.x=x;
		this.velocity=velocity;
		evaluate();
	}
	
	Solution copy() {
		return  new Solution(this.x,this.velocity);
	}
	
	void evaluate() {
		funcSphere();
	}
	
	/**
	 * bivariate Michaelwicz function.
	 * has a global minimum f∗ ≈ −1.8013 at (2.20319, 1.57049). 
	 * 
	 */
	void funcMicha()
	{
		this.cost=-Math.sin(x[0])*Math.pow(Math.sin(x[0]*x[0]/Math.PI), 20)-Math.sin(x[1])*Math.pow(Math.sin(2*x[1]*x[1]/Math.PI), 20);
	}
	
	/**
	 * Sphere function.
	 * has a global minimum f= 0 at (0,0...,0). 
	 * 
	 */
	void funcSphere() {
		for(int i=0;i<Simulations.dimension;i++)
			this.cost+=x[i]*x[i];
	}
	
	/**
	 * Rosenbrock function.
	 * has a global minimum f= 0 at (0,0...,0). 
	 * 
	 */
	void funcRosenbrock() {
		for(int i=0;i<Simulations.dimension-1;i++)
			this.cost+=100*(Math.pow(x[i+1]-x[i]*x[i], 2))+(x[i]-1)*(x[i]-1);
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
	
	double getCost() {return cost;};
	double[] getPosition(){return x;};
	double[] getVelocity() {return velocity;};
	
	@Override
	public String toString(){
		String str="[";
		for(int i=0;i<Simulations.dimension;i++) {
			str+=x[i]+" ";
		}
		str+="]"+"\ncost:"+cost;
		return str;
	}
	
	@Override
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
	}
	
	public static void main(String[] args) {
		Solution best=Methods.basicCuckoo();
		System.out.println(best);
	}
}


