<%-- 
    Document   : Logout.jsp
    Created on : Apr 22, 2018, 9:22:13 PM
    Author     : Akshat
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Logging you out</h1>
       
  <jsp:useBean id="login" scope="request" class="com.journaldev.jsf.beans.Login" />
    <jsp:setProperty property="*" name="login" />
    <% response.sendRedirect(login.logout()+".xhtml");  %>
    </body>
</html>
