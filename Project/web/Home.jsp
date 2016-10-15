<%-- 
    Document   : Home
    Created on : Oct 16, 2016, 2:30:13 AM
    Author     : limtingzhi
--%>

<%@page import="ccms.model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>

<%
    String name = "";

    if (user != null) {
        name = user.getName();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <h1>Welcome <%= name%>! </h1>

        <p><a href="Logout.jsp">Logout</a></p>
    </body>
</html>