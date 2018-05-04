<%-- 
    Document   : Stocks
    Created on : Apr 19, 2018, 10:55:03 PM
    Author     : Akshat
--%>

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
  <meta charset="UTF-8">
  <title>CoinAge</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <link rel='stylesheet prefetch' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css'>

      <link rel="stylesheet" href="resources\css\style1.css">

          
</head>

<body>
    <%--
 <%
      String userid = (String)session.getAttribute("username");
if(userid==null)
{
    String redirectURL = "login.xhtml";
    response.sendRedirect(redirectURL);
}
  %>
  --%>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.jsp">CoinAge</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav navbar-right">
           
            <li><a href="changepass.jsp">Settings</a></li>
           
            <li><a href="Logout.jsp">Logout</a></li>
          </ul>
         
        </div>
      </div>
    </nav>

    <div class="container-fluid">
      <div class="row">
        
        <div class="col-sm-9  col-md-12   main">
            <% String a = request.getParameter("subs");
                if(a.equals("True"))
                {
            %>
            <button type="button" class="btn btn-danger " onclick=" unsubs('<%= request.getParameter("name") %>') " style="float: right;">Unsubscribe</button>
          <%
              }
                else
                {
          %>
          <button type="button" class="btn btn-success" onclick=" subs('<%= request.getParameter("name") %>') " style="float: right;">Subscribe</button>
          <%
              }
              %>
          
          <h1 class="page-header " style=""> <% out.print( request.getParameter("name")); %></h1>
         
          <div class="col-sm-12 sub-header"> <div class="col-sm-2" style="width: max-content;"> <h2 style="width: auto;">Time :</h2></div><div class="col-sm-10"> <select class="form-control" style="width: auto; float: left ;    margin-top: 20px;">
    <option value="INTRADAY">INTRADAY</option>
    <option value="DAILY">DAILY</option>
    <option value="WEEKLY">WEEKLY</option>
    <option value="MONTHLY">MONTHLY</option>
                  </select></div></div>
           <div id="load_me"></div>
        </div>
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="../../assets/js/vendor/jquery.min.js"><\/script>')</script>
   
<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>

  <script src='https://code.jquery.com/jquery-2.2.4.min.js'></script>
<script src='https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js'></script>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js'></script>

 <script>
            $(document).ready(function() {
                var reloadData = 0; // store timer

                // load data on page load, which sets timeout to reload again
                loadData();
            });

            function loadData() {
                var reloadData = 0;
                var a = "<% out.print(request.getParameter("name")); %>";
                var b = "<% out.print(request.getParameter("time")); %>";
                
                $('#load_me').load('StockChart.jsp?name='+a+'&time='+b , function() {
                    if (reloadData !== 0)
                        window.clearTimeout(reloadData);
                     console.log("run");
                    reloadData = window.setTimeout(loadData, 1000)
                }).fadeIn("slow"); 
                 
            }
        </script>
       <script>
            var getUrlParameter = function getUrlParameter(sParam) {
                var sPageURL = decodeURIComponent(window.location.search.substring(1)),
                    sURLVariables = sPageURL.split('&'),
                    sParameterName,
                    i;

                for (i = 0; i < sURLVariables.length; i++) {
                    sParameterName = sURLVariables[i].split('=');

                    if (sParameterName[0] === sParam) {
                        return sParameterName[1] === undefined ? true : sParameterName[1];
                    }
                }
};
        </script>
        <script>
            $(document).ready(function() {
                    $('select').val(getUrlParameter('time'))
              });
        </script>
        <script>
            $('select').on('change', function() {
                
                $(location).attr('href',"Stocks.jsp?name="+getUrlParameter('name')+"&subs="+getUrlParameter('subs')+"&time="+this.value);
              });
            </script>
               <script>
                        function subs(name)
                        {
                            window.location = "Subscribe.jsp?name="+name;
                        }
                        
                    </script>
                       <script>
                        function unsubs(name)
                        {
                            window.location = "unsubscribe.jsp?name="+name;
                        }
                        
                    </script>
</body>

</html>
