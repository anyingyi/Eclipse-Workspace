package ci.algorithm.cs;

import ci.Problem;
import ci.algorithm.Utility;
import ci.problem.CEC2005;

import java.util.*;

public class CSforCEC2005Main {
    public static void main(String[] args) {
        solveCEC2005();
    }

    public static void solveCEC2005() {

        //int[] problemID={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};
        int[] problemID = {1};
        int runTimes = 30;
        int populationSize = 30;
        int dimensionSize = 30;
        long maxFES = dimensionSize * 10000;
        double pa = 0.25;
        double beta = 1.5;
        List<Double> bestErrorEachFunc = new ArrayList<>(runTimes);
        for (int i = 0; i < runTimes; i++) {
            bestErrorEachFunc.add(Double.MAX_VALUE);
        }
        List<List<Double>> bestError = new ArrayList<List<Double>>(problemID.length);


        for (int proid : problemID) {
            List<List<Double>> evolutionaryData=new ArrayList<List<Double>>(runTimes);
            for (int iRun = 0; iRun < runTimes; iRun++) {
                Problem problem = new CEC2005(proid, dimensionSize);
                CS cs = new CS(problem, maxFES, populationSize, pa, beta);
                cs.run();
                bestErrorEachFunc.set(iRun, cs.getBestSolution().getObjective(0));
                System.out.println("CS算法：第 " + iRun + "次求解问题 " + proid + " 的解得精度为" + String.format("%e",
                        cs.getBestSolution().getObjective(0)));
                evolutionaryData.add(cs.getListMinf());
            }
            boolean flag= Utility.createCsvFileWithTwoList(evolutionaryData,"F:\\CSVDir","CSforCEC2005_"+proid+"_EvoData_"+runTimes+"Runs");
            if (flag == false)
            {
               System.out.print("CS算法求解测试函数 "+proid +"的收敛过程数据保存失败！");
            }
            bestError.add(bestErrorEachFunc);
        }
    }
}
