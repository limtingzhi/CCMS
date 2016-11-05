<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ccms.model.SearchCaseDAO"%>
<%@page import="ccms.model.SearchCase"%>
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
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="assets/js/main.js"></script>
        <script type="text/javascript" src="assets/js/materialize.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Case</title>
    </head>
    <body>
        </br>
        <p style="border-bottom: 1px dotted #000000;">General Search</p>
        </br>
        
        <div id="search_div" align="center">
            <label for="wildcardsearch" class="control-label col-xs-2">Wild Card Search: </label>
            <input class="search_input" type="text" id="search" placeholder="Search"/>
            <a class="tooltipped tooltipped_circle" data-position="top" data-delay="50" data-tooltip=""></a>   
        </div>
        <br></br>
        <p style="border-bottom: 1px dotted #000000;">Advanced Search</p>
        </br>
        <form class="form-horizontal" align="center" name="login_form" method="post" action="SearchCase.do">
            <div class="form-group">
                <label for="caseid" class="control-label col-xs-2">Person's NRIC:</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="email" name="personnric">
                </div>
            </div>
            <div class="form-group">
                <label for="caseid" class="control-label col-xs-2">Case ID:</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control" id="emailresponse"  name="caseid">
                </div>
            </div>
            </br>
            <div class="form-group">
                <div class="col-xs-offset-2 col-xs-10">
                    <input type="submit" name="Search" class="btn btn-primary" value="Search">
                    <input type="reset" class="btn btn-primary" value="Reset">
                </div>
            </div>
        </form>
        
        <p> Search Result: </p>

        <%
            String errorMsg = (String) request.getAttribute("errorMsg");
            if (errorMsg != null) {
                out.println("<ul><li> Input error: " + errorMsg + "</li></ul>");
            }
        %>

        <table id="datatable" border="1" align="center">
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
            int count = 0;
            if (caseList != null && caseList.size() != 0) {
                for (int i = 0; i < caseList.size(); i++) {
                    out.println("<tr>");
                    SearchCase case1 = caseList.get(i);
                    out.println("<td>" + case1.getID() + "</td>");
                    out.println("<td>" + case1.getReportedDate() + "</td>");
                    out.println("<td>" + case1.getPersonName() + "</td>");
                    out.println("<td>" + case1.getPersonNric() + "</td>");
                    out.println("<td>" + case1.getType() + "</td>");
                    out.println("<td>" + case1.getStatus() + "</td>");
                    if (case1.getStatus().equals("Closed")) {
                        out.println("<td><a href=SearchViewCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + ">View Case</a> | Add Information | Archive </td>");
                    } else {
                        out.println("<td><a href=SearchViewCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + ">View Case</a> | <a href=AddInfo.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + ">Add Information</a> | <a href=ArchiveCase.jsp?caseid=" + case1.getID() + "&nric=" + case1.getPersonNric() + ">Archive</a></td>");
                    }

                    out.println("</tr>");
                }

                //if (count == 0) {
                //  out.println("<tr> <td colspan=7 align=center> No records found.</tr>");
                //}
            } else {
                out.println("<tr> <td colspan=7 align=center> No records found.</tr>");
            }
        %>
    </table>
</body>
</html>
