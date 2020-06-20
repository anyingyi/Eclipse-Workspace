
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yiwen zhong
 *
 */
public class Simulations {
	
	public static void main(String[] args) {
		String filePath = "";//(new File("")).getAbsolutePath() + "/../"; 
		filePath = (new File("")).getAbsolutePath() + "/datas/Mao24/";  
		//filePath = (new File("")).getAbsolutePath() + "/datas/Para4/";
		if (Simulations.TEST_TYPE == ETestType.MULTIPLE_INSTANCE) {
			System.out.println("\n"+Simulations.getParaSetting());
			testPerformance(filePath,Simulations.TIMES);
		} else if (Simulations.TEST_TYPE == ETestType.PARAMETER_TUNNING) {
			parametersTunning4alpha(filePath);
		} else {
			System.out.println("Error test type, Cannot reach here!");
		}
	}
	
	private static void parametersTunning4alpha(String filePath) {
		java.io.File dir = new java.io.File(filePath);
		java.io.File[] files = dir.listFiles();
		String pathName = filePath.substring(filePath.lastIndexOf("/", filePath.length()-2)).substring(1);
		pathName = pathName.substring(0, pathName.length()-1);
		
		String fileName = (new File("")).getAbsolutePath() + "/results/Parameters/";
		fileName +=  pathName + "-" + Simulations.methodType + "-" + Simulations.encodingType;
		
		//System.out.println(dir.exists());
		List<double[]> resultsList = new ArrayList<>();
		List<Double> paras = new ArrayList<>();
		double min = 0.90, max = 0.99, step = 0.01;
		for (int i = 0; i < 10; i++) {
			double scale = min + i * step;
			paras.add(scale);
		}
		fileName += " alpha (" + min + "-" + max + "-" + step + ") tunning results.csv";
		
		for (java.io.File file : files) {
			for (double para : paras) {
				Simulations.alpha = para;
				System.out.println(file.getName() + ",alpha--" + para);
				double[] rs = runSimulation(file.getAbsolutePath(), Simulations.TIMES);
				for (double r : rs) {
					System.out.print(r + "\t");
				}
				System.out.println();
				resultsList.add(rs);
				Simulations.saveParametersTunningResults(fileName, paras, resultsList);
			}
		}
	}	

