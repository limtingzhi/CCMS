<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="Protect.jsp" %>

<%
    // Remove session of login user and redirect to Login page
    session.removeAttribute("loginuser");
    session.invalidate();
    response.sendRedirect("Index.jsp");
%>