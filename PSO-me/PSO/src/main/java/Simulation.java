package main.java;


import java.util.ArrayList;
import java.util.List;

public class Simulation {
    public static void main(String[] args) {
        Problem problem=new Sphere(30);
        List<Double> res=new ArrayList<Double>(30);
        for (int iRun=0;iRun<30;iRun++) {
            SPSO algPSO = new SPSO(problem, 40, 10000 * 30, 1.49445, 1.49445, 0.729);
            algPSO.run();
            res.add(algPSO.getBestFit());
            System.out.println(String.format("第%d次运行的结果是：%e", iRun+1,algPSO.getBestFit()));
        }

    }
}
