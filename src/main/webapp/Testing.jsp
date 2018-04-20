<%-- 
    Document   : Testing
    Created on : Apr 19, 2018, 7:27:33 PM
    Author     : Akshat
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.0/jquery.min.js"></script>
        <script>
            $(document).ready(function() {
                var reloadData = 0; // store timer

                // load data on page load, which sets timeout to reload again
                loadData();
            });

            function loadData() {
                $('#load_me').load('DisplayTable.jsp?time=INTRADAY', function() {
                    if (reloadData != 0)
                        window.clearTimeout(reloadData);
                    reloadData = window.setTimeout(loadData, 1000)
                }).fadeIn("slow"); 
            }
        </script>
    </head>
    <body>
        <div id="load_me"></div>
    </body>
</html>
