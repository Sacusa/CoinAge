package capstone.kafka.coinage;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * Consumes stock data from Kafka and restores it in the order created by the producer.
 *
 * @author Sudhanshu Gupta
 */
public class StockDataConsumer {

  // Consumer object to interface with Kafka.
  private final KafkaConsumer<String, String> kafkaConsumer;

  // List of symbols for which to consume data.
  private final List<String> symbols;

  // Map from symbols to an ordered list of values.
  private final Map<String, List<Stock>> stockValues;

  // Boolean to stop the background consumer thread.
  private boolean runConsumerThread;
  
  private static long consumerCount = 0;

  public static void main(String[] args) {
    List<String> stocks = Arrays.asList("MSFT", "GOOG");
    List<String> timeSeries = Arrays.asList("INTRADAY", "MONTHLY");
    StockDataConsumer t1 = new StockDataConsumer(stocks, timeSeries);
    StockDataConsumer t2 = new StockDataConsumer(stocks, timeSeries);
    StockDataConsumer t3 = new StockDataConsumer(stocks, timeSeries);

    t1.runConsumerThread();
    t2.runConsumerThread();
    t3.runConsumerThread();
    

    while (true) {
      List<Stock> s;
      
      s = t1.getStockValues("MSFT-INTRADAY");
      while (s.isEmpty()) {
        s = t1.getStockValues("MSFT-INTRADAY");
      }
      
      s = t2.getStockValues("MSFT-INTRADAY");
      while (s.isEmpty()) {
        s = t2.getStockValues("MSFT-INTRADAY");
      }
      
      s = t3.getStockValues("MSFT-INTRADAY");
      while (s.isEmpty()) {
        s = t3.getStockValues("MSFT-INTRADAY");
      }
      
      System.out.println("OK");
      
      try {
        Thread.sleep(500);
      } catch (InterruptedException ex) {
        Logger.getLogger(StockDataConsumer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  /**
   * Create a Kafka consumer to consume data for given 'symbols' and 'timeSeries'.
   *
   * @param symbols List of symbols for which to consume data.
   * @param timeSeries List of time series for which to consume data.
   */
  public StockDataConsumer(List<String> symbols, List<String> timeSeries) {
    this.kafkaConsumer = getKafkaConsumer();
    this.symbols = new ArrayList<>();
    this.stockValues = new ConcurrentHashMap<>();
    this.runConsumerThread = false;

    // Subscribe to the required topics
    List<String> topics = new ArrayList<>();
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        String symbolAndTime = symbol + "-" + time;
        this.symbols.add(symbolAndTime);
        topics.add(symbolAndTime + "-OUTPUT");
      }
    }
    kafkaConsumer.subscribe(topics);
  }

  /**
   * Connect to Kafka and return a Consumer object to interface with it.
   *
   * @return A KafkaConsumer object to interface with Kafka.
   */
  private KafkaConsumer getKafkaConsumer() {
    Properties consumerProperties = new Properties();
    consumerProperties.put("bootstrap.servers", "localhost:9092");
    consumerProperties.put("group.id", "stock-consumer-" + StockDataConsumer.consumerCount);
    consumerProperties.put("enable.auto.commit", "true");
    consumerProperties.put("auto.commit.interval.ms", "8000");
    consumerProperties.put("session.timeout.ms", "30000");
    consumerProperties.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    consumerProperties.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    
    System.out.println("Consumer: " + StockDataConsumer.consumerCount);
    StockDataConsumer.consumerCount++;
    
    return new KafkaConsumer<>(consumerProperties);
  }

  public void runConsumerThread() {
    runConsumerThread = true;

    new Thread() {
      @Override
      public void run() {
        while (runConsumerThread) {
          pullStockData();
        }
      }
    }.start();
  }

  /**
   * Pulls latest stock data for all the symbols and time series.
   */
  private void pullStockData() {
    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);

//    int recordCount = records.count();
//    if (recordCount != 0) {
//      System.out.println("Pulled size = " + recordCount);
//    }
    for (ConsumerRecord<String, String> record : records) {
      //System.out.println("record: " + record);
      String symbol = record.key();

      List<Stock> stockData = new ArrayList<>();

      // Sequentially process each stock entry
      String recordValue = record.value().substring(1, record.value().length() - 1);
      for (String dataEntry : recordValue.split(",")) {

        // Parse data entry as-is into a map
        Map<String, String> rawStockData = new HashMap<>();
        String[] fields = dataEntry.split(";");
        for (String field : fields) {
          String[] values = field.split(":");
          rawStockData.put(values[0], values[1]);
        }

        // Extract and parse date and time info
        GregorianCalendar stockDateTime = new GregorianCalendar();
        stockDateTime.setTimeInMillis(new Long(rawStockData.get("timems")));

        // Extract and parse stock values
        Map<String, Double> stockDataValues = new HashMap<>();
        stockDataValues.put("open", new Double(rawStockData.get("open")));
        stockDataValues.put("high", new Double(rawStockData.get("high")));
        stockDataValues.put("low", new Double(rawStockData.get("low")));
        stockDataValues.put("close", new Double(rawStockData.get("close")));
        stockDataValues.put("volume", new Double(rawStockData.get("volume")));

        // Add new data into the list
        stockData.add(new Stock(symbol, stockDateTime, stockDataValues));
      }

      // Update stock values
      stockValues.put(symbol, stockData);
    }
  }

  public void stopConsumerThread() {
    runConsumerThread = false;
  }

  /**
   * Returns the list of stock values for the provided symbol.
   *
   * @param stockSymbol The stock symbol to return the values for.
   * @return A List&lt;Stock&lt; object containing the stock values in increasing time instance.
   */
  public List<Stock> getStockValues(String stockSymbol) {
    return stockValues.getOrDefault(stockSymbol, new ArrayList<Stock>());
  }

}
