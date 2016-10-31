<%-- 
    Document   : RespondCase
    Created on : 26 Oct, 2016, 4:41:47 PM
    Author     : asus user
--%>

<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.ArrayList"%>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Respond Case</title>
        <script>
        function myFunction() {
            var case_response = document.getElementById("response").value;
            var r = confirm("Response: " + case_response);
            if (r !== true) {
                return false;
            }
        }
        </script>
    </head>
    <body>
        <%
            ArrayList<String> caseDetails = (ArrayList<String>) request.getAttribute("caseDetails");
            if (caseDetails.size() > 0) {
                String caseID = caseDetails.get(0);
                String email = caseDetails.get(2);
                String complainantName = caseDetails.get(3);
                String description = caseDetails.get(5);
                String additional_info = caseDetails.get(6);
                String difficulty = caseDetails.get(7);
                String responseMsg = caseDetails.get(9);
                if(responseMsg == null) {
                    responseMsg = "";
                }
                out.println("<h1>Case ID: " + caseID + "</h1>");
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
                out.println("Additional Info: " + additional_info + "<br/>");
                out.println("Difficulty: " + difficulty + "<br/>");
                out.println("Issue: " + caseDetails.get(8));
        %>
        <br/>
        <%
            LinkedHashMap<Integer, ArrayList<String>> otherResponses = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("responses");
            if(otherResponses.size() > 0) {
                out.println("<h3><u>Other Responses</u></h3>");
                Set<Integer> keys = otherResponses.keySet();
                for(Integer k: keys) {
                    ArrayList<String> arr = otherResponses.get(k);
                    String name = arr.get(0);
                    out.println("Respondant Name: " + name + "<br/>");
                    for(int i = 1; i < arr.size(); i+=2) {
                        out.println("Response Date: " + arr.get(i) + "<br/>");
                        out.println("Response: " + arr.get(i+1) + "<br/>");
                        out.println("<br/>");
                    }
                }
            }
            String position = user.getPosition();
        %>
        <br>
        <form name="submitResponse" method="POST" action="RespondCase.do" onsubmit="return myFunction();">
            <b>Response: </b><br/>
            <textarea name="response" id="response" cols="60" rows="10" /><%=responseMsg%></textarea><br/>
            <input type="hidden" name="caseID" value="<% out.print(caseID); %>"/>
            <input type="hidden" name="employeeID" value="<% out.print(user.getEmployeeID());%>" />
            <input type="hidden" name="email" value="<%=email%>" />
            <input type="hidden" name="description" value="<%=description%>" />
            <input type="hidden" name="additional_info" value="<%=additional_info%>" />            
            <input type="hidden" name="complainantName" value="<%=complainantName%>" />
            <input type="submit" name="save" value="Save"/>
            <%
                if(((position.equalsIgnoreCase("Junior Executive") || position.equalsIgnoreCase("Senior Executive")) && difficulty.equalsIgnoreCase("easy")) || (position.equalsIgnoreCase("Manager") && difficulty.equalsIgnoreCase("hard")) || (position.equalsIgnoreCase("Director") && difficulty.equalsIgnoreCase("Super Complex"))) {
            %>
                    <input type="submit" name="save_and_email" value="Save & Email Complainant" />
            <%
                } else if(((position.equalsIgnoreCase("Junior Executive") || position.equalsIgnoreCase("Senior Executive")) && (difficulty.equalsIgnoreCase("hard") || difficulty.equalsIgnoreCase("super complex"))) || (position.equalsIgnoreCase("Manager") && difficulty.equalsIgnoreCase("super complex"))) {
            %>
                    <input type="submit" name="send_for_approval" value="Send For Approval" />
            <%
                }
            %>
        </form>    
        <%
            }
        %>
    </body>
</html>
