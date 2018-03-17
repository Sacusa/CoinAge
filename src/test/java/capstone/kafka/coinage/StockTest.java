package capstone.kafka.coinage;

import static org.junit.Assert.assertEquals;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class StockTest {
  
  /**
   * Unit tests for the class Stock.java.
   * 
   * <p>The constructor allows some keys in 'values' to be absent, and some extras to be present.
   * This functionality is tested.</p>
   * 
   * <p>Tested the immutability of the class.</p>
   */
  
  String symbol;
  GregorianCalendar dateTime = new GregorianCalendar();
  Map<String, Double> values = new HashMap<>();
  
  /**
   * Passes all the required values in the map 'values' to the constructor.
   * Tests all the getter methods.
   */
  @Test
  public void constructorAllValues() {
    symbol = "ABCD";
    dateTime.set(2018, 1, 25, 15, 30, 25);
    values.clear();
    values.put("open", 10d);
    values.put("high", 20d);
    values.put("low", 30d);
    values.put("close", 40d);
    values.put("volume", 50d);
    
    Stock testObject = new Stock(symbol, dateTime, values);
    
    assertEquals(symbol, testObject.getSymbol());
    assertEquals(dateTime, testObject.getDateTime());
    assertEquals(values, testObject.getValues());
  }
  
  /**
   * Passes some of the required values in the map 'values' to the constructor.
   * Tests all the getter methods.
   */
  @Test
  public void constructorSomeValues() {
    symbol = "ABCD";
    dateTime.set(2018, 1, 25, 15, 30, 25);
    values.clear();
    values.put("open", 10d);
    values.put("close", 40d);
    values.put("volume", 50d);
    
    Stock testObject = new Stock(symbol, dateTime, values);
    
    values.put("high", 0d);
    values.put("low", 0d);
    
    assertEquals(symbol, testObject.getSymbol());
    assertEquals(dateTime, testObject.getDateTime());
    assertEquals(values, testObject.getValues());
  }
  
  /**
   * Passes some of the required values, along with some extra values, in the map 'values' to the
   * constructor.
   * Tests all the getter methods.
   */
  @Test
  public void constructorExtraValues() {
    symbol = "ABCD";
    dateTime.set(2018, 1, 25, 15, 30, 25);
    values.clear();
    values.put("open", 10d);
    values.put("high", 20d);
    values.put("low", 30d);
    values.put("close", 40d);
    values.put("volume", 50d);
    values.put("extra1", 100d);
    values.put("extra2", 200d);
    
    Stock testObject = new Stock(symbol, dateTime, values);
    
    values.remove("extra1");
    values.remove("extra2");
    
    assertEquals(symbol, testObject.getSymbol());
    assertEquals(dateTime, testObject.getDateTime());
    assertEquals(values, testObject.getValues());
  }
  
  /**
   * Makes sure the class is immutable.
   * Tests all the getter methods.
   */
  @Test
  public void immutabilityTest() {
    symbol = "ABCD";
    dateTime.set(2018, 1, 25, 15, 30, 25);
    values.clear();
    values.put("open", 10d);
    values.put("high", 20d);
    values.put("low", 30d);
    values.put("close", 40d);
    values.put("volume", 50d);
    
    final Stock testObject = new Stock(symbol, dateTime, values);
    final GregorianCalendar requiredDateTime = (GregorianCalendar) dateTime.clone();
    final Map<String, Double> requiredValues = new HashMap<>(values);
    
    // modify the arguments, and check if the object gets mutated
    dateTime.set(2019, 2, 15, 12, 50, 1);
    values.put("extra1", 100d);
    values.put("extra2", 200d);
    
    GregorianCalendar receivedDateTime = testObject.getDateTime();
    Map<String, Double> receivedValues = testObject.getValues();
    
    assertEquals(symbol, testObject.getSymbol());
    assertEquals(requiredDateTime, receivedDateTime);
    assertEquals(requiredValues, receivedValues);
    
    // modify the received values, and check if the object gets mutated
    receivedDateTime.set(2019, 2, 15, 12, 50, 1);
    receivedValues.remove("open");
    
    assertEquals(symbol, testObject.getSymbol());
    assertEquals(requiredDateTime, testObject.getDateTime());
    assertEquals(requiredValues, testObject.getValues());
  }
  
}
