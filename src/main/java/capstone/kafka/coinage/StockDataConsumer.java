package capstone.kafka.coinage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.io.FileWriter;  

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

  public static void main(String[] args) {
    List<String> stocks = Arrays.asList("MSFT", "GOOG");
    List<String> timeSeries = Arrays.asList("INTRADAY", "MONTHLY");
    StockDataConsumer testData = new StockDataConsumer(stocks, timeSeries);

    testData.runConsumerThread();

    List<Stock> s = testData.getStockValues("MSFT-INTRADAY");
    while (s.isEmpty()) {
      s = testData.getStockValues("MSFT-INTRADAY");
    }
    if (!s.isEmpty())
    {
    //UPDATE DATA
    }
    while(true){
 
    System.out.println(s.get(s.size() - 1).getValues().get("open"));
    System.out.println(s.get(s.size() - 1).getValues().get("low"));
    try{    
           FileWriter fw=new FileWriter("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values1.txt");    
           fw.write(s.get(s.size() - 1).getValues().get("open").toString());    
           fw.close();
           
          }catch(Exception e){System.out.println(e);}    
          System.out.println("Success...1");  
          
          try{    
           FileWriter fw=new FileWriter("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values2.txt");    
           fw.write(s.get(s.size() - 1).getValues().get("low").toString());    
           fw.close();
           
          }catch(Exception e){System.out.println(e);}    
          System.out.println("Success...2"); 
          
          try{    
           FileWriter fw=new FileWriter("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values3.txt");    
           fw.write(s.get(s.size() - 1).getValues().get("high").toString());    
           fw.close();
           
          }catch(Exception e){System.out.println(e);}    
          System.out.println("Success...3"); 
          
    try{    
           FileWriter fw=new FileWriter("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values4.txt");    
           fw.write(s.get(s.size() - 1).getValues().get("close").toString());    
           fw.close();
           
          }catch(Exception e){System.out.println(e);}    
          System.out.println("Success...4"); 
          
          try{    
           FileWriter fw=new FileWriter("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values5.txt");    
           fw.write(s.get(s.size() - 1).getValues().get("volume").toString());    
           fw.close();
           
          }catch(Exception e){System.out.println(e);}    
          System.out.println("Success...5"); 
          
         
          
          
    }
          
          
          
         
    
    
    //   testData.stopConsumerThread();
    
    
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
    consumerProperties.put("group.id", "stock-consumer");
    consumerProperties.put("enable.auto.commit", "true");
    consumerProperties.put("auto.commit.interval.ms", "1000");
    consumerProperties.put("session.timeout.ms", "30000");
    consumerProperties.put("key.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
    consumerProperties.put("value.deserializer",
            "org.apache.kafka.common.serialization.StringDeserializer");
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
      synchronized (stockValues) {
        stockValues.put(symbol, stockData);
      }
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
    List<Stock> returnValue;
    synchronized (stockValues) {
      returnValue = stockValues.getOrDefault(stockSymbol, new ArrayList<>());
    }
    return returnValue;
  }

}
