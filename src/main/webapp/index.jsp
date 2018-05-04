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
    <%
      String userid = (String) session.getAttribute("username");
      if (userid == null) {
        String redirectURL = "login.xhtml";
        response.sendRedirect(redirectURL);
      }
    %>
    <script>console.log("<%= (String) session.getAttribute("username")%>");</script>
    <meta charset="UTF-8">
    <title>CoinAge</title>
    <jsp:useBean id="sql" class="capstone.kafka.coinage.Database" />
    <jsp:setProperty property="*" name="sql" />
    <jsp:useBean id="login" scope="request" class="com.journaldev.jsf.beans.Login" />

    <% String stock = sql.getStocksList((String) session.getAttribute("username"));%>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>

    <link rel="stylesheet" href="resources\css\style1.css">

    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>
    <script>
      $(document).ready(function () {
        var reloadData = 0; // store timer
        loadData();
        console.log("<%= stock%>");
      });

      function loadData() {
        var reloadData = 0;
        $('#load_me').load('DisplayTable.jsp?time=INTRADAY&name=<%= stock%>', function () {

          if (reloadData != 0)
            window.clearTimeout(reloadData);
          reloadData = window.setTimeout(loadData, 1000)

        }).fadeIn("slow");
      }
    </script>
  </head>

  <body >


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
          <a class="navbar-brand" href="#">CoinAge</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">

            <li><a href="changepass.jsp">Settings</a></li>

            <li><a href="Logout.jsp">Logout</a></li>
          </ul>
          <!--<form class="navbar-form navbar-right" action="">
          <input type="text" class="form-control" placeholder="Search...">
        </form>-->
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">

        <div class="col-sm-9  col-md-12   main">
          <h1 class="page-header">Dashboard</h1>

          <div class="row placeholders">
            <%! String Stocks[];
              int comma = 0;
              int len = 0;
              int match = 0;
              String list;
                                                                           %>

            <script>console.log("Starting");</script>
            <%

              try {
                String filePath = "D:\\programs\\java\\CoinAge-UI - Copy\\JSF_Login_Logout\\src\\main\\webapp\\resources\\text\\Stocks.txt";

            %>
            <script>console.log("In  Try block");</script>
            <%                          BufferedReader lineReader = new BufferedReader(new FileReader(filePath));
              String lineText = null;
            %>
            <script>console.log("Try Block 2");</script>
            <%
              list = stock;
              Stocks = list.split(",");
              if (list.contains(",")) {

                comma = 1;

            %>
            <script>console.log("List ',' : <%= list%>");</script>
            <%

              }

              while ((lineText = lineReader.readLine()) != null) {

                if (comma == 1) {
                  for (int i = 0; i < Stocks.length; i++) {
                    if (lineText.equals(Stocks[i])) {
                      match = 1;
                      break;
                    } else {
                      continue;
                    }

                  }
                  if (match == 1) {
            %>
            <div class="col-xs-6 col-sm-3 placeholder">

              <h4><% out.println(lineText);%></h4>
              <button type="button" class="btn btn-danger" onclick="unsubs('<%= lineText%>')">Unsubscribe</button> 
            </div>
            <%
              match = 0;
            } else {
            %>
            <div class="col-xs-6 col-sm-3 placeholder">

              <h4><% out.println(lineText);%></h4>
              <button type="button" class="btn btn-success" onclick="subs('<%= lineText%>')">Subscribe</button> 
            </div>
            <%
                match = 0;
              }
            } else {

              if (lineText.equals(Stocks[0])) {
            %>
            <div class="col-xs-6 col-sm-3 placeholder">

              <h4><% out.println(lineText);%></h4>
              <button type="button" class="btn btn-danger" onclick="unsubs('<%= lineText%>')">Unsubscribe</button> 
            </div>
            <%
            } else {
            %>
            <div class="col-xs-6 col-sm-3 placeholder">

              <h4><% out.println(lineText);%></h4>
              <button type="button" class="btn btn-success" onclick="subs('<%= lineText%>')">Subscribe</button> 
            </div>
            <%
                  }
                }

              }
              lineReader.close();
            } catch (IOException ex) {
              System.err.println(ex);
            %>
            <script>console.log('<%= ex.getMessage()%>');</script>
            <%

              }

            %>



          </div>

          <h2 class="sub-header">Section title</h2>
          <div class="table-responsive" id="load_me">

          </div>
        </div>
      </div>
    </div>
    <script>
      function subs(name)
      {
        window.location = "Subscribe.jsp?name=" + name;
      }
      function unsubs(name)
      {
        window.location = "unsubscribe.jsp?name=" + name;
      }

    </script>
    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>




    <script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
    <script src='https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js'></script>
    <script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js'></script>



  </body>

</html>

