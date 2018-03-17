/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone.kafka.coinage;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsException;

/**
 *
 * @author Sudhanshu Gupta
 */
public class StockKafkaStreams {
  public static void main(String[] args) {
    StockKafkaStreams.initStreams(Arrays.asList("MSFT", "GOOG"), Arrays.asList("MONTHLY"));
  }
  
  public static void initStreams(List<String> symbols, List<String> timeSeries) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stock-streams");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    final StreamsBuilder builder = new StreamsBuilder();

    for (String symbol : symbols) {
      for (String time: timeSeries) {
        builder.stream(symbol + "_" + time + "_INPUT").to(symbol + "_" + time + "_OUTPUT");
      }
    }

    final Topology topology = builder.build();

    final KafkaStreams streams = new KafkaStreams(topology, props);
    final CountDownLatch latch = new CountDownLatch(1);

    // attach shutdown handler to catch control-c
    Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
      @Override
      public void run() {
        streams.close();
        latch.countDown();
      }
    });
    
    System.out.println("Setup complete");

    try {
      streams.start();
      latch.await();
    } catch (IllegalStateException | InterruptedException | StreamsException e) {
      System.out.println("ERROR");
      System.exit(1);
    }
    
    System.exit(0);
  }
}
