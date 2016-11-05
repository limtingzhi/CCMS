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
                SearchCase sc = caseList.get(0);
                String complainantName = sc.getPersonName();
                String complainantNRIC = sc.getPersonNric();
                String email = sc.getEmail();
                String additional_info = sc.getAdditionalInfo();
                if(additional_info == null) {
                    additional_info = "";
                }
                out.println("<h1>Case ID: " + caseID + "</h1>");
                out.println("<h2><u>Complainant's Details</u></h2>");
                out.println("<table>");
                out.println("<tr>");
                out.println("<td>Name: " + complainantName + "</td>");
                out.println("<td></td>");
                out.println("<td>Contact: "+ sc.getContactNo()+" </td>");
                out.println("</tr>");
                out.println("<tr>");
                out.println("<td>NRIC: " + complainantNRIC +"</td>");
                out.println("<td></td>");
                out.println("<td>Email: " + email +"</td>");
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
            <textarea name="addInfo" id="response" cols="60" rows="10"/><%=additional_info%> </textarea><br/>
            <input type="hidden" name="caseID" value="<%=caseID%>"/>
            <input type="hidden" name="complainantName" value="<%=complainantName%>"/>
            <input type="hidden" name="email" value="<%=email%>"/>
            <input type="submit" value="Add Additional Info"/>
        </form>
        <input type="button" onclick="history.go(-1);" value="Back">
    </body>
</html>
