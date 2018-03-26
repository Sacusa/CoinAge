package capstone.kafka.coinage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
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
  
  // Boolean to stop the background producer thread.
  private boolean runProducerThread;

  public static void main(String[] args) {
    StockDataProducer p = new StockDataProducer(Arrays.asList("MSFT", "GOOG"),
            Arrays.asList("INTRADAY", "MONTHLY"), 1, "XS1YCFU15GDN1O6T");
    
    p.runDemoThread();
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
    this.runProducerThread = false;
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
  
  public void runProducerThread() {
    runProducerThread = true;
    
    new Thread() {
      @Override
      public void run() {
        while (runProducerThread) {
          downloadStockData();
          pushStockData();
          System.out.println("Data updated.");
        }
      }
    }.start();
  }

  /**
   * Updates the value list for each stock symbol.
   */
  private void downloadStockData() {
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

        stockValues.put(symbol + "-" + time, apiResponse);
      }
    }
  }

  /**
   * Pushes the current stock data into Kafka.
   */
  private void pushStockData() {
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        String stock = symbol + "-" + time;
        kafkaProducer.send(new ProducerRecord<>(stock + "-INPUT", stock,
                stockValues.get(stock).toString()));
      }
    }
  }
  
  public void runDemoThread() {
    runProducerThread = true;
    
    new Thread() {
      @Override
      public void run() {
        while (runProducerThread) {
          prepareSampleData();
          pushStockData();
          System.out.println("Data updated.");
          try {
            Thread.sleep(2000);
          } catch (InterruptedException ex) {
            Logger.getLogger(StockDataProducer.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      }
    }.start();
  }
  
  private void prepareSampleData() {
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        List<Stock> sampleData = new ArrayList<>();
        
        for (int i = 0; i < 10; ++i) {
          GregorianCalendar gc = null;
          if (time.equals("INTRADAY")) {
            gc = new GregorianCalendar(2018, 3, 28, i, i);
          }
          else {
            gc = new GregorianCalendar(2018, 3, 28);
          }
          
          Random r = new Random();
          Map<String, Double> v = new HashMap<>();
          v.put("open", r.nextDouble());
          v.put("close", r.nextDouble());
          v.put("high", r.nextDouble());
          v.put("low", r.nextDouble());
          v.put("volume", r.nextDouble());
          
          Stock s = new Stock(symbol, gc, v);
          sampleData.add(s);
        }
        
        stockValues.put(symbol + "-" + time, sampleData);
      }
    }
  }
  
  public void stopProducerThread() {
    runProducerThread = false;
  }
}
