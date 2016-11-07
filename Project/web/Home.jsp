<%-- 
    Document   : Home
    Created on : Oct 16, 2016, 2:30:13 AM
    Author     : limtingzhi
--%>

<%@page import="ccms.model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>

<%
    String name = "";
    String position = ""; 

    if (user != null) {
        name = user.getName();
        position = user.getPosition();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MOM CCMS - Home</title>
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
    </head>
    <body id="background">
        <div id="main-div">
            <div id="logo-div">
                Welcome <%= position%>, <%= name%>!
            </div>
            <div id="divider">
                <div id="inner"></div>
                <div id="features-header">Features</div>
            </div>
            <div id="features-div">
                <a class="features" href="CreateCase.jsp">
                    <img src="img/create.png"><br>
                    Create</a>
                <a class="features" href="ListOfCases.jsp">
                    <img src="img/handle.png"><br>
                    Handle</a>
                <a class="features" href="SearchCase.jsp">
                    <img src="img/search.png"><br>
                    Search</a>
                <a class="features" href="PotentialReassignCasesList.jsp">
                    <img src="img/reassign.png"><br>
                    Reassign</a>
                <a class="features" href="Home.jsp">
                    <img src="img/statistics.png"><br>
                    Statistics</a>
            </div>
        </div>
    </body>
</html>