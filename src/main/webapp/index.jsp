<%-- 
    Document   : index
    Created on : Apr 18, 2018, 10:32:44 PM
    Author     : Akshat
--%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException,java.nio.file.Files, java.nio.file.Paths, java.io.FileReader" %>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>

<html lang="en" >

  <head>
    <meta charset="UTF-8">
    <title>Bootstrap Dashboard Example</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>

    <link rel="stylesheet" href="resources\css\style1.css">

    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>
    <script>
      $(document).ready(function () {
        var reloadData = 0; // store timer
        loadData();
      });

      function loadData() {
        var reloadData = 0;
        $('#load_me').load('testGoogleCharts.jsp?time=INTRADAY', function () {
          console.log("Run");
          if (reloadData != 0)
            window.clearTimeout(reloadData);
          reloadData = window.setTimeout(loadData, 2000)

        }).fadeIn("slow");
      }
    </script>
  </head>

  <body >

    <%
      String userid = (String) session.getAttribute("username");
      if (userid == null) {
        String redirectURL = "login.xhtml";
        response.sendRedirect(redirectURL);
      }
    %>
    <jsp:useBean id="alpha" class="com.jsp.data.datadisplay2" />
    <jsp:setProperty property="*" name="alpha" />



    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Coin Age</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">

            <li><a href="#">Settings</a></li>

            <li><a href="#">Logout</a></li>
          </ul>
          <form class="navbar-form navbar-right" action="">
            <input type="text" class="form-control" placeholder="Search...">
          </form>
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">

        <div class="col-sm-9  col-md-12   main">
          <h1 class="page-header">Dashboard</h1>

          <div class="row placeholders">
            <%
              String filePath = "C:/Users/Akshat/Desktop/CoinAge-UI/JSF_Login_Logout/src/main/webapp/resources/text/Stocks.txt";

              try {
                BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
                String lineText = null;

                while ((lineText = lineReader.readLine()) != null) {

            %>
            <div class="col-xs-6 col-sm-3 placeholder">

              <h4><% out.println(lineText); %></h4>
              <span class="text-muted">Something else</span><br>
              <button type="button" class="btn btn-success">Success</button> 

            </div>
            <%
                }

                lineReader.close();
              } catch (IOException ex) {
                System.err.println(ex);
              }

            %>



          </div>

          <h2 class="sub-header">Section title</h2>
          <div class="table-responsive" id="load_me">

          </div>
        </div>
      </div>
    </div>
    <%alpha.test();%>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>




    <script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
    <script src='https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js'></script>
    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js'></script>



  </body>

</html>

