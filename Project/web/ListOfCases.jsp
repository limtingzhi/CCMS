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
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Handle Cases</title>
    </head>
    <body id="background-content">
        <p class="title"><img src="img/handle.png">Handle Case</p>
        <div class="handle-msg">
            <%
                String message = (String) request.getAttribute("message");
                if (message != null) {
                    out.println(message);
                }
            %>
        </div>   
        <table>
            <thead>
                <tr>
                    <td>Case ID</td>
                    <td>Received Date</td>
                    <td>Expected Response Date</td>
                    <td>Difficulty</td>
                    <td>Issue</td>
                    <td>Last Saved</td>
                    <td>Action</td>
                </tr>
            </thead>
            <tbody>
                <%
                    LinkedHashMap<Integer, ArrayList<String>> cList = (LinkedHashMap<Integer, ArrayList<String>>) new CaseDAO(new EmployeeDAO(new DepartmentDAO()), new PersonDAO()).getAllCasebyEmpID(empID);
                    if (cList.size() > 0) {
                        Set<Integer> keys = cList.keySet();
                        boolean containResponse = true;
                        String personNRIC = null;
                        for (Integer caseID : keys) {
                            ArrayList<String> details = cList.get(caseID);
                            containResponse = !details.get(details.size() - 1).equals("-");
                            personNRIC = details.get(0);
                %>
            <form name="caseRespond" method="POST" action="CaseController.do">        
                <tr>
                    <td><% out.println(caseID);%></td>
                    <% for (int i = 1; i < details.size(); i++) {%>
                    <td><% out.println(details.get(i));%></td>
                    <% }%>
                    <td>
                        <div class="img-btn">
                            <% if (containResponse) {%>
                            <input type="image" src="img/response.png" value="Continue" title="Respond"/>
                            <% } else {%>    
                            <input type="image" src="img/response.png" value="Respond" title="Respond"/>
                            <% }%>
                            <input type="image" src="img/close.png" value="Archive" onclick="this.form.action = 'ArchiveCase.jsp?caseid=<%=caseID%>&nric=<%=personNRIC%>'" title="Close"/>
                        </div>
                    </td>
                <input type="hidden" value="<%=personNRIC%>" name="personNRIC">
                <input type="hidden" value="<%=caseID%>" name="caseID">
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