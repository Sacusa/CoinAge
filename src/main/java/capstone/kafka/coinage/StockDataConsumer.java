package capstone.kafka.coinage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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
  
  public static void main(String[] args) {
    StockDataConsumer intraday = new StockDataConsumer(Arrays.asList("MSFT", "GOOG"),
            Arrays.asList("INTRADAY"));
    StockDataConsumer monthly = new StockDataConsumer(Arrays.asList("MSFT", "GOOG"),
            Arrays.asList("MONTHLY"));
    
    intraday.pullStockData();
    
    List<Stock> msft_intraday = new ArrayList<>();
    while (msft_intraday.isEmpty()) {
      intraday.pullStockData();
      msft_intraday = intraday.getStockValues("MSFT_INTRADAY");
    }
    System.out.println("==== MSFT (INTRADAY) ====");
    for (Stock s : msft_intraday) {
      System.out.println(s);
    }
    
    System.out.println("==== MSFT (MONTHLY) ====");
    for (Stock s : monthly.getStockValues("MSFT_MONTHLY")) {
      System.out.println(s);
    }
    
    System.out.println("==== GOOG (INTRADAY) ====");
    for (Stock s : intraday.getStockValues("GOOG_INTRADAY")) {
      System.out.println(s);
    }
    
    System.out.println("==== GOOG (MONTHLY) ====");
    for (Stock s : monthly.getStockValues("GOOG_MONTHLY")) {
      System.out.println(s);
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
    this.stockValues = new HashMap<>();

    // Subscribe to the required topics
    List<String> topics = new ArrayList<>();
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        String symbolAndTime = symbol + "_" + time;
        this.symbols.add(symbolAndTime);
        topics.add(symbolAndTime + "_OUTPUT");
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
    consumerProperties.put("group.id", "test");
    consumerProperties.put("enable.auto.commit", "true");
    consumerProperties.put("auto.commit.interval.ms", "1000");
    consumerProperties.put("session.timeout.ms", "30000");
    consumerProperties.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    consumerProperties.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    return new KafkaConsumer<>(consumerProperties);
  }

  /**
   * Pulls latest stock data for all the symbols and time series.
   */
  public void pullStockData() {
    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
    System.out.println("Pulling data ...");

    for (ConsumerRecord<String, String> record : records) {
      System.out.println("record: " + record);
      String symbol = record.key();
      String time = symbol.split("_")[1];

      // Construct a date parser according to the time series in the key
      String rawDateFormat = "yyyy-mm-dd";
      if (time.equals("INTRADAY")) {
        rawDateFormat += "'T'HH:mm:ss";
      }
      DateFormat dateFormat = new SimpleDateFormat(rawDateFormat);

      // Clear old data
      List<Stock> stockData;
      if (stockValues.containsKey(symbol)) {
        stockData = stockValues.get(symbol);
      } else {
        stockData = new ArrayList<>();
        stockValues.put(symbol, stockData);
      }

      // Sequentially process each stock entry
      for (String dataEntry : record.value().split(",")) {

        // Parse data entry as-is into a map
        Map<String, String> rawStockData = new HashMap<>();
        String[] fields = dataEntry.split(";");
        for (String field : fields) {
          String[] values = field.split(":");
          rawStockData.put(values[0], values[1]);
        }
        
        // Extract and parse date and time info
        String rawDateTime = rawStockData.get("year")
                + "-" + rawStockData.get("month")
                + "-" + rawStockData.get("day");
        if (time.equals("INTRADAY")) {
          rawDateTime += "T" + rawStockData.get("hour")
                  + ":" + rawStockData.get("minute")
                  + ":" + rawStockData.get("second");
        }
        
        Date dateTime = null;
        try {
          dateTime = dateFormat.parse(rawDateTime);
        } catch (ParseException ex) {
          Logger.getLogger(StockDataConsumer.class.getName()).log(Level.SEVERE, null, ex);
        }
        GregorianCalendar stockDateTime = new GregorianCalendar();
        stockDateTime.setTime(dateTime);
        
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
