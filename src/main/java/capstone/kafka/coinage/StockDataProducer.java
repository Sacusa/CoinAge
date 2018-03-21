package capstone.kafka.coinage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This class gathers stock data for the required stock symbols. The data is then stored in an
 * ordered fashion for easy manipulation.
 *
 * @author Sudhanshu Gupta
 */
public class StockDataProducer {

  // Producer to push values to Kafka.
  private final Producer<String, String> kafkaProducer;

  // List of symbols to store information about.
  private final List<String> symbols;

  // List of stock values at increasing time intervals for each stock.
  private final Map<String, List<Stock>> stockValues;

  // List of supported time series, as described in AlphaVantage API.
  private final List<String> timeSeries;

  // Time interval between each stock value.
  private final Integer interval;

  // API key for AlphaVantage API.
  private final String apiKey;

  public static void main(String[] args) {
    StockDataProducer p = new StockDataProducer(Arrays.asList("MSFT", "GOOG"),
            Arrays.asList("INTRADAY", "MONTHLY"), 1, "XS1YCFU15GDN1O6T");
    p.downloadStockData();

    System.out.println("==== MSFT (INTRADAY) ====");
    System.out.println(p.getStockValues("MSFT_INTRADAY").size());
    System.out.println("==== MSFT (MONTHLY) ====");
    System.out.println(p.getStockValues("MSFT_MONTHLY").size());

    System.out.println("==== GOOG (INTRADAY) ====");
    System.out.println(p.getStockValues("GOOG_INTRADAY").size());
    System.out.println("==== GOOG (MONTHLY) ====");
    System.out.println(p.getStockValues("GOOG_MONTHLY").size());
    
    p.pushStockData();
    System.out.println("Data sent");
  }

  /**
   * Initialize a StockDataProducer object.
   *
   * @param symbols List of stock symbols to store information about.
   * @param timeSeries The time series to store information for (INTRADAILY,DAILY,MONTHLY,YEARLY).
   * @param interval Time interval (in minutes) between each stock value.
   * @param apiKey API key for AlphaVantage API.
   */
  public StockDataProducer(List<String> symbols, List<String> timeSeries, Integer interval,
          String apiKey) {
    this.kafkaProducer = getKafkaProducer();
    this.symbols = new ArrayList<>(symbols);
    this.stockValues = new HashMap<>();
    this.timeSeries = new ArrayList<>(timeSeries);
    this.interval = interval;
    this.apiKey = apiKey;
  }
  
  /**
   * Returns an instance of KafkaProducer which is suitable for pushing data onto the Kafka
   * instance running on localhost.
   * 
   * @return a KafkaProducer object for Kafka running on localhost and using String serde.
   */
  private Producer getKafkaProducer() {
    Properties producerProperties = new Properties();
    producerProperties.put("bootstrap.servers", "localhost:9092");
    producerProperties.put("acks", "all");
    producerProperties.put("retries", 0);
    producerProperties.put("batch.size", 16384);
    producerProperties.put("linger.ms", 1);
    producerProperties.put("buffer.memory", 33554432);
    producerProperties.put("key.serializer",
            "org.apache.kafka.common.serialization.StringSerializer");
    producerProperties.put("value.serializer",
            "org.apache.kafka.common.serialization.StringSerializer");
    return new KafkaProducer<>(producerProperties);
  }

  /**
   * Updates the value list for each stock symbol.
   */
  public void downloadStockData() {
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        // prepare the AlphaVantage API query string
        String apiQuery = "https://www.alphavantage.co/query?function="
                + "TIME_SERIES_" + time
                + "&symbol=" + symbol
                + "&apikey=" + apiKey;

        if (time.equals("INTRADAY")) {
          // add time interval for intraday results
          apiQuery += "&interval=" + interval + "min";
        } else {
          // for all other results, request compact data (last 100 data points)
          apiQuery += "&outputsize=compact";
        }

        // request and parse the stock data
        List<Stock> apiResponse = null;
        try {
          apiResponse = new JsonParser().getLatestStock(apiQuery, symbol);
        } catch (IOException ex) {
          Logger.getLogger(StockDataProducer.class.getName()).log(Level.SEVERE, null, ex);
        }

        stockValues.put(symbol + "_" + time, apiResponse);
      }
    }
  }

  /**
   * Pushes the current stock data into Kafka.
   */
  public void pushStockData() {
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        String stock = symbol + "_" + time;
        kafkaProducer.send(new ProducerRecord<>(stock + "_INPUT",
                stockValues.get(stock).toString()));
      }
    }
  }

  /**
   * Returns the list of stock values for the provided symbol.
   *
   * @param stockSymbol The stock symbol to return the values for.
   * @return A List&lt;Stock&lt; object containing the stock values in increasing time instance.
   */
  public List<Stock> getStockValues(String stockSymbol) {
    return stockValues.getOrDefault(stockSymbol, new ArrayList<>());
  }
}
