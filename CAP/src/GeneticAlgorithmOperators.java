import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmOperators {
    /**
     * 
     * @param fitness �������Ӧֵ
     * @param num  Ҫѡ��ĸ�������
     * @return ��ѡ��ĸ���id���ɵ�����
     */
	public static int[] rouletteWheel(double[] fitness, int num) {
		double[] fites = new double[fitness.length];
		for (int i=0; i<fitness.length; i++) {//�����ۻ�fitness
			fites[i] = fitness[i];
			if ( i != 0) {
				fites[i] += fites[i-1];
			}
		}
		 return GeneticAlgorithmOperators.rw(fites, num);
	}
	
	/**
	 * 
	 * @param fitness
	 * @param num
	 * @return
	 */
	public static int[] disruptive(double[] fitness, int num) {
		double[] fites = new double[fitness.length];
		double average = Tools.mean(fitness);
		for (int i=0; i<fites.length; i++) {
			fites[i] = Math.abs(fitness[i] - average);
			if (i != 0) {
				fites[i] += fites[i-1];
			}
		}
		
		return GeneticAlgorithmOperators.rw(fites, num);
	}
    
    /**
     * 
     * @param order order of individuals
     * @param num   number of selected individuals
     * @param sp    selection pressure
     * @return
     */
    public static int[] rankLinear(int[] order, int num, double sp) {
		double[] fitness = new double[order.length];
		double p1 = 2 - sp;
		double p2 = 2 * (sp - 1) / (order.length  - 1);

		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = p1 + i * p2;
			if ( i != 0) {
				fitness[i] += fitness[i-1];
			}
		}
		
        return GeneticAlgorithmOperators.rw(fitness, num);
	}
    
    public static double[] fitnessOfRandLinear(int num, double sp) {
    	double[] fitness = new double[num];
		double p1 = 2 - sp;
		double p2 = 2 * (sp - 1) / (num  - 1);

		for (int i = 0; i < fitness.length; i++) {
			fitness[i] = p1 + i * p2;
		}
		for (int i = 0; i < fitness.length/2; i++) {
			double t = fitness[i];
			fitness[i] = fitness[fitness.length - i - 1];
			fitness[fitness.length - i - 1] = t;
		}
		for (int i = 1; i < fitness.length; i++) {
			fitness[i] += fitness[i-1];
		}
		return fitness;
    }
    
    /**
     * ���̶�ѡ��
     * 
     * @param fitness  accumulated fitness (������ۻ���Ӧֵ)
     * @param num ѡ���������
     * @return  ��ѡ��ĸ���id������
     */
    private static int[] rw(double[] fitness, int num) {
		int[] ids = new int[num];
		for (int id = 0; id < ids.length; id++) {
			double fit = GeneticAlgorithmOperators.rand.nextDouble()*fitness[fitness.length-1];
			for (int i=0; i<fitness.length; i++) {
				if (fit < fitness[i]) {
					ids[id] = i;
					break;
				}
			}
		}
		return ids;
    }
    
     /**
     * ������ѡ��
     * @param fitness
     * @param num
     * @param tour
     * @return
     */
    
    public static int[] tournamentSelection(double[] fitness, int num, int tour) {
    	int[] ids = new int[num];
		for (int id = 0; id < ids.length; id++) {
			int i = rand.nextInt(fitness.length);
			int c = 1;
			while (c++ < tour) {
				int j = rand.nextInt(fitness.length);
				if (fitness[j] > fitness[i]) {
					i = j;
				}
			}
			ids[id] = i;
		}
		return ids;
    }
    
    /**
     * Order Crossover (OX1)
     * Davis, L. (1985). Applying Adaptive Algorithms to Epistatic Domains. Proceedings of the International Joint Conference on Artificial Intelligence, 162�C164.
     * The OX1 exploits the property that the order of element (not their positions) is important.
     * 
     * @param p1
     * @param p2
     * @return
     */
    
    public static int[][] orderCrossover(int[] p1, int[] p2) {
    	int[] c1 = new int[p1.length];
    	int[] c2 = new int[p2.length];
    	
    	int pos1 = rand.nextInt(p1.length);
    	int pos2 = rand.nextInt(p1.length);
    	
    	if (pos1 > pos2) {
    		int temp = pos1;
    		pos1 = pos2; 
    		pos2 = temp;
    	}
    	
    	for (int i = pos1; i <= pos2; i++) {
    		c1[i] = p1[i];
    		c2[i] = p2[i];
    	}
    	
    	List<Integer> restOfc1 = new ArrayList<>();
    	List<Integer> restOfc2 = new ArrayList<>();
    	for (int i = 0; i < pos1; i++) {
    		restOfc1.add(p1[i]);
    		restOfc2.add(p2[i]);
    	}
    	for (int i = pos2+1; i < p1.length; i++) {
    		restOfc1.add(p1[i]);
    		restOfc2.add(p2[i]);
    	}
    	
        int idx = (pos2+1) % c1.length;
        for (int i = 0; i < p2.length; i++) {
        	int pos = (pos2+1+i) % p2.length;
        	if (restOfc1.contains(p2[pos])) {
        		c1[idx] = p2[pos];
        		idx = (idx+1) % c1.length;
        	}
        }
        
        idx = (pos2+1) % c2.length;
        for (int i = 0; i < p1.length; i++) {
        	int pos = (pos2+1+i) % p1.length;
        	if (restOfc2.contains(p1[pos])) {
        		c2[idx] = p1[pos];
        		idx = (idx+1) % c2.length;
        	}
        }
    	return new int[][] {c1, c2};		
    }
    
    public static int[][] linearOrderCrossover(int[] p1, int[] p2) {
    	int[] c1 = new int[p1.length];
    	int[] c2 = new int[p2.length];
    	
    	int pos1 = rand.nextInt(p1.length);
    	int pos2 = rand.nextInt(p1.length);
    	
    	if (pos1 > pos2) {
    		int temp = pos1;
    		pos1 = pos2; 
    		pos2 = temp;
    	}
    	
    	for (int i = pos1; i <= pos2; i++) {
    		c1[i] = p1[i];
    		c2[i] = p2[i];
    	}
    	
    	List<Integer> restOfc1 = new ArrayList<>();
    	List<Integer> restOfc2 = new ArrayList<>();
    	for (int i = 0; i < pos1; i++) {
    		restOfc1.add(p1[i]);
    		restOfc2.add(p2[i]);
    	}
    	for (int i = pos2+1; i < p1.length; i++) {
    		restOfc1.add(p1[i]);
    		restOfc2.add(p2[i]);
    	}
    	
        int idx = 0;
        for (int i = 0; i < p2.length; i++) {
        	if (restOfc1.contains(p2[i])) {
        		if (idx == pos1) {
        			idx = pos2 + 1;
        		}
        		c1[idx++] = p2[i];
        	}
        }
        
        idx = 0;
        for (int i = 0; i < p1.length; i++) {
        	if (restOfc2.contains(p1[i])) {
        		if (idx == pos1) {
        			idx = pos2 + 1;
        		}
        		c2[idx++] = p1[i];
        	}
        }
    	return new int[][] {c1, c2};		
    }
    
    /**
     * partially-mapped Crossover
     * Goldberg, D. E. & Lingle, Jr., R. (1985). Alleles, Loci and the TSP. In Grefenstette, J. J. (ed.) Proceedings of the First International Conference on Genetic Algorithms and Their Applications, 154�C159. Hillsdale, New Jersey: Lawrence Erlbaum.
     *It passes on ordering and value information from the parent tours to the offspring tours.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static int[][] partiallyMappedCrossover(int[] p1, int[] p2) {
    	int[] c1 = new int[p1.length];
    	int[] c2 = new int[p2.length];
    	
    	int pos1 = rand.nextInt(p1.length);
    	int pos2 = rand.nextInt(p1.length);
    	
    	if (pos1 > pos2) {
    		int temp = pos1;
    		pos1 = pos2; 
    		pos2 = temp;
    	}
    	
    	List<Integer> inc1 = new ArrayList<>();
    	List<Integer> inc2 = new ArrayList<>();   	
    	for (int i = pos1; i <= pos2; i++) {
    		c1[i] = p2[i];
    		inc1.add(c1[i]);
    		c2[i] = p1[i];
    		inc2.add(c2[i]);
    	}
    	
        for (int i = 0; i < c1.length; i++) {
        	if (i >= pos1 && i<= pos2) {
        		continue;
        	}
        	int e = p1[i];
        	int idx = inc1.indexOf(e);
        	while (idx != -1) {//mapping
        		e = inc2.get(idx);
        		idx = inc1.indexOf(e);
        	}
        	c1[i] = e;
        }
        
        for (int i = 0; i < c2.length; i++) {
        	if (i >= pos1 && i<= pos2) {
        		continue;
        	}
        	int e = p2[i];
        	int idx = inc2.indexOf(e);
        	while (idx != -1) {//mapping
        		e = inc1.get(idx);
        		idx = inc2.indexOf(e);
        	}
        	c2[i] = e;
        }
 
    	return new int[][] {c1, c2};		
    }
    
    /**
     * The position based operator (Syswerda 1991) also starts by selecting a random set of positions in the parent tours. However, this operator imposes
       the position of the selected cities on the corresponding cities of the other parent.
     * Syswerda, G. (1991). Schedule Optimization Using Genetic Algorithms. In Davis, L. (ed.) Handbook of Genetic Algorithms, 332�C349. New York: Van Nostrand Reinhold.

     * @param p1
     * @param p2
     * @return
     */
    
    public static int[][] positionBasedCrossover(int[] p1, int[] p2) {
    	int[] c1 = new int[p1.length];
    	int[] c2 = new int[p2.length];
    	
    	boolean[] flagIndexC1 = new boolean[c1.length];
    	boolean[] flagDataC1 = new boolean[c1.length];
    	boolean[] flagIndexC2 = new boolean[c2.length];
    	boolean[] flagDataC2 = new boolean[c2.length];
    	for (int i = 0; i < c1.length; i++) {
    		if (rand.nextDouble() < 0.5) {
    			c1[i] = p2[i];
    			flagIndexC1[i] = true;
    			flagDataC1[c1[i]] = true;
    		}
    		if (rand.nextDouble() < 0.5) {
    			c2[i] = p1[i];
    			flagIndexC2[i] = true;
    			flagDataC2[c2[i]] = true;
    		}
    	}
    	
    	int index = 0;
    	for (int i = 0; i < c1.length; i++) {
    		int data = p1[i];
    		if (!flagDataC1[data]) {
    			while (flagIndexC1[index]) {
    				index++;
    			}
    			c1[index] = data;
    			flagIndexC1[index] = true;
    			flagDataC1[data] = true;
    		}
    	}
    	
    	index = 0;
    	for (int i = 0; i < c2.length; i++) {
    		int data = p2[i];
    		if (!flagDataC2[data]) {
    			while (flagIndexC2[index]) {
    				index++;
    			}
    			c2[index] = data;
    			flagIndexC2[index] = true;
    			flagDataC2[data] = true;
    		}
    	}
    	
    	return new int[][] {c1, c2};		
    }
    
    public static int[][] modifiedPositionBasedCrossover(int[] p1, int[] p2) {
    	int[] c1 = new int[p1.length];
    	int[] c2 = new int[p2.length];
    	
    	boolean[] flagIndexC1 = new boolean[c1.length];
    	boolean[] flagDataC1 = new boolean[c1.length];
    	boolean[] flagIndexC2 = new boolean[c2.length];
    	boolean[] flagDataC2 = new boolean[c2.length];
    	for (int i = 0; i < c1.length; i++) {
    		if (rand.nextDouble() < 0.5) {
    			c1[i] = p2[i];
    			flagIndexC1[i] = true;
    			flagDataC1[c1[i]] = true;
    		}
    		if (rand.nextDouble() < 0.5) {
    			c2[i] = p1[i];
    			flagIndexC2[i] = true;
    			flagDataC2[c2[i]] = true;
    		}
    	}
    	
    	for (int i = 0; i < c1.length; i++) {
    		int data = p1[i];
    		if (!flagDataC1[data] && !flagIndexC1[i]) {
    			c1[i] = data;
    			flagIndexC1[i] = true;
    			flagDataC1[data] = true;
    		}
    	}
    	int index = 0;
    	for (int i = 0; i < c1.length; i++) {
    		int data = p1[i];
    		if (!flagDataC1[data]) {
    			while (flagIndexC1[index]) {
    				index++;
    			}
    			c1[index] = data;
    			flagIndexC1[index] = true;
    			flagDataC1[data] = true;
    		}
    	}
    	
    	for (int i = 0; i < c2.length; i++) {
    		int data = p2[i];
    		if (!flagDataC2[data] && !flagIndexC2[i]) {
    			c2[i] = data;
    			flagIndexC2[i] = true;
    			flagDataC2[data] = true;
    		}
    	}
    	index = 0;
    	for (int i = 0; i < c2.length; i++) {
    		int data = p2[i];
    		if (!flagDataC2[data]) {
    			while (flagIndexC2[index]) {
    				index++;
    			}
    			c2[index] = data;
    			flagIndexC2[index] = true;
    			flagDataC2[data] = true;
    		}
    	}
    	
    	return new int[][] {c1, c2};		
    }
    
    public static int[][] twoPointCrossover(int[] p1, int[] p2) {
    	int[] c1 = p1.clone();
    	int[] c2 = p2.clone();

    	int pos1 = rand.nextInt(p1.length);
    	int pos2 = rand.nextInt(p1.length);

    	if (pos1 > pos2) {
    		int temp = pos1;
    		pos1 = pos2; 
    		pos2 = temp;
    	}

    	for (int i = pos1; i < pos2; i++) {
    		c1[i] = p2[i];
    		c2[i] = p1[i];
    	}
    	return new int[][] {c1, c2};		
    }    
    
    /**
     * The exchange mutation operator (Banzhaf 1990) randomly selects two elements and exchanges them.
     * Banzhaf, W. (1990). The ��Molecular�� Traveling Salesman. Biological Cybernetics 64: 7�C14.
     * 
     * @param p
     * @param times
     */
    public static void swap(int[] p, int times) {
    	for (int i = 0; i < times; i++) {
			int x = rand.nextInt(p.length);
			int y = rand.nextInt(p.length);
			while (x == y) {
				y = rand.nextInt(p.length);
			}
			int temp = p[x];
			p[x] = p[y];
			p[y] = temp;
		}    	
    }
    
    /**
     * The insertion mutation operator (Fogel 1988; Michalewicz 1992) randomly chooses a element, removes it and inserts it in a randomly selected place.
     * Fogel, D. B. (1988). An Evolutionary Approach to the Traveling Salesman Problem. Biological Cybernetics 60: 139�C144.
     * Michalewicz, Z. (1992). Genetic Algorithms + Data Structures = Evolution Programs. Berlin Heidelberg: Springer Verlag.
     * 
     * @param p
     * @param times
     */
   
    public static void insert(int[] p, int times) {
    	for (int i = 0; i < times; i++) {
			int x = rand.nextInt(p.length);
			int y = rand.nextInt(p.length);
			while (x == y) {
				y = rand.nextInt(p.length);
			}
			int temp = p[x];
			while (x < y) {
				p[x] = p[x+1];
				x++;
			}
			while (x > y) {
				p[x] = p[x-1];
				x--;
			}
			p[y] = temp;
		}    	
    }
    
    public static void inverse(int[] p) {
    	int x = rand.nextInt(p.length);
    	int y = rand.nextInt(p.length);
    	while (x == y) {
    		y = rand.nextInt(p.length);
    	}
    	if ( x > y ) {
    		int temp = x;
    		x = y;
    		y = temp;
    	}
     	while (x < y) {
     		int temp = p[x];
    		p[x] = p[y];
    		p[y] = temp;
    		x++;
    		y--;
    	}
     }
    
    public static void move(int[] p) {
    	int x = rand.nextInt(p.length);
    	int y = rand.nextInt(p.length);
    	while (x == y) {
    		y = rand.nextInt(p.length);
    	}
    	if ( x > y ) {
    		int temp = x;
    		x = y;
    		y = temp;
    	}
    	
    	int k = rand.nextInt(p.length);
    	int[] t = p.clone();
    	boolean[] flagIndex = new boolean[p.length];
      	for (int i = x; i <= y; i++) {
     		p[k] = t[i];
     		flagIndex[k] = true;
     		k = (k+1) % p.length;
     	}
     	
     	k = 0;
     	for (int i = 0; i < p.length; i++) {
     		if (i >= x && i <= y) continue;
     		while (flagIndex[k]) {
     			k++;
     		}
     		p[k] = t[i];
     		k++;
     	}
     }
    
    /**
     * The scramble mutation operator (Syswerda 1991) selects a random section and scrambles the elements in it
     * Syswerda, G. (1991). Schedule Optimization Using Genetic Algorithms. In Davis, L. (ed.) Handbook of Genetic Algorithms, 332�C349. New York: Van Nostrand Reinhold.
     * 
     * @param p
     */
    public static void scramble(int[] p) {
    	int x = rand.nextInt(p.length);
    	int y = rand.nextInt(p.length);
    	while (x == y) {
    		y = rand.nextInt(p.length);
    	}
    	
    	if (x > y) {
    		int t = x;
    		x = y;
    		y = t;
    	}
    	
    	List<Integer> s = new LinkedList<>();
    	for (int i = x; i < y+1; i++) {
    		s.add(p[i]);
    	}
    	Collections.shuffle(s);
    	for (int i = 0; i < s.size(); i++) {
    		p[x+i] = s.get(i);
    	}
    }
    
    private static Random rand = new Random();
    
    public static void main(String[] args) {
    	double[] fs = GeneticAlgorithmOperators.fitnessOfRandLinear(10, 2);
    	for (double f : fs) {
    		System.out.print(f+"\t");
    	}
    	System.out.println();
    }
}
