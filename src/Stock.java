/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.coinage;

import java.util.HashMap;
import java.util.Map;


/**
 * The information about an individual stock at a particular time instance is stored 
 * in this class.
 * 
 * @author tanmaypatil
 */
public class Stock 
{
    /** symbol contains the stock symbol. For example, Microsoft symbol will be "MSFT". */
    private final String symbol;
    
    /** dateTime represents the date and time the object contains information for. */
    private final String dateTime;
    
    /**
     * values contains the detailed information about the stock like openValue, 
     * highValue, lowValue, closeValue and volume, each of which can be referred to using the
     * string "open", "high", "low", "close" and "volume" respectively.
     */
    private final Map<String, Double> values;
    
    /**
     * @param symbol
     * @param dateTime
     * @param values
     */
    public Stock(String symbol, String dateTime, Map<String, Double> values) {
        this.symbol = symbol;
        this.dateTime = dateTime;
        this.values = new HashMap<> (values);
    }
    
    /**
     * Returns a HashMap which contains a detailed information about the stock.
     * @return values
     */
    public Map<String, Double> getValues ()
    {
        return new HashMap<> (values);
    }

    /**
     * Returns a String which contains the stock symbol
     * @return symbol 
     */
    public String getSymbol() 
    {
        return symbol;
    }
    /**
     * Returns a String which contains the date and time of the stock
     * @return dateTime 
     */
    public String getDateTime() 
    {
        return dateTime;
    }
    
}
