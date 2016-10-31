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
        <title>MOM Complaints & Compliments Management System (CCMS)</title>
        <link rel="stylesheet" href="css/main.css">
    </head>
    <%
        // Redirect user to home page if he has login
        Employee user = (Employee) session.getAttribute("loginuser");

        if (user != null) {
            response.sendRedirect("Home.jsp");
            return;
        }
    %>
    <body id="index-bg">
        <div id="login-box">
            <div id="logo-div">
                <img id="logo" src="img/logo.jpg"/>
                <header>Complaints & Compliments Management System</header>
            </div>

            <form id="form-div" name="login_form" method="post" action="LoginController.do">
                <div class="form-label">
                    <label for="inputUsername">Username</label>
                    <input type="text" name="username">
                    <p></p>
                    <label for="inputPassword">Password</label>
                    <input type="password" name="password">

                    <div class="error-msg">
                        <%
                            // Print out error message when incorrect credentials are entered
                            String message = (String) request.getAttribute("errorMsg");
                            if (message != null) {
                                out.println(message);
                            }
                        %>
                    </div>

                    <input type="submit" name="Login" value="Login">
                </div>
            </form>
        </div>
    </body>
</html>