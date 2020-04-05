package utils;

import org.apache.commons.math3.util.FastMath;

import java.util.ArrayList;
import java.util.List;

public class StockOperationsUtility {

    public static List<Double> calculateChangesInClosingPrices(List<Double> closingPrices) {

        List<Double> changesInClosingPrices = new ArrayList<>();
        int n = closingPrices.size();
        for (int t = 0; t < n - 1; t++)
            changesInClosingPrices.add(closingPrices.get(t) - closingPrices.get(t + 1));
        return changesInClosingPrices;
    }

    private static List<Double> calculateSpreadForTwoStocks(List<Double> closingPricesChangeStockOne, List<Double> closingPricesChangeStockTwo) {

        List<Double> spread = new ArrayList<>();
        if (closingPricesChangeStockOne.size() != closingPricesChangeStockTwo.size())
            return null;
        else {
            for (int i = 0; i < closingPricesChangeStockOne.size(); i++)
                spread.add(closingPricesChangeStockOne.get(i) - closingPricesChangeStockTwo.get(i));
            return spread;
        }
    }

    public static List<Double> calculateStockReturnsFromCLosingPrices(List<Double> closingPrices) {
        List<Double> stockReturns = new ArrayList<>();
        int n = closingPrices.size();
        for (int t = 0; t < n - 1; t++)
            stockReturns.add(FastMath.log(closingPrices.get(t) / closingPrices.get(t + 1)));
        return stockReturns;
    }


}
