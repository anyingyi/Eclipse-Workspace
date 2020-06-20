import math
from os import path
import numpy as np
import matplotlib.pyplot as plt

if __name__=="__main__":
    prob_name = "01S9" 
    file_name = path.dirname(__file__) + "/" + prob_name + ".txt"
    points = np.loadtxt(file_name, delimiter=",")
    print(points.shape)
    x = np.zeros(5)
    y = np.zeros(5)
    print(len(points))
    px = np.zeros(len(points)-2)
    py = np.zeros(len(points)-2)
    num = int(points[0][0]);
    for index in range(1,num+1):
        ps = points[index]
        fac = int(ps[0])
        for i in range(4):
            x[i] = ps[i*2+1];
            y[i] = ps[i*2+2]
        x[4] = x[0]
        y[4] = y[0]
        plt.plot(x, y)
        plt.text((x[2]+x[0])/2, (y[2]+y[0])/2, str(fac))
        px[fac-1] = (x[2]+x[0])/2

    cost = points[-1][0]
        
    plt.scatter(px,py,marker="*")
    plt.xlabel(prob_name +", cost=" + str(cost))
    plt.show()
        
