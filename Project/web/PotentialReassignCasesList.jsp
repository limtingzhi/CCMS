<%-- 
    Document   : PotentailReassignCases
    Created on : 23 Oct, 2016, 5:35:25 PM
    Author     : asus user
--%>
<%@page import="ccms.controller.ReassignCaseController"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="ccms.model.*"%>
<%@include file="NavigationBar.jsp" %>
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
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Reassign Cases</title>
    </head>
    <body id="background-content">
        <p class="title"><img src="img/reassign.png">Reassign Case</p>
            <% SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");%>
        <p class="title-sub"><i>Today's Date: <%=sdf.format(new Date())%></i></p>
        <div class="handle-msg">
            <%
                String message = (String) request.getAttribute("reassignMessage");
                if (message != null) {
                    out.println(message);
                }
            %>
        </div>   
        <%
//            LinkedHashMap<Integer, ArrayList<String>> cList = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("potentialCases");
            ReassignCaseController rcc = new ReassignCaseController();
            LinkedHashMap<Integer, ArrayList<String>> cList = rcc.processOutstandingCases();
//            out.println(cList.size());
            if (cList.size() > 0) {
        %>        
        <table class="action">
            <thead>
                <tr>
                    <td>Case ID</td>
                    <td>Received Date</td>
                    <td>Expected Response Date</td>
                    <td>Difficulty</td>
                    <td>Issue</td>
                    <td>Remarks</td>
                    <td>Action</td>                    
                </tr>
            </thead>
            <tbody>
                <%
                    Set<Integer> keys = cList.keySet();
                    for (Integer caseID : keys) {
                        ArrayList<String> details = cList.get(caseID);
                %>
            <form name="reassignCase" method="POST" action="DoReassignCaseController.do">        
                <tr>
                    <td><% out.println(caseID);%></td>
                    <%
                        String dateReceived = details.get(0);
                        String expectedDateResponse = details.get(1);
                        String difficulty = details.get(2);
                        String issue = details.get(3);
                        String employeeID = details.get(6);
                        String lastSaved = details.get(7);
                        String remarks = details.get(details.size() - 1);
                    %>
                    <td><%=dateReceived%></td>
                    <td><%=expectedDateResponse%></td>
                    <td><%=difficulty%></td>
                    <td><%=issue%></td>
                    <td><%=remarks%></td>
                    <td>                        
                        <div class="img-btn">
                            <input type="image" src="img/reassign.png" value="Reassign" title="Reassign"/>
                        </div>
                    </td>
                <input type="hidden" name="expectedDateResponse" value="<%=expectedDateResponse%>" />
                <input type="hidden" name="caseID" value="<%=caseID%>" />
                <input type="hidden" name="employeeID" value="<%=employeeID%>" />
                </tr>
            </form>
            <% }
            } else {%>
            <tr>
                <td colspan=7 align=center>
                    <p>There are no outstanding cases at the moment.</p>
                </td>
            </tr>
            <%}%>
        </tbody>           
    </table>  
</body>
</html>
