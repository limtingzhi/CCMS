<%@page import="java.sql.*"%>
<%@page import="java.text.*"%>
<%@page import="java.util.*"%>
<%@page import="ccms.model.*"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>

<!DOCTYPE html>
<%
    int empID = 0;

    if (user != null) {
        empID = user.getEmployeeID();
    }

    String person_nric = (String) request.getAttribute("person_nric");
    if (person_nric == null) {
        person_nric = "";
    }
    String person_name = (String) request.getAttribute("person_name");
    if (person_name == null) {
        person_name = "";
    }
    String person_email = (String) request.getAttribute("person_email");
    if (person_email == null) {
        person_email = "";
    }
    String person_contact = (String) request.getAttribute("person_contact_no");
    if (person_contact == null) {
        person_contact = "";
    }
    String[] type = (String[]) request.getAttribute("type");
    String complaintDescription = (String) request.getAttribute("complaintDescription");
    if (complaintDescription == null) {
        complaintDescription = "";
    }
    String complimentDescription = (String) request.getAttribute("complimentDescription");
    if (complimentDescription == null) {
        complimentDescription = "";
    }
    String difficulty = (String) request.getAttribute("difficulty");
    String issues = (String) request.getAttribute("issues");
    String empOrDept = (String) request.getAttribute("employee_or_dept");
    String empName = (String) request.getAttribute("employee_name");
    String deptName = (String) request.getAttribute("employee_dept");

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <script src="js/jquery-3.1.1.min.js"></script>
        <script>
            $(function() {
                $('#checkboxComplaint, #checkboxCompliment').click(function() {
                    var cb1 = $('#checkboxComplaint').is(':checked');
                    $('#disabledComplaintDifficulty').prop('disabled', !cb1);
                    $('#disabledComplaintDesc').prop('disabled', !cb1);
                    $('#disabledComplaintDesc').prop('required', cb1);
                    $('#disabledComplaintIssue').prop('disabled', !cb1);
                    var cb2 = $('#checkboxCompliment').is(':checked');
                    $('#disabledComplimentEmployeeDept').prop('disabled', !cb2);
                    $('#disabledComplimentEmployee').prop('disabled', !cb2);
                    $('#disabledComplimentDesc').prop('required', cb2);
                    $('#disabledComplimentDesc').prop('disabled', !cb2);
                    $('#disabledComplimentDept').prop('disabled', !cb2);
                });
            });

            $(document).ready(function() {
                $('#disabledComplimentEmployeeDept').on('change', function() {
                    if (this.value === 'Employee') {
                        $("#employeeLabel").show();
                        $("#employeeDropdown").show();
                        $("#dept").hide();
                        $("#deptDropdown").hide();
                    } else {
                        $("#dept").show();
                        $("#deptDropdown").show();
                        $("#employeeLabel").hide();
                        $("#employeeDropdown").hide();
                    }
                });
            });
        </script>
        <title>MOM CCMS - Create Case</title>
    </head>
    <body id="background-content">
        <p class="title"><img src="img/create.png">Create Case</p>
        <div class="handle-msg">
            <%
                String message = (String) request.getAttribute("message");
                if (message != null) {
                    out.println(message);
                }

                String successMsg = (String) session.getAttribute("successMsg");
                if (successMsg != null) {
                    out.println(successMsg);
                    session.removeAttribute("successMsg");
                }
            %>
        </div>

        <%
            DepartmentDAO deptDAO = new DepartmentDAO();
            EmployeeDAO employeeDAO = new EmployeeDAO(deptDAO);

            ArrayList<Department> deptList = deptDAO.getAllDept();
            ArrayList<Employee> employeeList = employeeDAO.getAllEmployee();
        %>

        <form action="processCase" method="POST" class="content-form">
            <table class="no-fixed">
                <thead>
                    <tr>
                        <td colspan=4>Person Details</td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><b>NRIC</b></td>
                        <td><input type="text" name="person_nric" value="<%=person_nric%>" required /></td>
                        <td><b>Contact No</b></td>
                        <td><input type="text" name="person_contact_no" onkeypress='return event.charCode >= 48 && event.charCode <= 57' value="<%=person_contact%>" required /></td>                   
                    </tr>
                    <tr>
                        <td><b>Name</b></td>
                        <td><input type="text" name="person_name" value="<%=person_name%>" required /></td>
                        <td><b>Email</b></td>
                        <td><input type="text" name="person_email" value="<%=person_email%>"required /></td>
                    </tr>
                </tbody>
            </table>

            <table class="create-case">
                <colgroup>
                    <col class="col-1">
                    <col class="col-2">
                    <col class="col-3">
                    <col class="col-4">
                    <col class="col-5">
                </colgroup>
                <thead>
                    <tr>
                        <td colspan=5>Complaint Case Details</td>
                    </tr>
                </thead>
                <% if (type != null && (type[0].equals("Complaint") || (type.length == 2 && type[1].equals("Complaint")))) {%>
                <tbody>
                    <tr>
                        <td rowspan="2">
                            <div class="checkbox-design">
                                <input id="checkboxComplaint" type="checkbox" name="type" value="Complaint" checked>
                                <label class="checkbox" for="checkboxComplaint"></label>
                            </div>
                            <p class="checkboxLabel">Complaint</p>
                        </td>
                        <td><b>Difficulty</b></td>
                        <td>
                            <select name="difficulty" id="disabledComplaintDifficulty">
                                <% if (difficulty == null || difficulty.equals("Easy")) {%>
                                <option value="Easy" selected="selected">Easy</option>
                                <option value="Hard">Hard</option>
                                <option value="Super Complex">Super Complex</option>
                                <% } else if (difficulty.equals("Hard")) {%>
                                <option value="Easy">Easy</option>
                                <option value="Hard" selected="selected">Hard</option>
                                <option value="Super Complex">Super Complex</option>
                                <% } else if (difficulty.equals("Super Complex")) {%>
                                <option value="Easy">Easy</option>
                                <option value="Hard">Hard</option>
                                <option value="Super Complex" selected="selected">Super Complex</option>
                                <% }%>
                            </select>
                        </td>
                        <td rowspan="2"><b>Description</b></td>
                        <td rowspan="2"><textarea name="complaintDescription" id="disabledComplaintDesc"><%=complaintDescription%></textarea></td>
                    </tr>
                    <tr>
                        <td><b>Issue</b></td>
                        <td>
                            <select name="issues" id="disabledComplaintIssue">
                                <% if (issues == null || issues.equals("Work Pass and Permit")) {%>
                                <option value="Work Pass and Permit" selected="selected">Work Pass and Permit</option>
                                <option value="Employment Practices">Employment Practices</option>
                                <option value="Workplace Safety and Health">Workplace Safety and Health</option>
                                <% } else if (issues.equals("Employment Practices")) {%>
                                <option value="Work Pass and Permit">Work Pass and Permit</option>
                                <option value="Employment Practices" selected="selected">Employment Practices</option>
                                <option value="Workplace Safety and Health">Workplace Safety and Health</option>
                                <% } else if (issues.equals("Workplace Safety and Health")) {%>
                                <option value="Work Pass and Permit">Work Pass and Permit</option>
                                <option value="Employment Practices">Employment Practices</option>
                                <option value="Workplace Safety and Health" selected="selected">Workplace Safety and Health</option>
                                <% }%>
                            </select>
                        </td>
                    </tr>
                </tbody>
                <% } else {%>
                <tbody>
                    <tr>
                        <td rowspan="2">
                            <div class="checkbox-design">
                                <input id="checkboxComplaint" type="checkbox" name="type" value="Complaint">
                                <label class="checkbox" for="checkboxComplaint"></label>
                            </div>
                            <p class="checkboxLabel">Complaint</p>
                        </td>
                        <td><b>Difficulty</b></td>
                        <td>
                            <select name="difficulty" id="disabledComplaintDifficulty" disabled>
                                <% if (difficulty == null || difficulty.equals("Easy")) {%>
                                <option value="Easy" selected="selected">Easy</option>
                                <option value="Hard">Hard</option>
                                <option value="Super Complex">Super Complex</option>
                                <% } else if (difficulty.equals("Hard")) {%>
                                <option value="Easy">Easy</option>
                                <option value="Hard" selected="selected">Hard</option>
                                <option value="Super Complex">Super Complex</option>
                                <% } else if (difficulty.equals("Super Complex")) {%>
                                <option value="Easy">Easy</option>
                                <option value="Hard">Hard</option>
                                <option value="Super Complex" selected="selected">Super Complex</option>
                                <% }%>
                            </select>
                        </td>
                        <td rowspan="2"><b>Description</b></td>
                        <td rowspan="2"><textarea name="complaintDescription" id="disabledComplaintDesc" disabled><%=complaintDescription%></textarea></td>
                    </tr>
                    <tr>
                        <td><b>Issue</b></td>
                        <td>
                            <select name="issues" id="disabledComplaintIssue" disabled>
                                <% if (issues == null || issues.equals("Work Pass and Permit")) {%>
                                <option value="Work Pass and Permit" selected="selected">Work Pass and Permit</option>
                                <option value="Employment Practices">Employment Practices</option>
                                <option value="Workplace Safety and Health">Workplace Safety and Health</option>
                                <% } else if (issues.equals("Employment Practices")) {%>
                                <option value="Work Pass and Permit">Work Pass and Permit</option>
                                <option value="Employment Practices" selected="selected">Employment Practices</option>
                                <option value="Workplace Safety and Health">Workplace Safety and Health</option>
                                <% } else if (issues.equals("Workplace Safety and Health")) {%>
                                <option value="Work Pass and Permit">Work Pass and Permit</option>
                                <option value="Employment Practices">Employment Practices</option>
                                <option value="Workplace Safety and Health" selected="selected">Workplace Safety and Health</option>
                                <% }%>
                            </select>
                        </td>
                    </tr>
                </tbody>
                <% }%>
            </table>

            <table class="create-case">
                <colgroup>
                    <col class="col-1">
                    <col class="col-2">
                    <col class="col-3">
                    <col class="col-4">
                    <col class="col-5">
                </colgroup>
                <thead>
                    <tr>
                        <td colspan=5>Compliment Case Details</td>
                    </tr>
                </thead>
                <% if (type != null && (type[0].equals("Compliment") || (type.length == 2 && type[1].equals("Compliment")))) {%>
                <tbody>
                    <tr>
                        <td rowspan="2">
                            <div class="checkbox-design">
                                <input id="checkboxCompliment" type="checkbox" name="type" value="Compliment" checked>
                                <label class="checkbox" for="checkboxCompliment"></label>
                            </div>
                            <p class="checkboxLabel">Compliment</p>
                        </td>
                        <td><b>Employee/<br>Department</b></td>
                        <td>
                            <select name="employee_or_dept" id="disabledComplimentEmployeeDept">
                                <% if (empOrDept == null || empOrDept.equals("Employee")) {%>
                                <option value="Employee" selected="selected">Employee</option>
                                <option value="Department">Department</option>
                                <% } else if (empOrDept.equals("Department")) {%>
                                <option value="Employee">Employee</option>
                                <option value="Department" selected="selected">Department</option>
                                <% }%>
                            </select>
                        </td>
                        <td rowspan="2"><b>Description</b></td>
                        <td rowspan="2"><textarea name="complimentDescription" id="disabledComplimentDesc"><%=complimentDescription%></textarea></td>
                    </tr>
                    <tr>
                        <% if (empOrDept == null || empOrDept.equals("Employee")) {%>
                        <td id="employeeLabel"><b>Employee</b></td>
                        <td id="employeeDropdown">
                            <select name="employee_name" id="disabledComplimentEmployee">
                                <%
                                    for (Employee employee : employeeList) {
                                        if (empName != null && employee.getName().equals(empName.trim())) {
                                %>
                                <option value="<%out.println(employee.getName());%>" selected="selected"><%out.println(employee.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(employee.getName());%>"><%out.println(employee.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td id="dept" hidden><b>Department</b></td>
                        <td id="deptDropdown" hidden>
                            <select name="employee_dept" id="disabledComplimentDept">
                                <%
                                    for (Department dept : deptList) {
                                        if (deptName != null && dept.getName().equals(deptName.trim())) {
                                %>
                                <option value="<%out.println(dept.getName());%>" selected="selected"><%out.println(dept.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(dept.getName());%>"><%out.println(dept.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <% } else {%>
                        <td id="employeeLabel" hidden><b>Employee</b></td>
                        <td id="employeeDropdown" hidden>
                            <select name="employee_name" id="disabledComplimentEmployee">
                                <%
                                    for (Employee employee : employeeList) {
                                        if (empName != null && employee.getName().equals(empName.trim())) {
                                %>
                                <option value="<%out.println(employee.getName());%>" selected="selected"><%out.println(employee.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(employee.getName());%>"><%out.println(employee.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td id="dept"><b>Department</b></td>
                        <td id="deptDropdown">
                            <select name="employee_dept" id="disabledComplimentDept">
                                <%
                                    for (Department dept : deptList) {
                                        if (deptName != null && dept.getName().equals(deptName.trim())) {
                                %>
                                <option value="<%out.println(dept.getName());%>" selected="selected"><%out.println(dept.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(dept.getName());%>"><%out.println(dept.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <% }%>
                    </tr>
                </tbody>
                <% } else {%>
                <tbody>
                    <tr>
                        <td rowspan="2">
                            <div class="checkbox-design">
                                <input id="checkboxCompliment" type="checkbox" name="type" value="Compliment">
                                <label class="checkbox" for="checkboxCompliment"></label>
                            </div>
                            <p class="checkboxLabel">Compliment</p>
                        </td>
                        <td><b>Employee/<br>Department</b></td>
                        <td>
                            <select name="employee_or_dept" id="disabledComplimentEmployeeDept" disabled>
                                <% if (empOrDept == null || empOrDept.equals("Employee")) {%>
                                <option value="Employee" selected="selected">Employee</option>
                                <option value="Department">Department</option>
                                <% } else if (empOrDept.equals("Department")) {%>
                                <option value="Employee">Employee</option>
                                <option value="Department" selected="selected">Department</option>
                                <% }%>
                            </select>
                        </td>
                        <td rowspan="2"><b>Description</b></td>
                        <td rowspan="2"><textarea name="complimentDescription" id="disabledComplimentDesc" disabled><%=complimentDescription%></textarea></td>
                    </tr>
                    <tr>
                        <% if (empOrDept == null || empOrDept.equals("Employee")) {%>
                        <td id="employeeLabel"><b>Employee</b></td>
                        <td id="employeeDropdown">
                            <select name="employee_name" id="disabledComplimentEmployee" disabled>
                                <%
                                    for (Employee employee : employeeList) {
                                        if (empName != null && employee.getName().equals(empName.trim())) {
                                %>
                                <option value="<%out.println(employee.getName());%>" selected="selected"><%out.println(employee.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(employee.getName());%>"><%out.println(employee.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td id="dept" hidden><b>Department</b></td>
                        <td id="deptDropdown" hidden>
                            <select name="employee_dept" id="disabledComplimentDept" disabled>
                                <%
                                    for (Department dept : deptList) {
                                        if (deptName != null && dept.getName().equals(deptName.trim())) {
                                %>
                                <option value="<%out.println(dept.getName());%>" selected="selected"><%out.println(dept.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(dept.getName());%>"><%out.println(dept.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <% } else {%>
                        <td id="employeeLabel" hidden><b>Employee</b></td>
                        <td id="employeeDropdown" hidden>
                            <select name="employee_name" id="disabledComplimentEmployee" disabled>
                                <%
                                    for (Employee employee : employeeList) {
                                        if (empName != null && employee.getName().equals(empName.trim())) {
                                %>
                                <option value="<%out.println(employee.getName());%>" selected="selected"><%out.println(employee.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(employee.getName());%>"><%out.println(employee.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <td id="dept"><b>Department</b></td>
                        <td id="deptDropdown">
                            <select name="employee_dept" id="disabledComplimentDept" disabled>
                                <%
                                    for (Department dept : deptList) {
                                        if (deptName != null && dept.getName().equals(deptName.trim())) {
                                %>
                                <option value="<%out.println(dept.getName());%>" selected="selected"><%out.println(dept.getName());%></option>
                                <%} else {%>
                                <option value="<%out.println(dept.getName());%>"><%out.println(dept.getName());%></option>
                                <% }
                                    }%>
                            </select>
                        </td>
                        <% }%>
                    </tr>
                </tbody>
                <% }%>
            </table>

            <input type="submit" value="Create"/>
            <input type="button" onclick="history.go(-1);" value="Back">
            <input type="hidden" name="recorded_employee_id" value="<%=empID%>"/>
        </form>
    </body>
</html>