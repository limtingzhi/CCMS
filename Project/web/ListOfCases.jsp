<%-- 
    Document   : ListOfCases
    Created on : 23 Oct, 2016, 5:35:25 PM
    Author     : asus user
--%>

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
        <title>MOM CCMS - Handle Cases</title>
    </head>
    <body>
        <p id="title">Handle Cases</p>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null) {
                out.println(message);
            }

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
                    <td>Action</td>
                </tr>
            </thead>
            <tbody>
        <%            
            Set<Integer> keys = cList.keySet();
            boolean containResponse = true;
            String personNRIC = null;
            for(Integer caseID: keys) {
                ArrayList<String> details = cList.get(caseID);  
                containResponse = !details.get(details.size()-1).equals("-");
                personNRIC = details.get(0);
        %>
            <form name="caseRespond" method="POST" action="CaseController.do">        
            <tr>
                <td><% out.println(caseID); %></td>
                <% for(int i = 1; i < details.size(); i++) { %>
                <td><% out.println(details.get(i)); %></td>
                <% } %>
                <td>
                <% 
                    if (containResponse) {
                %>
                <input type="submit" value="Continue" />
                <%
                    } else {
                %>    
                <input type="submit" value="Respond" />
                <%  
                    }
                %>
                <input type="submit" value="Archieve" onclick="this.form.action='ArchiveCase.jsp?caseid=<%=caseID%>&nric=<%=personNRIC%>'"/>
                </td>
                <input type="hidden" value="<%=personNRIC%>" name="personNRIC">
                <input type="hidden" value="<%=caseID%>" name="caseID">
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
