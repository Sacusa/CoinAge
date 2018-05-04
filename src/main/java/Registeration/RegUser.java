/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registeration;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.sql.*;

/**
 *
 * @author Akshat
 */
@Named(value = "regUser")
@RequestScoped
public class RegUser {

    private String userName;
    private String out;
    private String passwd;
    private String repass;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getRepass() {
        return repass;
    }

    public void setRepass(String repass) {
        this.repass = repass;
    }
/*
    public String Reg() {
        if (true) {
            if (username()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3360/capstone", "root", "1234");
                    Statement statement = con.createStatement();
                    System.out.println(userName);
                    
                    String Query2 = "insert into users values ('" + userName + "','" + pass + "')";
                    statement = con.createStatement();
                    statement.executeUpdate(Query2);
                    System.out.println("Database Updated Successfully");
                    out = "Database Updated Successfully";
                    return "login";
                } catch (Exception e) {
                    System.out.println(e);
                    out=e.getMessage().toString();
                    return "";
                }
            }
              out="UserName alread exist";  
             return "";
        } else {
            out="Password mismatch error";
            return "";
        }

    }

 */
    public String validateUsernamePassword() {
               System.out.println("\n"+passwd+"\na"+repass+"++");
		 if (passwd.equals(repass) && passwd.length()>0) {
            if (username()) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/capstone?autoReconnect=true&useSSL=false", "root", "sacusa");
                    Statement statement = con.createStatement();
                    System.out.println("\naa\naa\naa\naa\naa\naa"+userName+"\naa\naa\naa\naa\naa\naa\naa");
                    String Query2 = "insert into users values ('" + userName + "','" + passwd + "','')";
                    statement = con.createStatement();
                    statement.executeUpdate(Query2);
                    System.out.println("Database Updated Successfully");
                    out = "Database Updated Successfully";
                    return "login";
                } catch (Exception e) {
                    System.out.println(e);
                    out = e.getMessage().toString();
                    return "";
                }
            }
              out="Username already exists";  
             return "";
        } else {
            out="Password mismatch error or Check the length of the password ";
            return "";
        }
	}
    
    
    
    
    public boolean username() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/capstone?autoReconnect=true&useSSL=false", "root", "sacusa");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from users where uname ='" + userName + "';");
            if (rs.next()) {

                con.close();
                out = "Username already exists";
                return false;
            } else {

                con.close();
                return true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    /**
     * Creates a new instance of RegUser
     */
    public RegUser() {
    }

}
