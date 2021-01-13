package ci.problem;

import ci.Problem;
import ci.Solution;
import ci.problem.common.Rosenbrock;
import ci.problem.common.*;

public class CommonProblem extends Problem {
    
    public CommonProblem(){
        super();
    }


    public static Problem func(String proid, int dimensionSize) {

        switch(proid){
            case "sph":return new Sphere(dimensionSize);
            case "ros":return new Rosenbrock(dimensionSize);
            case "ack":return new Ackley(dimensionSize);
            case "grw":return new Griewank(dimensionSize);
            case "ras":return new Rastrigin(dimensionSize);
            case "sch":return new Schwefel(dimensionSize);
            case "sal":return new Salomon(dimensionSize);
            case "wht":return new Whitely(dimensionSize);
            case "pn1":return new PenalizedFunc1(dimensionSize);
            case "pn2":return new PenalizedFunc2(dimensionSize);
            default:return null;
        }
    }

    @Override
    public void evaluate(Solution solution) {
        
    }
}
