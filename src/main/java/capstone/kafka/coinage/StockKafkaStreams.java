package capstone.kafka.coinage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

/**
 * This class handles Kafka topology, connecting input streams to output
 * streams.
 *
 * @author Sudhanshu Gupta
 */
public class StockKafkaStreams {

  // This object represents the created network topology.
  private final KafkaStreams stockStreams;

  public static void main(String[] args) {
    List<String> listStocks = new ArrayList<>();

    FileReader fileReader = null;
    try {
      String fileName = "D:\\programs\\java\\CoinAge-UI - Copy\\JSF_Login_Logout\\src\\main\\webapp\\resources\\text\\Stocks.txt";
      String line = "";

      fileReader = new FileReader(fileName);
      BufferedReader bufferedReader = new BufferedReader(fileReader);

      while ((line = bufferedReader.readLine()) != null) {
        listStocks.add(line.trim());
      }

      bufferedReader.close();
    } catch (FileNotFoundException ex) {
      Logger.getLogger(StockDataProducer.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      Logger.getLogger(StockDataProducer.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      try {
        fileReader.close();
      } catch (IOException ex) {
        Logger.getLogger(StockDataProducer.class.getName()).log(Level.SEVERE, null, ex);
      }
    }

    StockKafkaStreams stockStreams = new StockKafkaStreams(listStocks,
            Arrays.asList("INTRADAY", "DAILY", "WEEKLY", "MONTHLY"));
    stockStreams.run();
  }

  /**
   * Initialize a Kafka topology for the given 'symbols' and 'timeSeries'.
   *
   * @param symbols The symbols for which the topics will be connected.
   * @param timeSeries The time series' for which the topics will be connected.
   */
  public StockKafkaStreams(List<String> symbols, List<String> timeSeries) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stock-streams");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    // Build stream topology into a StreamsBuilder object
    final StreamsBuilder builder = new StreamsBuilder();
    for (String symbol : symbols) {
      for (String time : timeSeries) {
        builder.stream(symbol + "-" + time + "-INPUT").to(symbol + "-" + time + "-OUTPUT");
      }
    }

    // Initialize the Kafka streams
    final Topology topology = builder.build();
    stockStreams = new KafkaStreams(topology, props);
  }

  /**
   * Run the created streams.
   */
  public void run() {
    final CountDownLatch latch = new CountDownLatch(1);

    // Attach shutdown handler to catch control-c
    Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
      @Override
      public void run() {
        stockStreams.close();
        latch.countDown();
      }
    });

    // Run the streams until a shutdown is requested
    stockStreams.start();
    try {
      latch.await();
    } catch (InterruptedException ex) {
      Logger.getLogger(StockKafkaStreams.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
