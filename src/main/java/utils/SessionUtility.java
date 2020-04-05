package utils;

import org.patriques.AlphaVantageConnector;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.timeseries.Daily;
import org.patriques.output.timeseries.data.StockData;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SessionUtility {

    static AlphaVantageConnector connect() {

        AlphaVantageConnector apiConnector = null;

        try {
            InputStream fileInputStream = SessionUtility.class.getResourceAsStream("session.properties");
            Properties properties = new Properties();

            properties.load(fileInputStream);
            String apiKey = properties.getProperty("source.apiKey");
            String timeOut = properties.getProperty("source.timeout");

            apiConnector = new AlphaVantageConnector(apiKey, Integer.parseInt(timeOut));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiConnector;
    }

    static void test() {
        AlphaVantageConnector apiConnector = connect();
        TimeSeries stockTimeSeries = new TimeSeries(apiConnector);

        try {
            Daily response = stockTimeSeries.daily("M&MFIN.NS", OutputSize.COMPACT);
            Map<String, String> metaData = response.getMetaData();

            System.out.println(metaData);
            System.out.println("Information: " + metaData.get("1. Information"));
            System.out.println("Stock: " + metaData.get("2. Symbol"));

            List<StockData> stockData = response.getStockData();
            stockData.forEach(stock -> {
                System.out.println("date:   " + stock.getDateTime());
                System.out.println("open:   " + stock.getOpen());
                System.out.println("high:   " + stock.getHigh());
                System.out.println("low:    " + stock.getLow());
                System.out.println("close:  " + stock.getClose());
                System.out.println("volume: " + stock.getVolume());
            });
        } catch (AlphaVantageException e) {
            e.printStackTrace();
        }
    }
}