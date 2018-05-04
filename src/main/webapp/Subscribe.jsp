<%-- 
    Document   : Subscribe
    Created on : Apr 22, 2018, 9:27:26 PM
    Author     : Akshat
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>

<head>
  <meta charset="UTF-8">
  <title>CoinAge</title>
  
  <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.5/css/bootstrap.min.css'>

   <%
      String userid = (String) session.getAttribute("username");
      if (userid == null) {
        String redirectURL = "login.xhtml";
        response.sendRedirect(redirectURL);
      }
    %>
  
</head>

<body>
 
  <div class="jumbotron text-xs-center">
  <h1 class="display-3">Thank You!</h1>
  <h2>Adding new stock to your subscriptions, <%= (String) session.getAttribute("username") %></h2>
  <p class="lead"><strong>Please check your Dashboard</strong> for further information of the stocks subscribed.</p>
  <hr>
   <jsp:useBean id="sql" scope="request" class="capstone.kafka.coinage.Database" />
    <jsp:setProperty property="*" name="sql" />
    <%
       if( sql.addStock((String) session.getAttribute("username"), request.getParameter("name")))
       { 
          %>
        <img src="https://thumbs.gfycat.com/ShyCautiousAfricanpiedkingfisher-max-1mb.gif" style="display: block;
            margin-left: auto;
            margin-right: auto;
            width: 13%;">
        <h1 style="text-align: center">SUBSCRIBED</h1> 
        <%
           
       }
       else
       {
          %>
          <img src="resources\gifs\error.gif" style="display: block;
    margin-left: auto;
    margin-right: auto;
    width : 19%;">
        <h1 style="text-align: center">ERROR WHILE ADDING NEW STOCKS TO THE LIST</h1>
        <%
        }
    %>
  <p class="lead">
    <a class="btn btn-primary btn-lg" href="index.jsp" role="button">Continue to homepage</a>
  </p>
</div>
  <script src='https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.2/js/bootstrap.min.js'></script>

  

</body>

</html>
