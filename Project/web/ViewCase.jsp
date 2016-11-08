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
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Reassign Case</title>
    </head>
    <body id="background-content">
        <%
            String caseID = "";
            int employeeID = 0;
            String employeeName = "";
            ArrayList<String> caseDetails = (ArrayList<String>) request.getAttribute("caseDetails");
            if (caseDetails.size() > 0) {
                caseID = caseDetails.get(0);
                String email = caseDetails.get(2);
                String complainantName = caseDetails.get(3);
                String description = caseDetails.get(5);
                String additional_info = caseDetails.get(6);
                if (additional_info.equals("N/A")) {
                    additional_info = "-";
                }
                String difficulty = caseDetails.get(7);
                employeeID = Integer.parseInt(caseDetails.get(10));
                employeeName = caseDetails.get(11);
                String addOnDate = caseDetails.get(caseDetails.size() - 1);
                if (addOnDate.equals("N/A")) {
                    addOnDate = "-";
                } else {
                    int index1 = addOnDate.indexOf("on");
                    int index2 = addOnDate.indexOf(" ", index1);
                    String dateStr = addOnDate.substring(index2 + 1, addOnDate.length() - 1);
                    addOnDate = dateStr;
                }

                String responseMsg = caseDetails.get(9);
                if (responseMsg == null) {
                    responseMsg = "";
                } else {
                    out.println("Current working draft: " + responseMsg);
                }
        %>

        <p class="title"><img src="img/case.png">Case ID: <% out.print(caseID);%></p>
        <table class="no-fixed">
            <thead>
                <tr>
                    <td colspan=4>Complainant Details</td>
                </tr>
            </thead>
            <tr>
                <td><b>Name</b></td>
                <td><% out.println(complainantName);%></td>
                <td><b>Contact</b></td>
                <td><% out.println(caseDetails.get(4));%></td>
            </tr>
            <tr>
                <td><b>NRIC</b></td>
                <td><% out.println(caseDetails.get(1));%></td>
                <td><b>Email</b></td>
                <td><% out.println(email);%></td>
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
                    <td colspan=4>Case Details</td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><b>Difficulty</b></td>
                    <td><% out.println(difficulty);%></td>
                    <td rowspan="2"><b>Description</b></td>
                    <td rowspan="2"><% out.println(description);%></td>
                </tr>
                <tr>
                    <td><b>Issue</b></td>
                    <td><% out.println(caseDetails.get(8));%></td>
                </tr>
                <tr>
                    <td><b>Additional Info Date</b></td>
                    <td><% out.println(addOnDate);%></td>
                    <td><b>Additional Info</b></td>
                    <td><% out.println(additional_info);%></td>
                </tr>
            </tbody>
        </table>

        <%
            }
            LinkedHashMap<Integer, ArrayList<String>> employeeWorkload = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("processedEmployeeWorkload");
            if (employeeWorkload.size() > 0) {
                Set<Integer> employees = employeeWorkload.keySet();
        %>

        <p class="title-sub"><img src="img/person.png">List of Employees</p>
        <p><i>Current Employee Handling: <%=employeeName%></i></p>

        <table class="employees-list">
            <thead>
                <tr>
                    <td>Employee Name</td>
                    <td>Position</td>
                    <td># Easy Case</td>
                    <td># Hard Case</td>
                    <td># Super Complex Case</td>
                    <td>Remarks</td>
                    <td>Actions</td>
                </tr>
            </thead>

            <% for (int eID : employees) {%>
            <form name="reassignCaseForm" method="POST" action="ProcessCaseReassignController.do" />            
            <%
                out.println("<tr>");
                ArrayList<String> workload = employeeWorkload.get(eID);
                for (String details : workload) {
                    out.println("<td>" + details + "</td>");
                }
                if (eID != employeeID) {
            %> 
            <td>
                <div class="img-btn">
                    <input type="image" src="img/reassign.png" value="Assign" title="Assign"/>
                </div>
            </td>
            <input type="hidden" name="newAssignID" value="<%=eID%>"/>
            <%
            } else {%>
            <td>-</td>
            <% }
                out.println("</tr>");%>
            <input type="hidden" name="caseID" value="<%=caseID%>"/>
            <input type="hidden" name="currentEmployeeID" value="<%=employeeID%>"/>
        </form> 
        <% }%>
    </table>
    <%
        }
    %>
</body>
</html>
