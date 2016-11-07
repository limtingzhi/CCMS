<%-- 
    Document   : AddInfo
    Created on : Oct 29, 2016, 3:12:13 AM
    Author     : Feng Ru Chua
--%>

<%@page import="java.util.Arrays"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="ccms.model.SearchCase"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ccms.model.SearchCaseDAO"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - View Case</title>
    </head>
    <body id="background-content">
        <%
            String caseID = (String) request.getParameter("caseid");
            String nric = (String) request.getParameter("nric");

            int caseIDInt = Integer.parseInt(caseID);

            SearchCaseDAO scDao = new SearchCaseDAO();
            String result = scDao.showComplimentCase(caseIDInt, nric);
            List<String> resultArray = Arrays.asList(result.split(","));
        %>

        <p class="title"><img src="img/case.png">Case ID: <% out.print(caseID);%></p>
        <table class="no-fixed">
            <thead>
                <tr>
                    <td colspan=4>Respondent Details</td>
                </tr>
            </thead>
            <tr>
                <td><b>Name</b></td>
                <td><% out.println(resultArray.get(2));%></td>
                <td><b>Contact</b></td>
                <td><% out.println(resultArray.get(3));%></td>
            </tr>
            <tr>
                <td><b>NRIC</b></td>
                <td><% out.println(nric);%></td>
                <td><b>Email</b></td>
                <td><% out.println(resultArray.get(4));%></td>
            </tr>
        </table>

        <table class="case-details">
            <colgroup>
                <col class="col-1">
                <col class="col-2">
                <col class="col-3">
                <col class="col-4">
            </colgroup>
            <thead>
                <tr>
                    <td colspan=4>Compliment Details</td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><b>Department</b></td>
                    <td><% out.println(resultArray.get(6));%></td>
                    <td rowspan="2"><b>Description</b></td>
                    <td rowspan="2"><% out.println(resultArray.get(5));%></td>
                </tr>
                <tr>
                    <td><b>Employee Complimented</b></td>
                    <td><% out.println(resultArray.get(7));%></td>
                </tr>
            </tbody>
        </table>

        <input class="back-btn" type="button" onclick="history.go(-1);" value="Back">
    </body>
</html>
