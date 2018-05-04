/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package capstone.kafka.coinage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author tanmaypatil
 */
public class Database {

  Connection connection;
  ResultSet resultSet;
  Statement statement;
  String signup = "SIGNUP";
  String login = "LOGIN";
  String subscribe = "SUBSCRIBE";

  public Database() {
    connect();
  }

  private void connect() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      String path = "jdbc:mysql://localhost:3306/capstone?autoReconnect=true&useSSL=false";
      String user = "root";
      String password = "sacusa";
      connection = DriverManager.getConnection(path, user, password);
      statement = connection.createStatement();
      System.out.println("Opened database successfully");

    } catch (ClassNotFoundException | SQLException e) {
      System.out.println(e);
    }
  }

  public Boolean isValidUser(String username, String password) {

    boolean result = false;
    String command = "SELECT uname,password FROM users";
    try {
      resultSet = statement.executeQuery(command);
      if (resultSet != null) {
        System.out.println("Found Data");
        while (resultSet.next()) {
          if (username.equals(resultSet.getString("uname"))
                  && password.equals(resultSet.getString("password"))) {
            result = true;
            System.out.println("Login Confirmed");
          } 
        }
      }
      
    }
    catch(SQLException e){
      result = false;
    }
    return result;
  }

  public int insertUser(String username, String password) {
    int response;
    String command = String.format("INSERT INTO users (uname,password) values ('%s','%s')",
            username, password);
    try {
      statement.executeUpdate(command);
      response = 1;
    } catch (SQLException e) {
      response = 0;
    }

    return response;

  }

  public boolean changePassword(String username, String newPassword) {
    String command = String.format("UPDATE users SET password = '%s' WHERE uname = '%s'  ", newPassword, username);
    try {
      statement.executeUpdate(command);
      return true;
    } catch (SQLException e) {
      return false;
    }
  }

  public String getStocksList(String username) throws SQLException {
    String command = String.format("SELECT symbols FROM users WHERE uname = '%s'", username);
    try {
      resultSet = statement.executeQuery(command);
      while (resultSet.next()) {
        System.out.println(resultSet);
        System.out.print(resultSet.getString("symbols"));
        return resultSet.getString("symbols");
      }
      return "";
    } catch (SQLException e) {
      return "";
    }

  }

  public boolean addStock(String username, String stock) {
    String command = String.format("SELECT * from users where uname = '%s'", username);
    String sqlTopic = null;

    try {
      resultSet = statement.executeQuery(command);
      while (resultSet.next()) {
        sqlTopic = resultSet.getString("symbols");
      }
      System.out.println(sqlTopic);
      if (sqlTopic == null || sqlTopic.length()<1) {
        command = String.format("UPDATE users set symbols = '%s' where uname = '%s'", stock, username);

      } else {
        command = String.format("UPDATE users set symbols = '%s' where uname = '%s'", sqlTopic + "," + stock, username);
      }
      statement.executeUpdate(command);
    } catch (SQLException e) {
      return false;
    }

    return true;
  }

  public boolean removeStock(String username, String stock) {
    String command = String.format("SELECT * from users where uname = '%s'", username);
    String sqlTopic = null;

    try {
      resultSet = statement.executeQuery(command);
      while (resultSet.next()) {
        sqlTopic = resultSet.getString("symbols");
      }
      if (sqlTopic == null) {
        return false;

      } else {
        String[] stocks = sqlTopic.split(",");
        String updatedStocks = "";
        int i;
        
        
        /*for (i = 0; i < stocks.length; i++) {
            
          if (!stocks[i].equals(stock)) {
            if(updatedStocks.isEmpty()){
              updatedStocks = stocks[i];
            
              }
            else {
              updatedStocks = updatedStocks + "," + stocks[i];
            }
            
            
          }
        }*/
        
        
        List<String> list = new ArrayList<String>(Arrays.asList(stocks));
        list.remove(stock);
        
        stocks = list.toArray(new String[0]);
        for(int ii=0; ii<stocks.length; ii++)
        {
        updatedStocks = stocks[ii]+","+updatedStocks;
        
        }
        if(updatedStocks.contains(",")){
            if(updatedStocks.charAt(updatedStocks.length()-1)==',')
            {
            updatedStocks = updatedStocks.substring(0, updatedStocks.length()-1);
            }
      }
        
            /*
        
        String[] str_array = {"item1","item2","item3"};
        List<String> list = new ArrayList<String>(Arrays.asList(str_array));
        list.remove("item2");
        str_array = list.toArray(new String[0]);
        
        
        
        
        
        */
        
        command = String.format("UPDATE users set symbols = '%s' where uname = '%s'", updatedStocks, username);
        statement.executeUpdate(command);
      }
    } catch (SQLException e) {
      return false;
    }
    return true;
  }
  /*
  public static void main(String args []){
    Database db  = new Database();
    db.insertUser("user1", "password");
    db.insertUser("user2", "password");
    db.insertUser("user3", "password");
    db.insertUser("user4", "password");
    db.changePassword("user1", "newPassword");
    db.addStock("user1", "MSFT");
    db.addStock("user1", "GOOGL");
    db.addStock("user1", "AAPL");
    db.addStock("user1", "Y");
    db.removeStock("user1","Y");
    db.isValidUser("user1", "newPassword");
    
  }
*/
}
