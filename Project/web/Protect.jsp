<%@page import="ccms.model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Employee user = (Employee) session.getAttribute("loginuser");

    // Not authenticated, force user to authenticate
    if (user == null) {
        response.sendRedirect("Index.jsp");
        return;
    }
%>