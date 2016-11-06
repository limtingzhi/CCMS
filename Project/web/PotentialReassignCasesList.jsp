<%-- 
    Document   : PotentailReassignCases
    Created on : 23 Oct, 2016, 5:35:25 PM
    Author     : asus user
--%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="ccms.model.*"%>
<%@include file="Protect.jsp" %>
<%    int empID = 0;

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
            String message = (String) request.getAttribute("reassignMessage");
            if (message != null) {
                out.println(message);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

            out.println("<h2>Outstanding Cases</h2>");
            out.println("Today's Date: " + sdf.format(new Date()));
            LinkedHashMap<Integer, ArrayList<String>> cList = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("potentialCases");
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
                    <td>Remarks</td>
                    <td></td>
                </tr>
            </thead>
            <tbody>
                <%
                    Set<Integer> keys = cList.keySet();
//                    boolean containResponse = true;
                    for (Integer caseID : keys) {
                        ArrayList<String> details = cList.get(caseID);
//                        containResponse = !details.get(details.size() - 1).equals("-");
                %>
            <form name="reassignCase" method="POST" action="DoReassignCaseController.do">        
                <tr>
                    <td><% out.println(caseID); %></td>
                    <%
                        String dateReceived = details.get(0);
                        String expectedDateResponse = details.get(1);
                        String difficulty = details.get(2);
                        String issue = details.get(3);
                        String employeeID = details.get(6);
                        String lastSaved = details.get(7);
                        String remarks = details.get(details.size()-1);
                    %>
                    <td><%=dateReceived%></td>
                    <td><%=expectedDateResponse%></td>
                    <td><%=difficulty%></td>
                    <td><%=issue%></td>
                    <td><%=lastSaved%></td>
                    <td><%=remarks%></td>
                    <td><input type="submit" value="Reassign Case" /></td>
                    <input type="hidden" name="expectedDateResponse" value="<%=expectedDateResponse%>" />
                    <input type="hidden" name="caseID" value="<%=caseID%>" />
                    <input type="hidden" name="employeeID" value="<%=employeeID%>" />
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
</body>
</html>
