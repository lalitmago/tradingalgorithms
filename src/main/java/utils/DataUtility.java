package utils;

import com.google.common.collect.Multimap;
import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.DailyAdjusted;
import org.patriques.output.timeseries.data.StockData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataUtility {

    //private static String baseLocation = "C:/Users/User/Documents/Trading And Investment/Data/Data-Futures/";
    private static String baseLocation = "/home/lalit-mago/Documents/Trading And Investment/Data/Data-Futures/";

    public void prepareData() {
        File symbolsFile = new File("configs/nse-futures-symbols.txt");
        String stockData;
        int i = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(symbolsFile));

            String stockSymbol;
            while ((stockSymbol = reader.readLine()) != null) {
                stockData = extractData(stockSymbol);
                writeData(baseLocation + stockSymbol.split(":")[1] + ".csv", stockData);
                System.out.println("Data for " + stockSymbol + " written...!");
                i++;
                if (i % 5 == 0)
                    Thread.sleep(60000);
            }
            System.out.println("All done! Data for all NSE-MIS symbols extracted...!");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String extractData(String stockSymbol) {

        AlphaVantageConnector connector = SessionUtility.connect();
        TimeSeries stockTimeSeries = new TimeSeries(connector);

        StringBuilder contents = new StringBuilder("");

        try {
            DailyAdjusted response = stockTimeSeries.dailyAdjusted(stockSymbol, OutputSize.COMPACT);

            List<StockData> stockData = response.getStockData();

            contents.append("Date,Open,High,Low,Close,AdjustedClose,Volume");
            contents.append(System.lineSeparator());

            stockData.forEach(stock -> {
                contents.append(stock.getDateTime());
                contents.append(",");
                contents.append(stock.getOpen());
                contents.append(",");
                contents.append(stock.getHigh());
                contents.append(",");
                contents.append(stock.getLow());
                contents.append(",");
                contents.append(stock.getClose());
                contents.append(",");
                contents.append(stock.getAdjustedClose());
                contents.append(",");
                contents.append(stock.getVolume());
                contents.append(System.lineSeparator());
            });
        } catch (AlphaVantageException e) {
            e.printStackTrace();
        }
        return contents.toString();
    }

    private static void writeData(String filename, String contents) {
        File file = new File(filename);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.append(contents);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Double> getClosingPricesFromFiles(String eachStockDataFile) {

        List<Double> closingPrices = new ArrayList<>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(baseLocation + eachStockDataFile)));
            String line;
            while ((line = br.readLine()) != null) {
                count++;
                if (count >= 2) {
                    closingPrices.add(Double.parseDouble(line.split(",")[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return closingPrices;
    }

    public static RealMatrix convertMultimapToMatrix(Multimap<String, Double> stringDoubleMultimap) {

        int rows = stringDoubleMultimap.keySet().size();
        int columns = stringDoubleMultimap.entries().size() / stringDoubleMultimap.keySet().size();
        RealMatrix data = new BlockRealMatrix(rows, columns);
        AtomicInteger columnIndex = new AtomicInteger(0);
        stringDoubleMultimap.asMap().forEach((key, value) -> {
            double[] temp = value.stream().mapToDouble(Double::doubleValue).toArray();
            data.setRow(columnIndex.getAndIncrement(), temp);
        });
        return data.transpose();
    }

    public static void printArray(double[][] input) {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[0].length; j++) {
                System.out.print(input[i][j] + "   ");
            }
            System.out.println();
        }
    }
}


//IntraDay response = stockTimeSeries.intraDay("MSFT", Interval.ONE_MIN, OutputSize.COMPACT);
//IntraDay response = stockTimeSeries.intraDay("NSE:TATMOTORS", Interval.ONE_MIN, OutputSize.COMPACT);


/*System.out.println("Information: " + metaData.get("1. Information"));
            System.out.println("Stock: " + metaData.get("2. Symbol"));*/