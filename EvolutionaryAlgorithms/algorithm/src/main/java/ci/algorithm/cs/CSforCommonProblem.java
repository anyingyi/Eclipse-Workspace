package ci.algorithm.cs;

import ci.Problem;
import ci.algorithm.Utility;
import ci.algorithm.de.DE;
import ci.problem.CEC2005;
import ci.problem.CommonProblem;

import java.util.ArrayList;
import java.util.List;

public class CSforCommonProblem {

    public static void main(String[] args) {

        solveCommonProblem();
    }

    public static void solveCommonProblem() {

        String[] problemID_common={"sph","ros","ack","grw","ras","sch",
            "sal","wht","pn1","pn2"};
        int[] problemID_cec2005={1,2,3,4,5,6,7,8,9,10};


        int runTimes = 25;
        int populationSize = 30;
        long maxFES = 10000*populationSize*2*2;
        double pa = 0.25;
        double beta = 1.5;
        int []dimensionSizes={30};

        List<List<Double>> bestError = new ArrayList<List<Double>>();
        List<Double> averageError=new ArrayList<>();

        for(int dimensionSize:dimensionSizes) {

            System.out.printf("\ndimensionSize:%d\n",dimensionSize);
            System.out.println("func\t mean\t std");

            int len=problemID_cec2005.length+problemID_common.length;
            for (int q = 0; q < len; q++) {
                List<List<Double>> evolutionaryData = new ArrayList<List<Double>>(runTimes);
                List<Double> bestErrorEachFunc = new ArrayList<>(runTimes);
                for (int i = 0; i < runTimes; i++) {
                    bestErrorEachFunc.add(Double.MAX_VALUE);
                }

                long start_time = System.currentTimeMillis();
                double sum = 0;
                for (int iRun = 0; iRun < runTimes; iRun++) {

                    Problem problem=null;
                    if (q < 10) {
                        problem = CommonProblem.func(problemID_common[q], dimensionSize);
                    } else {
                        problem = new CEC2005(problemID_cec2005[q - 10], dimensionSize);
                    }

                    //OCS cs = new OCS(problem, maxFES, populationSize, pa, beta,0.3);
                    //DE cs = new DE(problem, (long)1e+6, 100, 0.9, 0.5);
                    CS cs = new CS(problem, maxFES, populationSize, pa, beta);
                    cs.run();
                    bestErrorEachFunc.set(iRun, cs.getBestSolution().getObjective(0));
                    evolutionaryData.add(cs.getListMinf());
                    sum = sum + cs.getBestSolution().getObjective(0);
                }
                double average = sum / runTimes;
                double variance = 0;
                double temp = 0;
                bestErrorEachFunc.add(average);
                for (int j = 0; j < runTimes; j++) {
                    temp = temp + Math.pow((bestErrorEachFunc.get(j) - average), 2);
                }
                variance = temp / runTimes;
                double standDeviation = Math.pow(variance, 1.0 / 2);
                bestErrorEachFunc.add(standDeviation);

                String path = "F:\\FAFU\\Thesis\\MineExperiment\\CSInitStudy\\CS";
                String fileName="CSforTestFunc_";
                if (q < 10)
                    fileName =fileName  + problemID_common[q]  ;
                else
                    fileName = fileName  + problemID_cec2005[q - 10] ;
                fileName=fileName+"_d_" + dimensionSize+"_maxFES_" + maxFES;


                /*
                boolean flag = Utility.createCsvFileWithTwoList(evolutionaryData, path, fileName);
                if (flag == false) {
                    System.out.print("CS算法求解测试函数 " + q + "的收敛过程数据保存失败！");
                }
                bestError.add(bestErrorEachFunc);*/

                long end_time = System.currentTimeMillis();
                long elapse_time = end_time - start_time;



                if (q < 10)
                    System.out.printf("F%s\t%1.6e\t%1.6e\n", problemID_common[q], average, standDeviation);
                else
                    System.out.printf("F%s\t%1.6e\t%1.6e\n", problemID_cec2005[q - 10], average, standDeviation);
            }
        }
        //boolean flag= Utility.createCsvFileWithTwoList(bestError,"D:\\File\\FAFU\\Thesis\\Mine","CSforCEC2005_"+runTimes+"runTimes_"+dimensionSize+"dimensionSize_"+"CS_teacher");
    }
}
