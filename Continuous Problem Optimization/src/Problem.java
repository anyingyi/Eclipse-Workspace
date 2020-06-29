
public class Problem {
	double cost;
	double []x;
	/**
	 * bivariate Michaelwicz function.
	 * has a global minimum f∗ ≈ −1.8013 at (2.20319, 1.57049). 
	 * 
	 */
	void funcMicha()
	{
		cost=-Math.sin(x[0])*Math.pow(Math.sin(x[0]*x[0]/Math.PI), 20)-Math.sin(x[1])*Math.pow(Math.sin(2*x[1]*x[1]/Math.PI), 20);
	}
	
	/**
	 * Sphere function.
	 * has a global minimum f= 0 at (0,0...,0). 
	 * 
	 */
	void funcSphere() {
		for(int i=0;i<Simulations.dimension;i++)
			cost+=x[i]*x[i];
	}
	
	/**
	 * Rosenbrock function.
	 * has a global minimum f= 0 at (0,0...,0). 
	 * 
	 */
	void funcRosenbrock() {
		for(int i=0;i<Simulations.dimension-1;i++)
			cost+=100*(Math.pow(x[i+1]-x[i]*x[i], 2))+(x[i]-1)*(x[i]-1);
	}
}
