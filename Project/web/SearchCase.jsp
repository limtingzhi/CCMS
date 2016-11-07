<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ccms.model.SearchCaseDAO"%>
<%@page import="ccms.model.SearchCase"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>

<!DOCTYPE html>
<%
    SearchCaseDAO caseDao = new SearchCaseDAO();
    ArrayList<SearchCase> caseList = null;
    caseList = caseDao.getAllCase();

    ArrayList<SearchCase> searchCaseList = (ArrayList<SearchCase>) request.getAttribute("searchResult");
    String errorMsg1 = (String) request.getAttribute("errorMsg");
    String noResultStatus = (String) request.getAttribute("noResult");

    if (searchCaseList != null && searchCaseList.size() != 0) {
        caseList = searchCaseList;
    }

    if (noResultStatus != null && noResultStatus.equals("No Records Found. ") || errorMsg1 != null) {
        caseList = null;
    }
%>
<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="js/main.js"></script>
        <title>MOM CCMS - Search Cases</title>
    </head>
    <body id="background-content">
        <p class="title"><img src="img/search.png">Search Case</p>
        <div id="search-padding">
            <div class="search-div">
                <p>General Search</p>
                <table>
                    <colgroup>
                        <col class="col-1">
                        <col class="col-2">
                    </colgroup>
                    <tbody>
                        <tr>
                            <td><label for="wildcardsearch"><b>Wild Card Search</b></label></td>
                            <td><input type="text" id="search"/></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div class="search-div">
                <p>Advanced Search</p>
                <form name="login_form" method="post" action="SearchCase.do">
                    <table>
                        <colgroup>
                            <col class="col-1">
                            <col class="col-2">
                        </colgroup>
                        <tbody>
                            <tr>
                                <td><label for="caseid"><b>Person NRIC</b></label></td>
                                <td><input type="text" id="email" name="personnric"></td>
                            </tr>
                            <tr>
                                <td><label for="caseid"><b>Case ID</b></label></td>
                                <td><input type="text" id="emailresponse" name="caseid"></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <input type="submit" name="Search" value="Search">
                                    <input type="reset" value="Reset">
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </form>
            </div>
        </div>

        <p class="title-sub"><img src="img/case.png">Search Result</p>
        <div class="handle-msg">
            <%
                String errorMsg = (String) request.getAttribute("errorMsg");
                if (errorMsg != null) {
                    out.println(errorMsg);
                }
            %>
        </div>   

        <table id="datatable">
            <thead>
            <th>Case ID</th>
            <th>Reported Date</th>
            <th>Name</th>
            <th>NRIC</th>
            <th>Case Type</th>
            <th>Status</th>
            <th>Actions</th>
        </thead>
        <%
            if (caseList != null && caseList.size() != 0) {
                for (int i = 0; i < caseList.size(); i++) {
                    SearchCase case1 = caseList.get(i);
        %>
        <tbody>
            <tr>
                <td><%=case1.getID()%></td>
                <td><%=case1.getReportedDate()%></td>
                <td><%=case1.getPersonName()%></td>
                <td><%=case1.getPersonNric()%></td>
                <td><%=case1.getType()%></td>
                <%if (case1.getType().equalsIgnoreCase("compliment")) {%>
                <td>-</td>
                <%} else {%>
                <td><%=case1.getStatus()%></td>
                <%}%>

                <%
                            if (case1.getType().equalsIgnoreCase("complaint") && case1.getStatus().equals("Closed")) {
                                out.println("<td><a href=SearchViewCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + "><img src=img/case.png></a></td>");
                            } else if (case1.getType().equalsIgnoreCase("complaint")) {
                                out.println("<td><a href=SearchViewCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + "><img src=img/case.png></a> <a href=AddInfo.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + "><img src=img/add.png></a> <a href=ArchiveCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + "><img src=img/close.png></a></td>");
                            } else if (case1.getType().equalsIgnoreCase("compliment")) {
                                out.println("<td><a href=SearchViewCaseCompliment.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + "><img src=img/case.png></a></td>");
                            }
                            out.println("</tr>");
                        }
                    } else {
                        out.println("<tr><td colspan=7 align=center><p>There are no outstanding cases at the moment.</p></td></tr>");
                    }
                %>

            <tr id="no-record" hidden><td colspan=7 align=center><p>There are no outstanding cases at the moment.</p></td></tr>
        </tbody>
    </table>
</body>
</html>
