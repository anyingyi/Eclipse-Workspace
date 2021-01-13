package util;

public class NormalizeUtils {

    public static double normalize(
            double value, double minRangeValue, double maxRangeValue, double min, double max) {

        return minRangeValue + (((value - min) * (maxRangeValue - minRangeValue)) / (max - min));
    }
}
