import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmOperators {
	
	static Random rand=new Random();

	/** 
     * use roulette wheel to generate an array of mating pool
     * 
     * @param fitness  fitness of parent solution
     * @param num  the number of generating offspring
     */
	public static int[] rouletteWheel(double[] fitness, int num) {
		// TODO Auto-generated method stub
		double[] fites=new double[fitness.length];
		for(int i=0;i<fitness.length;i++)
		{
			fites[i]=fitness[i];
			if(i!=0) {
				fites[i]+=fites[i-1];
			}
		}
		
		return GeneticAlgorithmOperators.rw(fites,num);
	}

	private static int[] rw(double[] fites, int num) {
		// TODO Auto-generated method stub
		int[] ids=new int[num];
		for(int id=0;id<ids.length;id++) {
			double fit=Solution.rand.nextDouble()*fites[fites.length-1];
			for(int i=0;i<fites.length;i++) {
				if(fit<fites[i]) {
					ids[id]=i;
					break;
				}
			}
		}
		return ids;
	}
	
	/** 
     * use tournament selection to generate an array of mating pool
     * 
     * @param fitness  fitness of parent solution
     * @param num  the number of generating offspring
     */
	public static int[] tournamentSelection(double[] fitness,int num,int tour){
		int[] ids=new int[num];
		for(int i=0;i<ids.length;i++) {
			int m=rand.nextInt(ids.length);
			for(int j=0;j<tour;j++) {
				int k=rand.nextInt(ids.length);
				if(fitness[m]>fitness[k])
					m=k;
			}
			ids[i]=m;
		}
		
		return ids;
	}

	

	/**
     * one offspring get some points of parent1 randomly and get rest from parent2. 
     * another one is the same theory.
     * 
     * @param p1  parent1
     * @param p2  parent2
     * @return offspring crossover by parent
     */
	public static int[][] positionBasedCrossover(int[] p1, int[] p2) {
		// TODO Auto-generated method stub
		int[] c1=new int[p1.length];
		int[] c2=new int[p2.length];
		
		boolean[] flagIndexC1=new boolean[c1.length];
		boolean[] flagDataC1=new boolean[c1.length];
		boolean[] flagIndexC2=new boolean[c2.length];
		boolean[] flagDataC2=new boolean[c2.length];
		
		for(int i=0;i<c1.length;i++) {
			if(rand.nextDouble()<0.5) {
				c1[i]=p2[i];
				flagIndexC1[i]=true;
				flagDataC1[c1[i]]=true;
			}
			if(rand.nextDouble()<0.5) {
				c2[i]=p1[i];
				flagIndexC2[i]=true;
				flagDataC2[c2[i]]=true;
			}
		}
		
		int index=0;
		for(int i=0;i<c1.length;i++) {
			int data=p1[i];
			if(!flagDataC1[data]) {
				while(flagIndexC1[index]) {
					index++;
				}
				c1[index]=data;
				flagIndexC1[index]=true;
				flagDataC1[data]=true;
			}
		}
		
		index=0;
		for(int i=0;i<c2.length;i++) {
			int data=p2[i];
			if(!flagDataC2[data]) {
				while(flagIndexC2[index]) {
					index++;
				}
				c2[index]=data;
				flagIndexC2[index]=true;
				flagDataC2[data]=true;
			}
		}
		
		return new int[][] {c1,c2};
	}

	public static int[][] twoPorintCrossover(int[] p1, int[] p2) {
		// TODO Auto-generated method stub
		int[] c1=p1.clone();
		int[] c2=p2.clone();
		
		int pos1=rand.nextInt(p1.length);
		int pos2=rand.nextInt(p2.length);
		
		c1[pos1]=p2[pos2];
		c2[pos2]=p1[pos1];
		
		return new int[][] {c1,c2};
	}
	
	/**
     * one offspring get one section of parent1 randomly and get rest from parent2. 
     * another one is the same theory.
     * 
     * @param p1  parent1
     * @param p2  parent2
     * @return offspring crossover by parent
     */
	public static int[][] orderCrossover(int[] p1,int[] p2){
		int[] c1=new int[p1.length];
		int[] c2=new int[p2.length];
		
		int pos1=rand.nextInt(p1.length);
		int pos2=rand.nextInt(p1.length);
		
		if(pos1>pos2) {
			int temp=pos1;
			pos1=pos2;
			pos2=temp;
		}
		
		for(int i=pos1;i<=pos2;i++) {
			c1[i]=p1[i];
			c2[i]=p2[i];
		}
		
		List<Integer> restOfc1=new ArrayList<>();
		List<Integer> restOfc2=new ArrayList<>();
		for(int i=0;i<pos1;i++) {
			restOfc1.add(p1[i]);
			restOfc2.add(p2[i]);
		}
		for(int i=pos2+1;i<p1.length;i++) {
			restOfc1.add(p1[i]);
			restOfc2.add(p2[i]);
		}
		
		int idx=(pos2+1)%c1.length;
		for(int i=0;i<p2.length;i++) {
			int pos=(pos2+1+i)%p2.length;
			if(restOfc1.contains(p2[pos])) {
				c1[idx]=p2[pos];
				idx=(idx+1)%c1.length;
			}
		}
		
		idx=(pos2+1)%c1.length;
		for(int i=0;i<p1.length;i++) {
			int pos=(pos2+1+i)%p1.length;
			if(restOfc2.contains(p1[pos])) {
				c2[idx]=p1[pos];
				idx=(idx+1)%c2.length;
			}
		}
		
		return new int[][] {c1,c2};
		
	}
	
	public static int[][] linearOrderCrossover(int[] p1,int[] p2){
		int[] c1=new int[p1.length];
		int[] c2=new int[p2.length];
		
		int pos1=rand.nextInt(p1.length);
		int pos2=rand.nextInt(p1.length);
		
		if(pos1>pos2) {
			int temp=pos1;
			pos1=pos2;
			pos2=temp;
		}
		
		for(int i=pos1;i<=pos2;i++) {
			c1[i]=p1[i];
			c2[i]=p2[i];
		}
		
		List<Integer> restOfc1=new ArrayList<>();
		List<Integer> restOfc2=new ArrayList<>();
		for(int i=0;i<pos1;i++) {
			restOfc1.add(p1[i]);
			restOfc2.add(p2[i]);
		}
		for(int i=pos2+1;i<p1.length;i++) {
			restOfc1.add(p1[i]);
			restOfc2.add(p2[i]);
		}
		
		int idx=0;
		for(int i=0;i<p2.length;i++) {
			if(restOfc1.contains(p2[i])) {
				if(idx==pos1)
					idx=pos2+1;
				c1[idx++]=p2[i];
			}
		}
		
		idx=0;
		for(int i=0;i<p1.length;i++) {
			if(restOfc2.contains(p1[i])) {
				if(idx==pos1)
					idx=pos2+1;
				c2[idx++]=p1[i];
			}
		}
		
		return new int[][] {c1,c2};
		
	}
	
	public static void swap(int[] p, int times) {
		// TODO Auto-generated method stub
		for(int i=0;i<times;i++) {
			int x=rand.nextInt(p.length);
			int y=rand.nextInt(p.length);
			while(x==y) {
				y=rand.nextInt(p.length);
			}
			int temp=p[x];
			p[x]=p[y];
			p[y]=temp;
		}
		
	}

}
