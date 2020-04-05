package utils;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class StatisticalOperationsUtility {
    public static RealMatrix getCorrelationMatrix(RealMatrix inputData) {
        PearsonsCorrelation obj = new PearsonsCorrelation();
        RealMatrix correlationMatrix = obj.computeCorrelationMatrix(inputData);


        return correlationMatrix;
    }

    public static void getCorrelation(RealMatrix correlationMatrix) {

    }
}
