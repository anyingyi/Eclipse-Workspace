import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

public abstract class Solution implements Comparable<Solution> {

	protected List<Integer>[] twoList = new List[2];
	protected double[] pos;
	protected double[][] dist;
	protected double cost = Double.MAX_VALUE;
	protected int lastImprove = 0;
	static Random rand = new Random();

	public static Solution make() {
		if (Simulations.encodingType == EEncodingType.ORDER) {
			return new SolutionOrder(); //Call the construction of your implementation
		} else if (Simulations.encodingType == EEncodingType.ORDER_INT){
			return null; //Call the construction of your implementation
		} else if (Simulations.encodingType == EEncodingType.ORDER_BIT){
			return new SolutionOrderBit(); //Call the construction of your implementation
		} else {
			return new SolutionTwoList(); //Call the construction of your implementation
		}
	}

	protected void eval() {
		decoding();
		calcPosition();
		calcDistance();
		calcCost();
	}
	
	protected void calcPosition() {
		Problem problem = Problem.get();
        pos = new double[problem.getFacNum()];
        for (int r = 0; r < twoList.length; r++) {
        	double x = 0;
        	for (int f : twoList[r]) {
        		pos[f] = x + problem.getLength(f)/2.0;
        		x += problem.getLength(f);
        	}
        }
	}

	protected void calcDistance() {
		Problem problem = Problem.get();
		dist = new double[problem.getFacNum()][problem.getFacNum()];
		//Distance between facilities in same row
		for (int r = 0; r < twoList.length; r++) {
			List<Integer> list = twoList[r];
			for (int i = 0; i < list.size()-1; i++) {
				int fac1 = list.get(i);
				for (int j = i + 1; j < list.size(); j++) {
					int fac2 = list.get(j);
					dist[fac1][fac2] = Math.abs(pos[fac1] - pos[fac2]);
					dist[fac2][fac1] = dist[fac1][fac2];
				}
			}
		}

		//Distance between facilities in different row
		List<Integer> list0 = twoList[0];
		List<Integer> list1 = twoList[1];
		for (int i = 0; i < list0.size(); i++) {
			int fac1 = list0.get(i);
			for (int j = 0; j < list1.size(); j++) {
				int fac2 = list1.get(j);
				dist[fac1][fac2] = Math.abs(pos[fac1] - pos[fac2]);
				if (Simulations.withWidth) {
					dist[fac1][fac2] += problem.getW();
				}
				dist[fac2][fac1] = dist[fac1][fac2];
			}
		}
	}

	protected void calcCost() {
		Problem problem = Problem.get();
		cost = 0;
		for (int i = 0; i < pos.length - 1; i++) {
			for (int j = i+1; j < pos.length; j++) {
				cost += (dist[i][j])* problem.getFlow(i, j);
			}
		}
	}
	
	protected boolean isValid() {
		int[] c = new int[pos.length];
		for (int i = 0; i < twoList.length; i++) {
			for (int fac : twoList[i]) {
				c[fac]++;
			}
		}
		for (int n : c) {
			if (n != 1) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		String str = cost + "\n";
		for (int r = 0; r < twoList.length; r++) {
			for (int f : twoList[r]) {
				str += (f+1) + "\t";
			}
			str += "\n";
		}
		return str;
	}

	public void save(String fileName) {
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			printWriter.println(pos.length+",0,0,0,0,0,0,0,0");
			for (int f : twoList[0]) {//First row
				printWriter.print((f+1) + ",");
				double w = Problem.get().getLength(f);
				printWriter.print((pos[f]-w/2) + ",0,");
				printWriter.print((pos[f]+w/2) + ",0,");
				printWriter.print((pos[f]+w/2) + "," + w + ",");
				printWriter.print((pos[f]-w/2) + "," + w);
				printWriter.println();
			}
			for (int f : twoList[1]) {//Second row
				printWriter.print((f+1) + ",");
				double w = Problem.get().getLength(f);
				printWriter.print((pos[f]-w/2) + ",0,");
				printWriter.print((pos[f]+w/2) + ",0,");
				printWriter.print((pos[f]+w/2) + "," + (-w) + ",");
				printWriter.print((pos[f]-w/2) + "," + (-w));
				printWriter.println();
			}
			printWriter.println(cost+",0,0,0,0,0,0,0,0");
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}	    	
	}

	public abstract void decoding();
	public abstract Solution neighbor();
	public abstract Solution localSearch();


	public double getCost() { return cost; }
	public void setLastImprove(int n) { this.lastImprove = n; }
	public int getLastImprove() { return this.lastImprove;}

	@Override
	public int compareTo(Solution s) {
		if (this.cost > s.cost) {
			return 1;
		} else if (this.cost < s.cost) {
			return -1;
		} else {
			return 0;
		}
	}

	
	public static void main(String[] args) {
		String name = "14N30_05";
    	String fileName = (new File("")).getAbsolutePath() + "/datas/Mao24/" + name + ".txt";
    	Problem problem = null;
    	try {
    	    problem = Problem.readProblem(fileName);
    	    System.out.println(problem);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	//Random search
    	Solution best=Methods.basicGeneticAlgorithm();
    	
        System.out.println(best);
        best.save((new File("")).getAbsolutePath() + "/results/" + name + ".txt" );
	}
}
