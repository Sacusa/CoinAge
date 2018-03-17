package capstone.kafka.coinage;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The information about an individual stock at a particular time instance is stored in this class.
 * Objects of this class are immutable. Consequently, only getter methods are present.
 * 
 * @author tanmaypatil
 */
public class Stock  {
  /** symbol contains the stock symbol. For example, Microsoft symbol will be "MSFT". */
  private final String symbol;
  
  /** dateTime represents the date and time the object contains information for. */
  private final GregorianCalendar dateTime;
  
  /** values contains the detailed information about the stock. */
  private final Map<String, Double> values;
  
  /**
   * Construct a Stock object.
   * 
   * @param symbol   The symbol the object corresponds to.
   * @param dateTime The date and time the object represents.
   * @param values   The stock values the object needs to contain. The information includes
   *                 openValue, highValue, lowValue, closeValue and volume, each of which should
   *                 be accessible using the strings "open", "high", "low", "close" and "volume"
   *                 respectively.
   */
  public Stock(String symbol, GregorianCalendar dateTime, Map<String, Double> values) {
    this.symbol = symbol;
    this.dateTime = (GregorianCalendar) dateTime.clone();
    this.values = new HashMap<>();

    // load stock value from 'values' if it contains the key. Else, set that value to 0.
    List<String> keys = Arrays.asList("open", "high", "low", "close", "volume");
    
    for (String key : keys) {
      if (values.containsKey(key)) {
        this.values.put(key, values.get(key));
      } else {
        this.values.put(key, 0d);
      }
    }
  }
  
  /**
   * Returns a HashMap which contains a detailed information about the stock, like openValue,
   * highValue, lowValue, closeValue and volume, each of which can be referred to using the string
   * "open", "high", "low", "close" and "volume" respectively.
   * 
   * @return values
   */
  public Map<String, Double> getValues() {
    return new HashMap<>(values);
  }

  /**
   * Returns a String which contains the stock symbol.
   * 
   * @return symbol 
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Returns a String which contains the date and time of the stock.
   * 
   * @return dateTime 
   */
  public GregorianCalendar getDateTime() {
    return (GregorianCalendar) dateTime.clone();
  }
  
  @Override
  public String toString() {
    String stringValue = symbol + ";" + dateTime.getTimeInMillis();
    
    for (String key : values.keySet()) {
      stringValue += ";" + key + ":" + values.get(key);
    }
    return stringValue;
  }
  
}
