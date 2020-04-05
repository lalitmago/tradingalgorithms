import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import utils.DataUtility;
import utils.StatisticalOperationsUtility;
import utils.StockOperationsUtility;

import java.io.*;
import java.util.*;

public class Main {

    //private static LoadingCache<>

    public static void main(String[] args) {

        //File directory = new File("C:\\Users\\User\\Documents\\Trading And Investment\\Data\\Data-Futures");
        File directory = new File("/home/lalit-mago/Documents/Trading And Investment/Data/Data-Futures");
        File[] listOfFiles = directory.listFiles();
        Map<String, Long> data = new HashMap<>();
        Multimap<String, Double> stocksAndClosingPrices = MultimapBuilder.treeKeys().arrayListValues().build();

        if (listOfFiles != null)
            Arrays.stream(listOfFiles).forEach(file -> data.put(file.getName(), getRowCount(file)));
        System.out.println(data.size());
        data.forEach((key, value) -> {
            //System.out.println("Key : " + key + ", Row Count: " + value);
            //System.out.println();
            stocksAndClosingPrices.putAll(key.split("\\.")[0], DataUtility.getClosingPricesFromFiles(key));
        });


        RealMatrix stocksClosingPricesMatrix = DataUtility.convertMultimapToMatrix(stocksAndClosingPrices);
        System.out.println("===Stocks Closing Prices Matrix===");
        System.out.println(Arrays.deepToString(stocksClosingPricesMatrix.getData()));

        Multimap<String, Double> stocksAndReturns = MultimapBuilder.treeKeys().arrayListValues().build();
        stocksAndClosingPrices.asMap().forEach((stock, closingPrices) -> {
            List<Double> eachStockReturns = StockOperationsUtility.calculateStockReturnsFromCLosingPrices((List<Double>) closingPrices);
            stocksAndReturns.putAll(stock, eachStockReturns);
        });

        RealMatrix stocksReturnsMatrix = DataUtility.convertMultimapToMatrix(stocksAndReturns);
        System.out.println("===Stocks Returns Matrix===");
        System.out.println(Arrays.deepToString(stocksReturnsMatrix.getData()));

        System.out.println("Correlation using stock prices");
        RealMatrix correlationMatrixStockPrice = StatisticalOperationsUtility.getCorrelationMatrix(stocksClosingPricesMatrix);
        System.out.println("Rows : " + correlationMatrixStockPrice.getRowDimension() + ", Columns : " + correlationMatrixStockPrice.getColumnDimension());
        System.out.println(Arrays.deepToString(correlationMatrixStockPrice.getData()));
        System.out.println();

        System.out.println("Correlation using stock returns");
        RealMatrix correlationMatrixStockReturns = StatisticalOperationsUtility.getCorrelationMatrix(stocksReturnsMatrix);
        System.out.println("Rows : " + correlationMatrixStockReturns.getRowDimension() + ", Column : " + correlationMatrixStockReturns.getColumnDimension());
        System.out.println(Arrays.deepToString(correlationMatrixStockReturns.getData()));


    }

    private static long getRowCount(File file) {
        long count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            count = br.lines().count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    // TODO: 08/10/2019 - Build the cache for closing prices 
    private static void buildCache(Multimap<String, Double> obj) {
        CacheLoader<String, List<Double>> loader;
        loader = new CacheLoader<String, List<Double>>() {
            @Override
            public List<Double> load(String s) throws Exception {
                return null;
            }
        };
    }

}

/*

    DataUtility obj = new DataUtility();
        obj.prepareData();

//test();
//String contents = DataUtility.extractAndPrepareData();
//DataUtility.writeData("C:/Users/User/Documents/Trading And Investment/Data/TataMotors.csv", contents);*/
