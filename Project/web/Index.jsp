<%-- 
    Document   : Index
    Created on : Oct 6, 2016, 8:36:19 PM
    Author     : limtingzhi
--%>

<%@page import="ccms.model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CCMS</title>
    </head>
    <%
        // Redirect user to home page if he has login
        Employee user = (Employee) session.getAttribute("loginuser");

        if (user != null) {
            response.sendRedirect("Home.jsp");
            return;
        }
    %>
    <body>
        <form name="login_form" method="post" action="LoginController.do">
            <label for="inputUsername">Username:</label>
            <input type="text" name="username">

            <label for="inputPassword">Password:</label>
            <input type="password" name="password">

            <input type="submit" name="Login" value="Login">
        </form>
        <%
            // Print out error message when incorrect credentials are entered
            String message = (String) request.getAttribute("errorMsg");
            if (message != null) {
                out.println(message);
            }
        %>
    </body>
</html>