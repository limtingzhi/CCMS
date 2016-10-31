<%-- 
    Document   : HandleCase
    Created on : 23 Oct, 2016, 5:35:25 PM
    Author     : asus user
--%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="ccms.model.*"%>
<%@include file="Protect.jsp" %>
<%
    int empID = 0;

    if (user != null) {
        empID = user.getEmployeeID();
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Complaint Cases</title>
    </head>
    <body>
        <%
            String message = (String) request.getAttribute("message");
            if(message != null) {
                out.println(message);
            }
            
            out.println("<h2>Outstanding Cases</h2>");
            LinkedHashMap<Integer, ArrayList<String>> cList = (LinkedHashMap<Integer, ArrayList<String>>) new CaseDAO(new EmployeeDAO(new DepartmentDAO()), new PersonDAO()).getAllCasebyEmpID(empID);
            if (cList.size() > 0) {
        %>        
        <table border="1">
            <thead>
                <tr>
                    <td>Case ID</td>
                    <td>Date Received</td>
                    <td>Expected Response Date</td>
                    <td>Difficulty</td>
                    <td>Issue</td>
                    <td>Last Saved</td>
                    <td>Respond</td>
                </tr>
            </thead>
            <tbody>
        <%            
            Set<Integer> keys = cList.keySet();
            for(Integer key: keys) {
                ArrayList<String> details = cList.get(key);                
        %>
            <form name="caseRespond" method="POST" action="CaseController.do">        
            <tr>
                <td><% out.println(key); %></td>
                <% for(String d: details) { %>
                <td><% out.println(d); %></td>
                <% } %>
                <td><input type="submit" value="View / Respond" /></td>
                <input type="hidden" value="<%out.print(key);%>" name="caseID">
            </tr>
            </form>
        <%
            }
        %>
            </tbody>           
        </table>
        <% 
            } else {
                out.println("There is no outstanding cases at the moment.");
            }
        %>
        <input type="button" onclick="history.go(-1);" value="Back">
    </body>
</html>
