<%-- 
    Document   : StatisticBoard
    Created on : Nov 7, 2016, 11:03:42 AM
    Author     : Wayne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="NavigationBar.jsp" %>
<%@include file="Protect.jsp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
        <link rel="stylesheet" href="css/home.css">
        <link rel="stylesheet" href="css/content.css">
        <title>MOM CCMS - Statistics</title>
    </head>
    <body id="background-content">
        <p class="title"><img src="img/statistics.png">Statistics</p>
        <div class='tableauPlaceholder' id='viz1478523946986' style='position: relative'>
            <noscript>
            <a href='#'>
                <img alt='Dashboard 1 ' src='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;cc&#47;ccms_dashboard&#47;Dashboard1&#47;1_rss.png' style='border: none' />
            </a>
            </noscript>
            <object class='tableauViz'  style='display:none;'>
                <param name='host_url' value='https%3A%2F%2Fpublic.tableau.com%2F' />
                <param name='site_root' value='' />
                <param name='name' value='ccms_dashboard&#47;Dashboard1' />
                <param name='tabs' value='no' />
                <param name='toolbar' value='yes' />
                <param name='static_image' value='https:&#47;&#47;public.tableau.com&#47;static&#47;images&#47;cc&#47;ccms_dashboard&#47;Dashboard1&#47;1.png' />
                <param name='animate_transition' value='yes' />
                <param name='display_static_image' value='yes' />
                <param name='display_spinner' value='yes' />
                <param name='display_overlay' value='yes' />
                <param name='display_count' value='yes' />
            </object>
        </div>                
        <script type='text/javascript'>
            var divElement = document.getElementById('viz1478523946986');
            var vizElement = divElement.getElementsByTagName('object')[0];
            vizElement.style.width = '100%';
//            vizElement.style.height = (divElement.offsetWidth * 0.75) + 'px';
            vizElement.style.height = '450px';
            var scriptElement = document.createElement('script');
            scriptElement.src = 'https://public.tableau.com/javascripts/api/viz_v1.js';
            vizElement.parentNode.insertBefore(scriptElement, vizElement);
        </script>
    </body>
</html>
