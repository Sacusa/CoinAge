<%-- 
    Document   : changepass
    Created on : Apr 21, 2018, 11:15:43 PM
    Author     : Yash
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
  <head>
    <%
      String userid = (String) session.getAttribute("username");
      if (userid == null) {
        String redirectURL = "login.xhtml";
        response.sendRedirect(redirectURL);
      }
    %>
    <title>CoinAge</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous"/>

    <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/animate.css">
    <link rel="stylesheet" type="text/css" href="resources/css/hamburgers.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/select2.min.css">
    <link rel="stylesheet" type="text/css" href="resources/css/util.css">
    <link rel="stylesheet" type="text/css" href="resources/css/main.css">
  </head>
  <body>

    <div class="limiter">
      <div class="container-login100">

        <div class="wrap-login100">
          <div class="login100-pic js-tilt" data-tilt>
            <img src="resources/images/img-01.png" alt="IMG">
          </div>

          <form class="login100-form validate-form" action="Change.jsp" method="POST">
            <span class="login100-form-title">
              Hey <%= session.getAttribute("username")%>, having trouble with the password?
            </span>

            <div class="wrap-input100 validate-input" data-validate = "Password is required">
              <input class="input100" type="password" name="NewPassword" id="NewPassword" placeholder="New Password">
              <span class="focus-input100"></span>
              <span class="symbol-input100">
                <i class="fa fa-lock" aria-hidden="true"></i>
              </span>
            </div>

            <div class="wrap-input100 validate-input" data-validate = "Password is required">
              <input class="input100" type="password" name="RetypePassword" id="RetypePassword" placeholder="Retype Password">
              <span class="focus-input100"></span>
              <span class="symbol-input100">
                <i class="fa fa-lock" aria-hidden="true"></i>
              </span>
            </div>

            <div class="container-login100-form-btn">
              <button class="login100-form-btn btn" id="submit" disabled="disabled">
                CHANGE IT
              </button>

            </div>

            <br><br><br><br><br>
          </form>
        </div>
      </div>
    </div>




    <!--===============================================================================================-->	
    <script src="resources/js/jquery-3.2.1.min.js"></script>
    <!--===============================================================================================-->
    <script src="resources/js/popper.js"></script>
    <script src="resources/js/bootstrap.min.js"></script>
    <!--===============================================================================================-->
    <script src="resources/js/select2.min.js"></script>
    <!--===============================================================================================-->
    <script src="resources/js/tilt.jquery.min.js"></script>
    <script >
      $('.js-tilt').tilt({
        scale: 1.1
      })
    </script>

    <script>


      $('#NewPassword').keyup(
              function () {
                var pass1 = document.getElementById('NewPassword').value

                check();
              });
      $('#RetypePassword').keyup(
              function () {
                var pass2 = document.getElementById('RetypePassword').value

                check();
              });



      function check() {
        if (document.getElementById('NewPassword').value == document.getElementById('RetypePassword').value)
        {


          if (document.getElementById('NewPassword').value != "" && document.getElementById('RetypePassword').value != "")
          {
            $("#submit").removeAttr("disabled");

          }
        } else {


          $("#submit").attr("disabled", "disabled");

        }
      }
      ;



    </script>
    <!--===============================================================================================-->
    <script src="resources/js/main.js"></script>

  </body>
</html>
