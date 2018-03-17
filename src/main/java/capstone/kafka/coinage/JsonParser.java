package capstone.kafka.coinage;

import java.util.GregorianCalendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class parses JSON and returns a Stock object.
 * @author tanmaypatil
 */
public class JsonParser {

  /**
   * Describe this.
   * @param args Describe this.
   * @throws IOException Describe this.
   */
  public static void main(String [] args) throws IOException {
    String request = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=MSFT&interval=5min&outputsize=compact&apikey=XS1YCFU15GDN1O6T";
    JsonParser p = new JsonParser();
    List<Stock> s = p.getLatestStock(request,"MSFT");
  }
  
  /**
   * Describe this.
   * @param request Describe this.
   * @param symbol Describe this.
   * @return Describe this.
   * @throws IOException Describe this.
   */
  
  public List<Stock> getLatestStock(String request,String symbol) throws IOException {
    List<Stock> stockList = new ArrayList<Stock>();
    JSONObject json = readJsonFromUrl(request);
    JSONObject j = json.getJSONObject("Monthly Time Series");
    Iterator<String> s = j.keys(); 
    while(s.hasNext())
    {
        try{
            String date = s.next().toString();
            JSONObject k = j.getJSONObject(date);
            Double open = new Double(k.getString("1. open"));
            Double close = new Double(k.getString("4. close"));
            Double high = new Double(k.getString("2. high"));
            Double low = new Double(k.getString("3. low"));
            Double volume = new Double(k.getString("5. volume"));
            Map<String,Double> m = new HashMap<>();
            m.put("open", open);
            m.put("close", close);
            m.put("high", high);
            m.put("low", low);
            m.put("volume", volume);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date1 = dateFormat.parse(date);
            GregorianCalendar dateTime = new GregorianCalendar();
            dateTime.setTime(date1);
            Stock stock = new Stock(symbol,dateTime,m);
            stockList.add(stock);
        }catch(Exception e)
        {}
        
    }
    return stockList;
  }
  
  /**
   * Describe this.
   * @param rd Describe this.
   * @return Describe this.
   * @throws IOException Describe this.
   */
  
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  /**
   * Describe this.
   * @param url Describe this.
   * @return Describe this.
   * @throws IOException Describe this.
   * @throws JSONException Describe this.
   */
  
  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    try (InputStream is = new URL(url).openStream()) {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } 
  }    
}
