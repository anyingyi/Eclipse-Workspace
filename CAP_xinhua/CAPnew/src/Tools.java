import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tools {
    /**
     * 
     * @param arr
     * @return
     */
    public static double sum(double[] arr) {
        double sum = 0;
        for (double num : arr) {
            sum += num;
        }
        return sum;
    }

    /**
     *
     * @param arr
     * @return
     */
    public static double mean(double[] arr) {
        return sum(arr) / arr.length;
    }

    /**
     * Mode
     *
     * @param arr
     * @return
     */
    public static double mode(double[] arr) {
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        for (int i = 0; i < arr.length; i++) {
            if (map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i]) + 1);
            } else {
                map.put(arr[i], 1);
            }
        }
        int maxCount = 0;
        double mode = -1;
        Iterator<Double> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            double num = iter.next();
            int count = map.get(num);
            if (count > maxCount) {
                maxCount = count;
                mode = num;
            }
        }
        return mode;
    }

    /**
     * Median
     *
     * @param arr
     * @return
     */
    public static double median(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        if (tempArr.length % 2 == 0) {
            return (tempArr[tempArr.length >> 1] + tempArr[(tempArr.length >> 1) - 1]) / 2;
        } else {
            return tempArr[(tempArr.length >> 1)];
        }
    }


    /**
     * Middle range
     *
     * @param arr
     * @return
     */
    public static double midrange(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return (min + max) / 2;
    }

    /**
     * 求四分位数
     *
     * @param arr
     * @return 存放三个四分位数的数组
     */
    public static double[] quartiles(double[] arr) {
        double[] tempArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(tempArr);
        double[] quartiles = new double[3];
        // 第二四分位数（中位数）
        quartiles[1] = median(tempArr);
        // 求另外两个四分位数
        if (tempArr.length % 2 == 0) {
            quartiles[0] = median(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = median(Arrays.copyOfRange(tempArr, tempArr.length / 2, tempArr.length));
        } else {
            quartiles[0] = median(Arrays.copyOfRange(tempArr, 0, tempArr.length / 2));
            quartiles[2] = median(Arrays.copyOfRange(tempArr, tempArr.length / 2 + 1, tempArr.length));
        }
        return quartiles;
    }

    /**
     * 求极差
     *
     * @param arr
     * @return
     */
    public static double range(double[] arr) {
        double max = arr[0], min = arr[0];
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
            if (arr[i] < min) {
                min = arr[i];
            }
        }
        return max - min;
    }

    /**
     * 求四分位数极差
     *
     * @param arr
     * @return
     */
    public static double quartilesRange(double[] arr) {
        return range(quartiles(arr));
    }

    /**
     * 求截断均值
     *
     * @param arr 求值数组
     * @param p   截断量p，例如p的值为10，则截断20%（高10%，低10%）
     * @return
     */
    public static double trimmedMean(double[] arr, int p) {
        int tmp = arr.length * p / 100;
        double[] tempArr = Arrays.copyOfRange(arr, tmp, arr.length + 1 - tmp);
        return mean(tempArr);
    }

    /**
     * 求方差
     *
     * @param arr
     * @return
     */
    public static double variance(double[] arr) {
        double variance = 0;
        double sum = 0, sum2 = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
            sum2 += arr[i] * arr[i];
        }
        variance = sum2 / arr.length - (sum / arr.length) * (sum / arr.length);
        return variance;
    }

    /**
     * 求绝对平均偏差(AAD)
     *
     * @param arr
     * @return
     */
    public static double absoluteAverageDeviation(double[] arr) {
        double sum = 0;
        double mean = mean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.abs(arr[i] - mean);
        }
        return sum / arr.length;
    }

    /**
     * 求中位数绝对偏差(MAD)
     *
     * @param arr
     * @return
     */
    public static double medianAbsoluteDeviation(double[] arr) {
        double[] tempArr = new double[arr.length];
        double median = median(arr);
        for (int i = 0; i < arr.length; i++) {
            tempArr[i] = Math.abs(arr[i] - median);
        }
        return median(tempArr);
    }

    /**
     * 求标准差
     * @param arr
     * @return
     */
    public static double standardDevition(double[] arr) {
        double sum = 0;
        double mean = mean(arr);
        for (int i = 0; i < arr.length; i++) {
            sum += Math.sqrt((arr[i] - mean) * (arr[i] - mean));
        }
        return (sum / (arr.length - 1));
    }
    
    public static int[] permutation(double[] rk, int length) {
    	int[] p = new int[length];
    	List<RandKey> list = new LinkedList<>();
    	for (int i = 0; i < length; i++) {
    		list.add(new RandKey(rk[i], i));
    	}
    	Collections.sort(list);
    	for (int i = 0; i < list.size(); i++) {
    		p[i] = list.get(i).key;
        }
    	return p;
    }
    
    public static int[] randomPermutation(int length) {
    	int[] p = new int[length];
    	List<Integer> list = new ArrayList<>();
    	for (int i = 0; i < p.length; i++) {
    		list.add(i);
    	}
    	Collections.shuffle(list);
   		for (int i = 0; i < p.length; i++) {
   			p[i] = list.get(i);;
   		}
    	return p;
    }
    public static void swap(int[] array, int times) {
    	for (int i = 0; i < times; i++) {
			int x = rand.nextInt(array.length);
			int y = rand.nextInt(array.length);
			while (x == y) {
				y = rand.nextInt(array.length);
			}
			int temp = array[x];
			array[x] = array[y];
			array[y] = temp;
		}  
    }
    private static Random rand = new Random();
}

class RandKey implements Comparable<RandKey>{
	double value;
	int key;
	
	RandKey(double v, int k) {
		value = v;
		key = k;
	}
	@Override
	public int compareTo(RandKey arg) {
		// TODO Auto-generated method stub
		if (value - arg.value > 0) {
			return 1;
		} else if (value == arg.value) {
			return 0;
		} else {
			return -1;
		}
	}
	
	
}