<%-- 
    Document   : RespondCase
    Created on : 26 Oct, 2016, 4:41:47 PM
    Author     : asus user
--%>

<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.ArrayList"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Respond Case</title>
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
    <body id="background-content">
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
                String addOnDate = caseDetails.get(caseDetails.size() - 1);

                if (responseMsg == null) {
                    responseMsg = "";
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
            LinkedHashMap<Integer, ArrayList<String>> otherResponses = (LinkedHashMap<Integer, ArrayList<String>>) request.getAttribute("responses");
        %>
        <table class="case-details">
            <colgroup>
                <col class="col-1">
                <col class="col-2">
                <col class="col-3">
                <col class="col-4">
            </colgroup>
            <thead>
                <tr>
                    <td colspan=4>Responses</td>
                </tr>
            </thead>
            <%
                if (otherResponses.size() > 0) {
                    Set<Integer> keys = otherResponses.keySet();
                    for (Integer k : keys) {
                        ArrayList<String> arr = otherResponses.get(k);
                        String name = arr.get(0);
                        String[] nameArr = name.split(",");
                        int size = arr.size() - 1;
            %>
            <tbody>
                <tr>
                    <td rowspan="<% out.println(size);%>"><b>Respondent</b></td>
                    <td rowspan="<% out.println(size);%>">
                        <% out.println(nameArr[0].trim());%><br>
                        <% out.println("(" + nameArr[1].trim() + ")");%>
                    </td>
                    <% for (int i = 1; i < arr.size(); i += 2) {%>
                    <td><b>Response Date</b></td>
                    <td><% out.println(arr.get(i));%></td>
                </tr>
                <tr>
                    <td><b>Response</b></td>
                    <td><% out.println(arr.get(i + 1));%></td>
                </tr>
                <%
                        }
                    }
                } else {%>
                <tr>
                    <td colspan="4" >
                        <p style="text-align: center;">There are no responses.</p>
                    </td>
                </tr>
                <%  }
                    String position = user.getPosition();
                %>
            </tbody>
        </table>

        <form name="submitResponse" class="content-form" method="POST" action="RespondCase.do" onsubmit="return myFunction();">
            <p class="title-sub"><img src="img/response.png">Response</p>
            <textarea name="response" id="response"/><%=responseMsg%></textarea><br/>
        <input type="hidden" name="caseID" value="<% out.print(caseID);%>"/>
        <input type="hidden" name="employeeID" value="<% out.print(user.getEmployeeID());%>" />
        <input type="hidden" name="email" value="<%=email%>" />
        <input type="hidden" name="description" value="<%=description%>" />
        <input type="hidden" name="additional_info" value="<%=additional_info%>" />            
        <input type="hidden" name="complainantName" value="<%=complainantName%>" />
        <input type="submit" name="save" value="Save"/>
        <% if (((position.equalsIgnoreCase("Junior Executive") || position.equalsIgnoreCase("Senior Executive")) && difficulty.equalsIgnoreCase("easy")) || (position.equalsIgnoreCase("Manager") && difficulty.equalsIgnoreCase("hard")) || (position.equalsIgnoreCase("Director") && difficulty.equalsIgnoreCase("Super Complex"))) {%>
        <input type="submit" name="save_and_email" value="Save & Email Complainant" />
        <%  } else if (((position.equalsIgnoreCase("Junior Executive") || position.equalsIgnoreCase("Senior Executive")) && (difficulty.equalsIgnoreCase("hard") || difficulty.equalsIgnoreCase("super complex"))) || (position.equalsIgnoreCase("Manager") && difficulty.equalsIgnoreCase("super complex"))) {%>
        <input type="submit" name="send_for_approval" value="Send For Approval" />
        <%  }%>            
        <input type="button" onclick="history.go(-1);" value="Back">
    </form>    
    <% }%>
</body>
</html>
