<%-- 
    Document   : AddInfo
    Created on : Oct 29, 2016, 3:12:13 AM
    Author     : Feng Ru Chua
--%>

<%@page import="ccms.model.SearchCase"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ccms.model.SearchCaseDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Info</title>
    </head>
    <body>
        <%
                String caseID = (String) request.getParameter("caseid");
                String nric = (String) request.getParameter("nric");
                
                int caseIDInt = Integer.parseInt(caseID);
                
                SearchCaseDAO scDao = new SearchCaseDAO();
                ArrayList<SearchCase> caseList = scDao.searchCase(caseIDInt, nric);
                
                out.println("<h1>Case ID: " + caseID + "</h1>");
                out.println("<h2><u>Complainant's Details</u></h2>");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>Name: "+caseList.get(0).getPersonName()+"</td>");
                out.println("<td></td>");
                out.println("<td>Contact: "+caseList.get(0).getContactNo()+" </td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>NRIC: "+caseList.get(0).getPersonNric()+"</td>");
                out.println("<td></td>");
                out.println("<td>Email: "+caseList.get(0).getEmail()+"</td>");
                out.println("</tr>");
                out.println("</table>");
                out.println("<h2><u>Case Details</u></h2>");
                out.println("Description: "+caseList.get(0).getDescription()+"<br/>");
                out.println("Difficulty: "+caseList.get(0).getDifficulty()+"<br/>");
                out.println("Issue: "+caseList.get(0).getIssue()+"<br/>");
               
        %>
        </br>
        <form name="submitResponse" method="post" action="ProcessAddInfo.do">
            <b>Additional Info: </b><br/>
            <textarea name="addInfo" id="response" cols="60" rows="10"/><% out.print(caseList.get(0).getAdditionalInfo()); %> </textarea><br/>
            <input type="hidden" name="caseID" value="<% out.print(caseID); %>"/>
            <input type="submit" value="Add Additional Info"/>
        </form>
        <input type="button" onclick="history.go(-1);" value="Back">
    </body>
</html>
