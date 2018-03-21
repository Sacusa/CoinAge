package capstone.kafka.coinage;

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
 * This class handles Kafka topology, connecting input streams to output streams.
 * 
 * @author Sudhanshu Gupta
 */
public class StockKafkaStreams {
  
  // This object represents the created network topology.
  private final KafkaStreams stockStreams;
  
  public static void main(String[] args) {
    StockKafkaStreams stockStreams = new StockKafkaStreams(Arrays.asList("MSFT, GOOG"),
            Arrays.asList("MONTHLY"));
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
      for (String time: timeSeries) {
        builder.stream(symbol + "_" + time + "_INPUT").to(symbol + "_" + time + "_OUTPUT");
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
