package util;

public class Chaos {

	static public double[] circleMap(double x0, int times){
		double[] X=new double[times];
		X[0]=x0;
		double a=0.5,b=0.2;

		for(int i=0;i<times-1;i++){
			double value=X[i]+b-a/Math.PI*Math.sin(2*Math.PI*X[i]);
			X[i+1]=value%1;
		}

		return X;
	}

	static public double[] gaussMap(double x0, int times){
		double[] X=new double[times];
		X[0]=x0;
		double value;

		for(int i=0;i<times-1;i++){
			if(X[i]==0){
				value=1;
			}
			else{
				value=(1/X[i])%1;
			}
			X[i+1]=value;
		}

		return X;
	}

}
