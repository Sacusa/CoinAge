<%-- 
    Document   : DisplayTable
    Created on : Apr 19, 2018, 10:56:58 PM
    Author     : Sudhanshu Gupta
--%>

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
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
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
        return new KafkaProducer<String, String>(producerProperties);
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
        return new KafkaConsumer<String, String>(consumerProperties);
      }

      private void sendRequest(Producer kafkaProducer, String username) {
        kafkaProducer.send(new ProducerRecord("db-query", username, "stocks;" + username));
      }

      private String getResponse(Consumer kafkaConsumer, String username) {
        String response = "MSFT,GOOG";
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

      private String getStocksString(String username) {
        Producer kafkaProducer = getKafkaProducer();
        Consumer kafkaConsumer = getKafkaConsumer();
        kafkaConsumer.subscribe(Arrays.asList("db-response"));
        sendRequest(kafkaProducer, username);
        return getResponse(kafkaConsumer, username);
      }

      private List<String> getStocks(String username) {
        return Arrays.asList(getStocksString(username).split(","));
      }
    %>

    <%
      //String username = (String) session.getAttribute("username");
      String username = "mama";
      List<String> stocks = getStocks(username);
      StockDataConsumer consumer = new StockDataConsumer(stocks,
              Arrays.asList("INTRADAY", "DAILY", "WEEKLY", "MONTHLY"));
      consumer.runConsumerThread();
      String time = request.getParameter("time").toUpperCase();
    %>
    <script type="text/javascript">
      google.charts.load('current', {'packages': ['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Time', 'Open', 'Close', 'High', 'Low', 'Volume'],
          <%
            String symbol = stocks.get(0) + "-" + time;
            List<Stock> stockData = consumer.getStockValues(symbol);
            while (stockData.isEmpty()) {
              stockData = consumer.getStockValues(symbol);
            }

            int iterEnd = stockData.size() - 1, index = 0;
            for (; index < iterEnd; index++) {
              Map<String, Double> stockValues = stockData.get(index).getValues();
          %>
          [
            <%= index %>,
            parseFloat(<%= stockValues.get("open") %>),
            parseFloat(<%= stockValues.get("close") %>),
            parseFloat(<%= stockValues.get("high") %>),
            parseFloat(<%= stockValues.get("low") %>),
            parseFloat(<%= stockValues.get("volume") %>)
          ],
          <%
            }
            Map<String, Double> stockValues = stockData.get(index).getValues();
          %>
          [
            <%= index %>,
            parseFloat(<%= stockValues.get("open") %>),
            parseFloat(<%= stockValues.get("close") %>),
            parseFloat(<%= stockValues.get("high") %>),
            parseFloat(<%= stockValues.get("low") %>),
            parseFloat(<%= stockValues.get("volume") %>)
          ]
        ]);

        var options = {
          title: 'Company Performance',
          curveType: 'line',
          legend: {position: 'bottom'}
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
    <div id="curve_chart" style="width: 900px; height: 500px"></div>
  </body>
</html>
