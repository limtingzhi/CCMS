<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="ccms.model.*"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<!DOCTYPE html>
<%    int empID = 0;

    if (user != null) {
        empID = user.getEmployeeID();
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Complain & Compliment Management System</title>
    </head>
    <body>
        <form action="processCase" method="POST">
            <table border="0">
                <tr><td><h3>Complaint's Details</h3></td></tr>
                <tr>
                    <td>Nric: <input type="text" name="person_nric" /> Name: <input type="text" name="person_name" /><br/></td>
                </tr>
                <tr>
                    <td>Email: <input type="text" name="person_email"/>Contact No.: <input type="text" name="person_contact_no" /><br/></td>
                </tr>

                <tr><td><h3>Case Information</h3></td></tr>
                <%
                    String errorMessage = (String) session.getAttribute("Error");
                    if (errorMessage != null) {
                        out.println("<font color='red'>" + errorMessage + "</font>");
                    }
                %>
                <tr> <td>
                        <input type="checkbox" name="type" value="complaint">Complaint <br>
                    </td>
                </tr>
                <tr>
                    <td>Difficulty: <select name="difficulty">
                            <option value="Easy">Easy</option>
                            <option value="Hard">Hard</option>
                            <option value="Super complex">Super Complex</option>
                        </select>
                        Issues: <select name="issues">
                            <option value="Work pass and permit">Work pass and permit</option>
                            <option value="Employment Practices">Employment Practices</option>
                            <option value="Workplace Safety and Health">Workplace Safety and Health</option>
                        </select><br/>
                    </td>
                </tr>
                <tr>
                    <td>Description:<br/>
                        <textarea name="complaintDescription" rows="10" cols="70"></textarea>
                    </td>
                </tr>

                <tr>
                    <td>
                        <input type="checkbox" name="type" value="compliment">Compliment
                    </td>
                </tr>
                <%
                    Connection con = null;
                    PreparedStatement ps = null;
                    ResultSet rs = null;
                    try {

//Class.forName("com.mysql.jdbc.Driver").newInstance();
                        con = ConnectionManager.getConnection();

                        Statement statement = con.createStatement();

                        rs = statement.executeQuery("select name from employee");

                %>
                <tr>
                    <td>Employee: <select name="employee_name">
                            <%  while (rs.next()) {%>
                            <option><%= rs.getString(1)%></option>
                            <% }
                                con.close();
                            %>
                        </select>
                        <%
                            con = ConnectionManager.getConnection();

                            statement = con.createStatement();

                            rs = statement.executeQuery("select name from department");

                        %>
                        Department: <select name="employee_dept">
                            <%  while (rs.next()) {%>
                            <option><%= rs.getString(1)%></option>
                            <% }
                                con.close();%>
                        </select>
                    </td>
                </tr>
                <%
                        //**Should I input the codes here?**
                    } catch (Exception e) {
                        out.println("wrong entry" + e);
                    }
                %>
                <tr>
                    <td>Description:<br/>
                        <textarea name="complimentDescription" rows="10" cols="70"></textarea>
                    </td>
                </tr>
                <tr><td><input type="submit" value="Create"/><button><a href="Home.jsp">Cancel</a></button><td/><tr/>
            </table>
            <input type="hidden" name="recorded_employee_id" value="<%=empID%>"/>
        </form>
    </body>
</html>
