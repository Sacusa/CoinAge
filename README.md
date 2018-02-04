# CoinAge

__TODO__: Describe the application.

## Contents

__TODO__: Add contents after the document is ready.

## 1. Data Producer

The data producer fetches real-time data using Alpha Vantage API. The data is then pushed onto Apache Kafka.

The information about an indivdual stock at a particular time instance is stored in a class Stock, described as follows:

    class Stock {
        private final String symbol;
        private final String dateTime;
        private final Map<String, Double> values;

        public Stock (String symbol, String dateTime, Double openValue, Double highValue, Double lowValue, Double closeValue, double volume);
        public Map<String, Double> getValues ();
        public String getSymbol ();
        public String getDateTime ()
    }

All the functionality is encapsulated in a class called Producer, described as follows:

    class Producer {
        private List<String> symbols;
        private Map<String, List<Stock>> stockValues;
        private final String apiKey;

        public Producer (List<String> symbols, String apiKey);
        public void update ();
        public List<Stock> getStockValues (String stockSymbol);
    }
