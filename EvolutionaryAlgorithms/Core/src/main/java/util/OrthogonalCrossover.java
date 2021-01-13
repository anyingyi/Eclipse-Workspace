package util;

import java.util.List;

//only for Q=3
public class OrthogonalCrossover {
	int[][] orthoticArray={{1,1,1,1},{1,2,2,2},{1,3,3,3},
							{2,1,2,3},{2,2,3,1},{2,3,1,2},
							{3,1,3,2},{3,2,1,3},{3,3,2,1}};
	int Q=3,M=9;
	OrthogonalCrossover(){

	}

	public void execute(double[] s1,double[] s2){
		double[][] temp=new double[3][s1.length];
		int dimension=s1.length;
		double[][] solu=new double[M][dimension];
		for(int i=0;i<s1.length;i++){
			if(s1[i]<s2[i]){
				temp[0][i]=s1[i];
				temp[2][i]=s2[i];
			}
			else{
				temp[0][i]=s2[i];
				temp[2][i]=s1[i];
			}

			temp[1][i]=(s1[i]+s2[i])/2;
		}


		for(int i=0;i<M;i++){
			//solu[i]
		}
	}
}
