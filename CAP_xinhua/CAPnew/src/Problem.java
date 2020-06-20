import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Problem {
	public static String fileName = null;
	private static Problem problem = null;
    private int facNum;
    private int[][] flow;
    private int[] lengths;
    private double width;
    private double bestCost;
    
    public int getFacNum() { return facNum;}
    public double getBestCost() { return bestCost;}
    public double getFlow(int fac1, int fac2) { return flow[fac1][fac2]; }
    public double getLength(int i) { return lengths[i];}
    public double getW() { return width; } //width of corridor
    
    private Problem(String fileName) throws FileNotFoundException,IOException {
    	FileReader data;
		Scanner scan;

		Problem.fileName = fileName;
		data = new FileReader(fileName);
		scan = new Scanner(data);
		facNum = scan.nextInt();
		bestCost = scan.nextDouble();
		flow = new int[facNum][facNum];
		lengths = new int[facNum];
		width = Integer.MAX_VALUE;
		for (int i = 0; i < facNum; i++) {
			lengths[i] = scan.nextInt();
			if (lengths[i] < width) {
				width = lengths[i];
			}
		}

		for (int i = 0; i < flow.length; i++) {
			for (int j = 0; j < flow[i].length; j++) {
				flow[i][j] = scan.nextInt();
			}
		}
		scan.close();
    }
    
    public static Problem readProblem(String fileName) {
    	try {
    		problem = new Problem(fileName);
        	return problem;
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    public static Problem get() { return problem; }
    
    public String toString() {
    	String str = "" + facNum + "\t" + bestCost + "\n\n";
    	for (int i = 0; i < lengths.length; i++) {
    		str += lengths[i] + "\t" ;
    	}
    	
    	str += "\n\n";
    	for (int i=0; i<flow.length; i++) {
    		for (int j = 0; j<flow[i].length; j++) {
    			if (j != 0) {
    				str += "\t";
    			}
    			str += flow[i][j];
    		}
    		str += "\n";
    	}
    	
    	return str;
    }
    
    /**
     * This method is used to unit test.
     * 
     * @param args
     */
    public static void main(String[] args) {
    	String fileName = (new File("")).getAbsolutePath() + "/datas/Mao24/09Am15.txt";
    	try {
    	    Problem problem = Problem.readProblem(fileName);
    	    System.out.println(problem);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
}

