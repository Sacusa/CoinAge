package com.jsp.data;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.servlet.ServletContext;


/**
 *
 * @author Yash
 */
@ManagedBean
@RequestScoped
@Named(value="datadisplay")

                
public class datadisplay2 {

    public datadisplay2() {
        
    }
    
    
    
    
    private String value1="1235",value2="1255255",value3="",value4="",value5="",
               value11="",value12="",value13="",value14="",value15="",
                value21="",value22="",value23="",value24="",value25="",
                value31="",value32="",value33="",value34="",value35="",
                value41="adsd",value42="",value43="",value44="",value45="",test="";

    public String getValue1() {
        
        
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values1.txt")));
      
      
      
      
      return content;
      
    }
    catch(Exception e)
    {
   return e.toString();
    }
       
    }

    public void setValue1(String value1) {
        
        this.value1 = value1;
        
        
        
    }

    public String getValue2() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values2.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue3() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values3.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue3(String value3) {
        this.value3 = value3;
    }

    public String getValue4() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values4.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue4(String value4) {
        this.value4 = value4;
    }

    public String getValue5() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values5.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue5(String value5) {
        this.value5 = value5;
    }

    public String getValue11() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values6.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue11(String value11) {
        this.value11 = value11;
    }

    public String getValue12() {
      try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values7.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue12(String value12) {
        this.value12 = value12;
    }

    public String getValue13() {
        
    try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values8.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue13(String value13) {
        this.value13 = value13;
    }

    public String getValue14() {
        
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values9.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
        
        
    }

    public void setValue14(String value14) {
        this.value14 = value14;
    }

    public String getValue15() {
      
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values10.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
        
    }

    public void setValue15(String value15) {
        this.value15 = value15;
    }

    public String getValue21() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values11.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue21(String value21) {
        this.value21 = value21;
    }

    public String getValue22() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values12.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue22(String value22) {
        this.value22 = value22;
    }

    public String getValue23() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values13.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue23(String value23) {
        this.value23 = value23;
    }

    public String getValue24() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values14.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue24(String value24) {
        this.value24 = value24;
    }

    public String getValue25() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values15.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue25(String value25) {
        this.value25 = value25;
    }

    public String getValue31() {
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values16.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue31(String value31) {
        this.value31 = value31;
    }

    public String getValue32() {
       try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values17.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue32(String value32) {
        this.value32 = value32;
    }

    public String getValue33() {
       try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values18.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue33(String value33) {
        this.value33 = value33;
    }

    public String getValue34() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values19.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue34(String value34) {
        this.value34 = value34;
    }

    public String getValue35() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values20.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue35(String value35) {
        this.value35 = value35;
    }

    public String getValue41() {
       
        try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values21.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
        
    }

    public void setValue41(String value41) {
        this.value41 = value41;
    }

    public String getValue42() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values22.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue42(String value42) {
        this.value42 = value42;
    }

    public String getValue43() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values23.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue43(String value43) {
        this.value43 = value43;
    }

    public String getValue44() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values24.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue44(String value44) {
        this.value44 = value44;
    }

    public String getValue45() {
         try{
        String content = new String(Files.readAllBytes(Paths.get("C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/web/DATA/values25.txt")));
    return content;
    }
    catch(Exception e)
    {
   return e.toString();
    }
    }

    public void setValue45(String value45) {
        this.value45 = value45;
     
    }
    
    
    
    public void test(){
        System.out.println("Test called");
        value1 = "Akshat";
         setValue2(Integer.toString(500));
          setValue3(Integer.toString(800));
    }
    
}
