    <%-- 
        Document   : ViewCase
        Created on : 5 Nov, 2016, 9:45:54 PM
        Author     : asus user
    --%>

    <%@page import="java.util.Set"%>
    <%@page import="java.util.LinkedHashMap"%>
    <%@page import="ccms.model.Employee"%>
    <%@page import="ccms.model.CaseDAO"%>
    <%@page import="java.util.ArrayList"%>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Case</title>
    </head>
    <%
        String caseID = "";
        int employeeID = 0;
        ArrayList<String> caseDetails = (ArrayList<String>) request.getAttribute("caseDetails");
        if (caseDetails.size() > 0) {
            caseID = caseDetails.get(0);
            String email = caseDetails.get(2);
            String complainantName = caseDetails.get(3);
            String description = caseDetails.get(5);
            String additional_info = caseDetails.get(6);
            String difficulty = caseDetails.get(7);
            employeeID = Integer.parseInt(caseDetails.get(10));
            String employeeName = caseDetails.get(11);
            String addOnDate = caseDetails.get(caseDetails.size() - 1);
            out.println("<h1>Case ID: " + caseID + "</h1>");
            out.println("<h2><u>Current Employee Handling: " + employeeName + "</u></h2>");
            String responseMsg = caseDetails.get(9);
            if (responseMsg == null) {
                responseMsg = "";
            } else {
                out.println("Current working draft: " + responseMsg);
            }
            out.println("<h2><u>Complainant's Details</u></h2>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<td>Name: " + complainantName + "</td>");
            out.println("<td></td>");
            out.println("<td>Contact: " + caseDetails.get(4) + "</td>");
            out.println("</tr>");
            out.println("<tr>");
            out.println("<td>NRIC: " + caseDetails.get(1) + "</td>");
            out.println("<td></td>");
            out.println("<td>Email: " + email + "</td>");
            out.println("</tr>");
            out.println("</table>");
            out.println("<h2><u>Case Details</u></h2>");
            out.println("Description: " + description + "<br/>");
            out.println("Additional Info " + addOnDate + ": " + additional_info + "<br/>");
            out.println("Difficulty: " + difficulty + "<br/>");
            out.println("Issue: " + caseDetails.get(8));
    %>
    <br/>
    <br>
    <%
        }

        LinkedHashMap<Integer, ArrayList<String>> employeeWorkload = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("processedEmployeeWorkload");
        if (employeeWorkload.size() > 0) {
            Set<Integer> employees = employeeWorkload.keySet();
    %>
    <h2><u>List of Employees</u></h2>
    <form name="reassignCaseForm" method="POST" action="ProcessCaseReassignController.do" />            
    <table border="1">
        <tr>
            <td>Employee Name</td>
            <td>Position</td>
            <td># Easy Case</td>
            <td># Hard Case</td>
            <td># Super Complex Case</td>
            <td>Remarks</td>
        </tr>
        <%
            for (int eID : employees) {
                out.println("<tr>");
                ArrayList<String> workload = employeeWorkload.get(eID);
                for (String details : workload) {
                    out.println("<td>" + details + "</td>");
                }
                if(eID != employeeID) {
        %> 
        <td><input type="submit" value="Assign"/></td>
        <input type="hidden" name="newAssignID" value="<%=eID%>"/>
        <%
                }
                out.println("</tr>");
            }
        %>
        <input type="hidden" name="caseID" value="<%=caseID%>"/>
        <input type="hidden" name="currentEmployeeID" value="<%=employeeID%>"/>
    </table>
    </form> 
    <%
        }
    %>

