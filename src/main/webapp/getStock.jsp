
<%@page import="java.util.Map"%>
<%@page import="capstone.kafka.coinage.Stock"%>
<%@page import="capstone.kafka.coinage.StockDataConsumer"%>
<%@page import="java.util.Arrays"%>
<%@page import="org.apache.kafka.clients.consumer.ConsumerRecord"%>
<%@page import="org.apache.kafka.clients.consumer.ConsumerRecords"%>
<%@page import="org.apache.kafka.clients.consumer.Consumer"%>
<%@page import="org.apache.kafka.clients.consumer.KafkaConsumer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.kafka.clients.producer.ProducerRecord"%>
<%@page import="org.apache.kafka.clients.producer.KafkaProducer"%>
<%@page import="java.util.Properties"%>
<%@page import="org.apache.kafka.clients.producer.Producer"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
  <head>
       <%
      String userid = (String) session.getAttribute("username");
      if (userid == null) {
        String redirectURL = "login.xhtml";
        response.sendRedirect(redirectURL);
      }
    %>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  </head>
  <body>
    <%!
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
        return new KafkaProducer<String,String>(producerProperties);
      }

      private Consumer getKafkaConsumer() {
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
        return new KafkaConsumer<String,String>(consumerProperties);
      }

      private void sendRequest(Producer kafkaProducer, String username) {
        kafkaProducer.send(new ProducerRecord("db-query", username, "stocks;" + username));
      }

      private String getResponse(Consumer kafkaConsumer, String username, String name) {
        String response = name;
        boolean respondReceived = false;

        /*
        while (!respondReceived) {
          ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
          while (records.isEmpty()) {
            records = kafkaConsumer.poll(100);
          }

          for (ConsumerRecord<String, String> record : records) {
            if (record.key().equals(username)) {
              response = record.value();
              respondReceived = true;
              break;
            }
          }
        }*/

        return response;
      }

      private String getStocksString(String username , String stock) {
        Producer kafkaProducer = getKafkaProducer();
        Consumer kafkaConsumer = getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("db-response"));
        sendRequest(kafkaProducer, username);
        return getResponse(kafkaConsumer, username, stock);
      }

      private List<String> getStocks(String username , String stock) {
        return Arrays.asList(getStocksString(username,stock).split(","));
      }
    %>

    <%
      String username = (String) session.getAttribute("username");
      List<String> stocks = getStocks(username,request.getParameter("name"));
      StockDataConsumer consumer = new StockDataConsumer(stocks,
              Arrays.asList("INTRADAY", "DAILY", "WEEKLY", "MONTHLY"));
      consumer.runConsumerThread();
      String time = request.getParameter("time").toUpperCase();
      System.out.println(username + "; " + stocks + "; " + time);
    %>
    
    <table class="table table-striped">
      <tr>
        <th>Stock</th>
        <th>Open</th>
        <th>Close</th>
        <th>High</th>
        <th>Low</th>
        <th>Volume</th>
      </tr>
      <%
        for (String stock : stocks) {
          String symbol = stock + "-" + time;
          List<Stock> stockData = consumer.getStockValues(symbol);
          while (stockData.isEmpty()) {
            stockData = consumer.getStockValues(symbol);
          }
          Map<String, Double> latestStockValues = stockData.get(stockData.size() - 1).getValues();
      %>
      <tr>
        <td><%= stock %></td>
        <td><%= latestStockValues.get("open") %></td>
        <td><%= latestStockValues.get("close") %></td>
        <td><%= latestStockValues.get("high") %></td>
        <td><%= latestStockValues.get("low") %></td>
        <td><%= latestStockValues.get("volume") %></td>
      </tr>
      <%
        }
      %>
    </table>
  </body>
</html>
