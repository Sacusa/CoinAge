<%-- 
    Document   : Change
    Created on : Apr 22, 2018, 10:08:54 PM
    Author     : Akshat
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title>JSP Page</title>
         <jsp:useBean id="sql" class="capstone.kafka.coinage.Database" />
        <jsp:setProperty property="*" name="sql" />
    </head>
    <body>
        <%
            if(request.getParameter("NewPassword").equals(request.getParameter("RetypePassword"))){
            sql.changePassword((String) session.getAttribute("username") , request.getParameter("NewPassword") ); 
               // out.println("Password  changed successfully");
                 response.sendRedirect("index.jsp");
            }
            else
            {
               %>
        <img src="resources\gifs\passwordmismatch.gif" style="display: block;
                margin-left: auto;
                margin-right: auto;
                width: 50%;">
        <%
            }
        %>
    </body>
</html>
