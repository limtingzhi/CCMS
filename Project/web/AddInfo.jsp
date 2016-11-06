<%-- 
    Document   : AddInfo
    Created on : Oct 29, 2016, 3:12:13 AM
    Author     : Feng Ru Chua
--%>

<%@page import="java.util.Set"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="ccms.model.SearchCase"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ccms.model.SearchCaseDAO"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Add Information</title>   
    </head>
    <body id="background-content">
        <%
            String caseID = (String) request.getParameter("caseid");
            String nric = (String) request.getParameter("nric");

            int caseIDInt = Integer.parseInt(caseID);

            SearchCaseDAO scDao = new SearchCaseDAO();
            ArrayList<SearchCase> caseList = scDao.searchCase(caseIDInt, nric);
            SearchCase sc = caseList.get(0);
            String complainantName = sc.getPersonName();
            String complainantNRIC = sc.getPersonNric();
            String email = sc.getEmail();
            String additional_info = sc.getAdditionalInfo();
            if (additional_info == null) {
                additional_info = "";
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
                <td><% out.println(sc.getContactNo());%></td>
            </tr>
            <tr>
                <td><b>NRIC</b></td>
                <td><% out.println(complainantNRIC);%></td>
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
                    <td><% out.println(caseList.get(0).getDifficulty());%></td>
                    <td rowspan="2"><b>Description</b></td>
                    <td rowspan="2"><% out.println(caseList.get(0).getDescription());%></td>
                </tr>
                <tr>
                    <td><b>Issue</b></td>
                    <td><% out.println(caseList.get(0).getIssue());%></td>
                </tr>
            </tbody>
        </table>

        <%
            CaseDAO caseDAO = new CaseDAO();
            LinkedHashMap<Integer, ArrayList<String>> otherResponses = caseDAO.getCaseResponses(Integer.parseInt(caseID));
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
                <%  }%>
            </tbody>
        </table>

        <form name="submitResponse" class="content-form" method="post" action="ProcessAddInfo.do">
            <p class="title-sub"><img src="img/add.png">Additional Info</p>
            <textarea name="addInfo" id="response"/><%=additional_info%> </textarea><br/>
        <input type="hidden" name="caseID" value="<%=caseID%>"/>
        <input type="hidden" name="complainantName" value="<%=complainantName%>"/>
        <input type="hidden" name="email" value="<%=email%>"/>
        <input type="submit" value="Add Additional Info"/>
        <input type="button" onclick="history.go(-1);" value="Back">
    </form>
</body>
</html>
