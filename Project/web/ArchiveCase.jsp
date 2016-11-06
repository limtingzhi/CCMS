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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Close Case</title>
    </head>
    <body id="background-content">
        <%
            String caseID = (String) request.getParameter("caseid");
            String nric = (String) request.getParameter("nric");

            int caseIDInt = Integer.parseInt(caseID);

            SearchCaseDAO scDao = new SearchCaseDAO();
            ArrayList<SearchCase> caseList = scDao.searchCase(caseIDInt, nric);
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
                <td><% out.println(caseList.get(0).getPersonName());%></td>
                <td><b>Contact</b></td>
                <td><% out.println(caseList.get(0).getContactNo());%></td>
            </tr>
            <tr>
                <td><b>NRIC</b></td>
                <td><% out.println(caseList.get(0).getPersonNric());%></td>
                <td><b>Email</b></td>
                <td><% out.println(caseList.get(0).getEmail());%></td>
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
                <tr>
                    <td><b>Additional Info Date</b></td>
                    <td><% out.println(caseList.get(0).getAdditionalInfoDate());%></td>
                    <td><b>Additional Info</b></td>
                    <td><% out.println(caseList.get(0).getAdditionalInfo());%></td>
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

        <form name="submitResponse" class="content-form" method="post" action="ProcessArchive.do">
            <p class="title-sub"><img src="img/close.png">Closing Remark</p>
            <textarea name="closingremark" id="response"></textarea><br/>
            <input type="hidden" name="caseID" value="<% out.print(caseID);%>"/>
            <input type="submit" value="Close Case"/>
            <input type="button" onclick="history.go(-1);" value="Back">
        </form>
    </body>
</html>
