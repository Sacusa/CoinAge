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
    <%
      List<String> stocks = Arrays.asList(request.getParameter("name"));
      StockDataConsumer consumer = new StockDataConsumer(stocks,
              Arrays.asList("INTRADAY", "DAILY", "WEEKLY", "MONTHLY"));
      consumer.runConsumerThread();
      String time = request.getParameter("time").toUpperCase();
      
      String symbol = stocks.get(0) + "-" + time;
      List<Stock> stockData = consumer.getStockValues(symbol);
      while (stockData.isEmpty()) {
        stockData = consumer.getStockValues(symbol);
      }
    %>
    <script type="text/javascript">
      google.charts.load('current', {'packages': ['corechart']});
      google.charts.setOnLoadCallback(drawChart);

      function drawChart() {
        var data = google.visualization.arrayToDataTable([
          ['Time', 'Open', 'Close', 'High', 'Low', 'Volume'],
          <%
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
          curveType: 'line',
          legend: {position: 'bottom'}
        };

        var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));

        chart.draw(data, options);
      }
    </script>
  </head>
  <body>
      <table class="table table-striped">
      <tr>
        <th>Open</th>
        <td><%= stockValues.get("open") %></td>
        <td rowspan="5" id="curve_chart" style="width: 900px; height: 500px"></td>
      </tr>
      <tr>
        <th>Close</th>
        <td><%= stockValues.get("close") %></td>
      </tr>
      <tr>
        <th>High</th>
        <td><%= stockValues.get("high") %></td>
      </tr>
      <tr>
        <th>Low</th>
        <td><%= stockValues.get("low") %></td>
      </tr>
      <tr>
        <th>Volume</th>
        <td><%= stockValues.get("volume") %></td>
      </tr>
    </table>
  </body>
</html>
