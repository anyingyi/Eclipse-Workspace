import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DifferentialEvolution {

	public static int[] randomNum(int popSize, int randVectorNum) {
		// TODO Auto-generated method stub
		int[] ids=new int[randVectorNum];
		List<Integer> p=new ArrayList<Integer>();
		for(int i=0;i<popSize;i++) {
			p.add(i);
		}
		Collections.shuffle(p);
		for(int i=0;i<randVectorNum;i++) {
			int temp=Methods.rand.nextInt(p.size());
			ids[i]=p.remove(temp);
		}
		
		return ids;
	}

	public static Solution makeMutaVector(Solution solution, Solution solution2, Solution solution3) {
		// TODO Auto-generated method stub
		final double F=Simulations.F;
		double[] position=new double[solution.getPosition().length];
		double[] velocity=null;
		for(int i=0;i<solution.getPosition().length;i++) {
			position[i]=solution.getPosition()[i]+F*(solution2.getPosition()[i]-solution3.getPosition()[i]);
			position[i]=Solution.adjustValue(position[i], 0)[0];
		}
		
		return new Solution(position,velocity);
	}

	public static Solution makeCrossOver(Solution solution, Solution solution2) {
		// TODO Auto-generated method stub
		double Cr=Simulations.Cr;
		double[] x1=solution.getPosition();
		double[] x2=solution2.getPosition();

		for(int i=0;i<x1.length;i++) {
			if(Methods.rand.nextDouble()<Cr) {
				x1[i]=x2[i];
				x1[i]=Solution.adjustValue(x1[i], 0)[0];
			}
		}
		return new Solution(x1,null);
	}

	public static Solution[] oppoBasedPopInit(Solution[] pop) {
		// TODO Auto-generated method stub
		double xMin=Simulations.xMin;
		double xMax=Simulations.xMax;
		double[] pos1;
		double[] oPos=new double[Simulations.dimension];
		Solution[] oPop=new Solution[pop.length*2];
		
		for(int i=0;i<pop.length;i++) {
			pos1=pop[i].getPosition().clone();
			for(int j=0;j<pos1.length;j++) {
				oPos[j]=xMin+xMax-pos1[j];
			}
			oPop[i]=new Solution(oPos,null);
		}
		
		for(int i=pop.length;i<pop.length*2;i++) {
			oPop[i]=pop[i-pop.length];
		}
		Arrays.sort(oPop);
		for(int i=0;i<pop.length;i++) {
			pop[i]=oPop[i];
		}
		return pop;
	}
	
	public static Solution[] oppoBasedPopJump(Solution[] pop) {
		// TODO Auto-generated method stub
		Arrays.sort(pop);
		double xMin=pop[0].getCost();
		double xMax=Simulations.xMax;
		double[] pos1;
		double[] oPos=new double[Simulations.dimension];
		Solution[] oPop=new Solution[pop.length*2];
		
		for(int i=0;i<pop.length;i++) {
			pos1=pop[i].getPosition().clone();
			for(int j=0;j<pos1.length;j++) {
				oPos[j]=xMin+xMax-pos1[j];
			}
			oPop[i]=new Solution(oPos,null);
		}
		
		for(int i=pop.length;i<pop.length*2;i++) {
			oPop[i]=pop[i-pop.length];
		}
		Arrays.sort(oPop);
		for(int i=0;i<pop.length;i++) {
			pop[i]=oPop[i];
		}
		return pop;
	}

}
