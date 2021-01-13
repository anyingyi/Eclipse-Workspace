package util;

public class Statistic {
	public static double weibullDistribution(double t){
		double b=200;
		double k=0.25;
		return Math.exp(-Math.pow((t/b),k));
	}

	public static double average(double[] doubles) {
		double aver;
		double sum=0;
		for(double val:doubles){
			sum+=val;
		}
		aver=sum/doubles.length;
		return aver;
	}

	public static double variance(double[] doubles) {
		double aver=average(doubles);
		double sum=0;
		double vari;
		for(double val:doubles){
			sum+=Math.pow(val-aver,2);
		}
		vari=sum/doubles.length;
		return vari;
	}

	public static double standardDeviation(double[] doubles) {
		double vari=variance(doubles);
		return Math.pow(vari,0.5);
	}
}
