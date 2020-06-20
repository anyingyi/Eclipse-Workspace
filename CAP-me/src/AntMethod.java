import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AntMethod {

	public static Solution constructAntSolution(double[][] pher, double[][] heur, double alpha, double beta) {
		// TODO Auto-generated method stub
		List<Integer> listFac=new ArrayList<Integer>();
		int facNum=pher.length;
		int[] order=new int[facNum];
		for(int i=0;i<facNum;i++) {
			listFac.add(i);
		}
		int loc=0;
		Collections.shuffle(listFac);

		while(!listFac.isEmpty()) {
			double[] probs=new double[listFac.size()];
			//List<Double> probs=new ArrayList<Double>();
			for(int i=0;i<listFac.size();i++) {
				double p=Math.pow(pher[loc][listFac.get(i)], alpha);
				probs[i]=p;
			}
			int[] facIdx=GeneticAlgorithmOperators.rouletteWheel(probs, 1);
			order[loc++]=listFac.remove(facIdx[0]);
		}
		
		return new SolutionOrder(order);
	}
	
	public static void evaporatePher(double[][] pher, double rho) {
		double pherMin=Simulations.pherMin;
		for (int loc = 0; loc < pher.length; loc++) {
            for (int fac = 0; fac < pher[loc].length; fac++) {
                    pher[loc][fac] = (1 - rho) * pher[loc][fac];
                    if (pher[loc][fac] < pherMin) pher[loc][fac] = pherMin;
            }
    }

	}

	public static void depositPher(double[][] pher,Solution s, double rho) {
		// TODO Auto-generated method stub
		double pherMax=Simulations.pherMax;
		int[] order=((SolutionOrder)s).getP();
		for(int loc=0;loc<pher.length;loc++) {
			int fac=order[loc];
			pher[loc][fac]+=Simulations.addPher;
			if(pher[loc][fac]>pherMax)pher[loc][fac]=pherMax;
		}
	}

	public static double[][] initPher(double[][] pher) {
		// TODO Auto-generated method stub
		int facNum=pher.length;
		for(int i=0;i<facNum;i++) {
			for(int j=0;j<facNum;j++) {
				pher[i][j]=1.0;
			}
		}
		return pher;
	}

}
