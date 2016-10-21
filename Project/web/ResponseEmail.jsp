<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Complaint & Compliment Management System</title>
    </head>
    <body>
        <h1>Respond to Case</h1>
        <p ><b>Case Details</b></p>
                                <form class="form-horizontal" name="login_form" method="post" action="ResponseEmail.do">
                                    <div class="form-group">
                                        <label for="caseid" class="control-label col-xs-2">Case ID:</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="caseid" name="caseid">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="caseid" class="control-label col-xs-2">Email:</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="email" name="email">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="caseid" class="control-label col-xs-2">Response:</label>
                                        <div class="col-xs-10">
                                            <input type="text" class="form-control" id="emailresponse"  name="emailresponse">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="col-xs-offset-2 col-xs-10">
                                            <input type="submit" name="Send Response" class="btn btn-primary" value="SendResponse">
                                            <input type="reset" class="btn btn-primary" value="Reset">
                                        </div>
                                    </div>
                                </form>
    </body>
</html>