	private static void saveParametersTunningResults(String fileName, List<Double> paras, List<double[]> resultsList) {
		if (!Simulations.SAVING_PARA_TUNNING) { return;	}
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			for (int idx = 0; idx < resultsList.size(); idx++) {
				double[] rs = resultsList.get(idx);
				printWriter.println();
				printWriter.print(paras.get(idx % paras.size()));
				for (int j = 0; j < rs.length; j++) {
					printWriter.print(","+rs[j]);
				}
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


	private static double[] testPerformance(String filePath, final int TIMES) {
		java.io.File dir = new java.io.File(filePath);
		java.io.File[] files = dir.listFiles();
		String pathName = filePath.substring(filePath.lastIndexOf("/", filePath.length()-2)).substring(1);
		pathName = pathName.substring(0, pathName.length()-1);
		System.out.println(pathName);
		String fileName = (new File("")).getAbsolutePath() + "/results/Performance/" + pathName + "-";
		fileName += Simulations.getParaSetting();
		fileName += " results.csv";

		List<double[]> results = new ArrayList<>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			double[] result = runSimulation(file.getAbsolutePath(), TIMES);
			results.add(result);
			System.out.println();
			System.out.print(file.getName()+"\t");
			for (double d : result) {
				System.out.print(d+"\t");
			}
			System.out.println();
		    Simulations.saveFinalResults(fileName, files, results);
		}
		
		//calculate statistics results
		double[] totals = new double[results.get(0).length];
		for (int i = 0; i < files.length; i++) {
			System.out.println();
			System.out.print(files[i].getName()+"\t");
			for (int j = 0; j < results.get(i).length; j++) {
				System.out.print(results.get(i)[j]+"\t");
				totals[j] += results.get(i)[j];
			}
		}
		System.out.println("\t");
		for (int j = 0; j < totals.length; j++) {
			totals[j] = Math.round(totals[j]/files.length*1000)/1000.0;
			System.out.print(totals[j]+"\t");
		}
		return totals; //average data for all files
	}
	
	
	private static void saveFinalResults(String fileName, File[] files, List<double[]> results) {
		if (!Simulations.SAVING_FINAL_RESULTS) return;
		
		try {
			PrintWriter printWriter = new PrintWriter(new FileWriter(fileName));
			for (int i = 0; i < results.size(); i++) {
				printWriter.println();
				printWriter.print(files[i].getName());
				for (int j = 0; j < results.get(i).length; j++) {
					printWriter.print(","+results.get(i)[j]);
				}
			}
			printWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static double[] runSimulation(String fileName, final int TIMES) {
		Problem.readProblem(fileName);
		double duration = (new java.util.Date()).getTime();
		double bValue= Problem.get().getBestCost();
        Solution s = null;
        Solution bs = null;
		double[] makespans = new double[Simulations.TIMES];
		int[] iterations = new int[Simulations.TIMES]; //last improving iteration
		for (int i = 0; i < Simulations.TIMES; i++) {
			if (Simulations.methodType == EMethodType.SA ) {
				s = Methods.basicSA(); 
			} else if (Simulations.methodType == EMethodType.TA ) {
				s = Methods.basicTA(); 
			} else if (Simulations.methodType == EMethodType.NM ) {
				s = Methods.basicNM(); 
			} else if (Simulations.methodType == EMethodType.GA ) {
				s = Methods.basicGeneticAlgorithm(); 
			} else if (Simulations.methodType == EMethodType.PBIL ) {
				s = Methods.populationBasedIncrementalLearning(); 
			} else if (Simulations.methodType == EMethodType.RANDOM ) {
				s = Methods.randomSearch(); 
			} else {
				System.out.println("Cannot reach here!");;
			}
			makespans[i] = s.getCost(); // - bValue;
			iterations[i] = s.getLastImprove();
			if (Simulations.OUT_INDIVIDUAL_RUNNING_DATA) {
				System.out.println( i + " -- " + bValue + "," + makespans[i] + "," + iterations[i]);
			}
	        if (bs == null || s.cost < bs.cost) {
	        	bs = s;
	        }
		}
		
    	int idx = fileName.lastIndexOf("\\");
		String fn = fileName.substring(idx+1);
		idx = fn.indexOf(".");
		fn = fn.substring(0, idx);
        bs.save((new File("")).getAbsolutePath() + "/results/" + fn + ".txt" );

		duration = (new java.util.Date()).getTime()-duration;
		duration /= TIMES;
		duration = Math.round(duration/1000*1000)/1000.0;

		double min = Integer.MAX_VALUE, max = Integer.MIN_VALUE, count = 0;
		double total = 0;
		double totalIterations = 0;
		for (int i = 0; i < Simulations.TIMES; i++) {
			double mk = makespans[i];
			total += mk;
			if ( Math.abs((mk-bValue)) * (1.0/bValue) *100 < 1) {
				count++;
			}
			if ( mk < min) {
				min = mk;
			}
			if (mk > max) {
				max = mk;
			}
			totalIterations += iterations[i];
		}
		double ave = total / Simulations.TIMES;
		double median = Tools.median(makespans);
		double STD = Tools.standardDevition(makespans);
		double bpd = Math.abs(Math.round((min-bValue)) * (1.0/bValue) *100*1000)/1000.0;
		double wpd = Math.abs(Math.round((max-bValue)) * (1.0/bValue) *100*1000)/1000.0;
		double apd = Math.abs(Math.round((ave-bValue)) * (1.0/bValue) *100*1000)/1000.0;
		double itr = Math.round(totalIterations/iterations.length*10)/10; //average last improving iteration
		//return new double[] {bValue, min, max, ave, bpd, wpd, apd, count, itr, duration};
		double[] stat = new double[] {bValue, min, max, ave, median, STD, bpd, wpd, apd, count/makespans.length, itr, duration};
		double[] results = new double[makespans.length + stat.length];
		for (int i = 0; i < stat.length; i++) {
			results[i] = stat[i];
		}
		for (int i = 0; i < makespans.length; i++) {
			results[i+stat.length] = makespans[i];
		}
		return results;
	}
	
	public static EMethodType getMthoedType() { return Simulations.methodType;}
	public static boolean isSavingFinalResults() { return Simulations.SAVING_FINAL_RESULTS;}
	public static boolean isSavingProcessData() { return Simulations.SAVING_PROCESS_DATA;}
	public static String getParaSetting() {
		String str = methodType + "-";
		if (methodType == EMethodType.SA || methodType == EMethodType.TA || methodType == EMethodType.NM) {
			str +=  encodingType + "-";
			str += " alpha=" + Simulations.alpha + " " + Simulations.MAX_G + "-" + Simulations.MCL_FACTOR;
		} 
		
		if (methodType == EMethodType.GA) {
			str += "ORDER_BIT-";
			str += " popSize=" + popSize + " offSize=" + offspringSize + "-" + top + "-" +  learningNum + "-" + Simulations.MAX_G;
		}
		
		if (methodType == EMethodType.PBIL) {
			str += "ORDER_BIT-";
			str += " popSize=" + popSize + " bestSize=" + bestSize + "-" + rou + "-" +  learningNum + "-" + Simulations.MAX_G;
		}
		
		if (Simulations.withWidth) {
			str += "-width";
		}

		return str;
	}
	
	public static boolean withWidth = false; //CAP considering width or not
	
	private static EMethodType methodType = EMethodType.PBIL;
	public static EEncodingType encodingType = EEncodingType.ORDER_BIT;
	static final int TIMES = 25;
	
	public static final boolean OUT_INDIVIDUAL_RUNNING_DATA = true;
	public static final boolean SAVING_PROCESS_DATA = false;
	public static final boolean SAVING_FINAL_RESULTS = true;
	public static final boolean SAVING_PARA_TUNNING = true;
	public static final ETestType TEST_TYPE = ETestType.MULTIPLE_INSTANCE;
	
	//Total evaluation times = MAX_G * facility number * MAC_FACTOR
    //Parameters for SA
	public static final int MAX_G = 500;    //
	public static final int MCL_FACTOR = 50;//SCHEDULE_LENGTH = facilityNumber*Simulations.MCL_FACTOR;
	public static double alpha = 0.99;// 
	
	//Parameter for genetic algorithm, 10000, 100, 50, 2; 500, 100, 50, 2, 1
	public static int popSize = 100;	//
	public static int offspringSize = 50;//
	public static int top = 2; //Number of best offsprings to be used to replace the worst solutions in population
	public static int learningNum = 1; // Number of best offsprings to be enhanced by local search method
	
	//Parameter for PBIL, MAX_G = 1000, popSize = 500, bestSize = 50, rou = 0.1
	public static int bestSize = 50;	//
	public static double rou = 0.1; //learning rate for PBIL
}
