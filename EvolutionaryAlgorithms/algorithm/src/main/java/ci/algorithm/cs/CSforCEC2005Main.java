package ci.algorithm.cs;

import ci.Problem;
import ci.algorithm.Utility;
import ci.algorithm.de.DE;
import ci.problem.CEC2005;

import java.util.*;

public class CSforCEC2005Main {
    public static void main(String[] args) {
        //CSforCommonProblem.solveCommonProblem();
        solveCEC2005();
    }

    public static void solveCEC2005() {

        int[] problemID={1,2,3,4,5,6,7,8,9,10};
        int runTimes = 5;
        int populationSize = 30;
        int dimensionSize = 30;
        long maxFES = (long)3e+5;
        double pa = 0.25;
        double beta = 1.5;

        List<List<Double>> bestError = new ArrayList<List<Double>>(problemID.length);


        for (int proid : problemID) {
            List<List<Double>> evolutionaryData=new ArrayList<List<Double>>(runTimes);
            List<Double> bestErrorEachFunc = new ArrayList<>(runTimes);
            for (int i = 0; i < runTimes; i++) {
                bestErrorEachFunc.add(Double.MAX_VALUE);
            }

            long start_time = System.currentTimeMillis();
            double sum=0;
            for (int iRun = 0; iRun < runTimes; iRun++) {
                Problem problem = new CEC2005(proid, dimensionSize);
                //OCS cs = new OCS(problem, maxFES, populationSize, pa, beta,0.3);
                //CS_me cs = new CS_me(problem, maxFES, populationSize, pa, beta);
                //DE cs = new DE(problem, maxFES, populationSize, 0.9, 0.9);
                CS cs = new CS(problem, maxFES, populationSize, pa, beta);
                //CS_variants cs = new CS_variants(problem, maxFES, populationSize, pa, beta);
                cs.run();
                bestErrorEachFunc.set(iRun, cs.getBestSolution().getObjective(0));
                //System.out.println("CS算法：第 " + iRun + "次求解问题 " + proid + " 的解得精度为" + String.format("%e",
                //      cs.getBestSolution().getObjective(0)));
                evolutionaryData.add(cs.getListMinf());
                sum=sum+cs.getBestSolution().getObjective(0);
            }
            double average=sum/runTimes;
            double variance=0;
            double temp=0;
            bestErrorEachFunc.add(average);
            for(int j=0;j<runTimes;j++)
            {
                temp=temp+Math.pow((bestErrorEachFunc.get(j)-average),2);

            }
            variance=temp/runTimes;
            double standDeviation=Math.pow(variance,1.0/2);
            bestErrorEachFunc.add(standDeviation);

            /*
            boolean flag= Utility.createCsvFileWithTwoList(evolutionaryData,"F:\\FAFU\\Thesis\\Mine\\test","OCSforTestFunc_"+proid+"_maxFES_"+maxFES+"_dimensionSize_"+dimensionSize);
            if (flag == false)
            {
                System.out.print("CS算法求解测试函数 "+proid +"的收敛过程数据保存失败！");
            }
            bestError.add(bestErrorEachFunc);
*/


            long end_time = System.currentTimeMillis();
            long elapse_time=end_time-start_time;

            System.out.printf("F_%d\t%1.6e\t%1.6e\n",proid,average,standDeviation);
        }
        //boolean flag= Utility.createCsvFileWithTwoList(bestError,"D:\\File\\FAFU\\Thesis\\Mine","CSforCEC2005_"+runTimes+"runTimes_"+dimensionSize+"dimensionSize_"+"CS_teacher");
    }
}
