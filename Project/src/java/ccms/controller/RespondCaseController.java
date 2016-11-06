/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.controller;

import ccms.model.CaseDAO;
import ccms.model.DepartmentDAO;
import ccms.model.Employee;
import ccms.model.EmployeeDAO;
import ccms.model.PersonDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author asus user
 */
@WebServlet(name = "RespondCaseController", urlPatterns = {"/RespondCase.do"})
public class RespondCaseController extends HttpServlet {

    private CaseDAO caseDAO;
    private EmployeeDAO eDAO;
    
    public RespondCaseController() throws ParseException {
        DepartmentDAO dDAO = new DepartmentDAO();
        this.eDAO = new EmployeeDAO(dDAO);
        PersonDAO pDAO = new PersonDAO();
        this.caseDAO = new CaseDAO(eDAO, pDAO);
    }
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            String case_response = request.getParameter("response");
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));          
            int caseID = Integer.parseInt(request.getParameter("caseID"));
            String email = request.getParameter("email");
            String case_description = request.getParameter("description");
            String additional_info = request.getParameter("additional_info");
            String complainantName = request.getParameter("complainantName");

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new Date());
            int update = 0;            
            String message = "";
            //EMPLOYEE SAVE RESPONSE ONLY
            if(request.getParameter("save") != null) {
                update = caseDAO.saveCaseResponse(case_response, date, employeeID, caseID);
                //Return message
                if(update == 0) {
                    message = "Failed to save your response, please try again.";
                } else {
                    message = "Response for case ID (" + caseID + ") has been saved, you can continue at a later time.";
                }
            } else {
                update = caseDAO.updateCaseResponse(case_response, date, employeeID, caseID);
                String status = "Replied";
                //EMPLOYEE SAVE RESPONSE AND EMAIL COMPLAINANT
                if(request.getParameter("save_and_email") != null) {
                    int update1 = caseDAO.updateCaseStatus(status, caseID);
                                        
                    //Send email to complainant
                    if(update1 > 0) {
                        sendEmail(complainantName, case_description, additional_info, case_response, email, caseID);
                        message = "Response for case ID (" + caseID + ") has been sent to complainant.";
                    } else {
                        message = "Failed to update your response, please try again.";  
                    }
                //EMPLOYEE DRAFT EMAIL AND SEEK FOR IN-CHARGE'S APPROVAL
                } else if (request.getParameter("send_for_approval") != null) {
                    int inChargeID = eDAO.getEmployeeByID(employeeID).getInchargeID();                                    
                    Employee inCharge = eDAO.getEmployeeByID(inChargeID);
                    String inChargePosition = inCharge.getPosition();
                    status = "Pending - " + inChargePosition;
                    
                    int update1 = caseDAO.routeCaseToRespectiveInCharge(caseID, inChargeID, date); //Route case to in charge (e.g executive to manager)
                    int update2 = caseDAO.updateCaseStatus(status, caseID);

                    //Return message
                    if(update > 0 && update1 > 0 && update2 > 0) { //If all updates are updated successfully
                        message = "Response for case ID (" + caseID + ") has been sent for approval.";
                    } else {
                        message = "Failed to update your response, please try again.";                       
                    }
                }

            }
                                    
            RequestDispatcher rd = request.getRequestDispatcher("ListOfCases.jsp");
            request.setAttribute("message", message);
            rd.forward(request, response);
        } finally {
            out.close();
        }
    }

public void sendEmail(String complainantName, String description, String additional_info, String response, String email, int caseID) {        
        final String username = "ccms.mom.noreply@gmail.com";
	final String password = "helloworld01";
        // Recipient's email ID needs to be mentioned.
        String to = email;
 
        // Sender's email ID needs to be mentioned
        String from = "ccms.mom.noreply@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties props = System.getProperties();

        // Setup mail server
                  props.put("mail.smtp.auth", "true");
                  props.put("mail.smtp.starttls.enable", "true");
                  props.put("mail.smtp.host", "smtp.gmail.com");
                  props.put("mail.smtp.port", "587");

        // Get the default Session object.
        //Session session = Session.getDefaultInstance(props);
        Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                          @Override
                          protected PasswordAuthentication getPasswordAuthentication() {
                                  return new PasswordAuthentication(username, password);
                          }
                    });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                                     new InternetAddress(to));
            
            // Set Subject: header field
            message.setSubject("[MOM] Response to Case " + caseID);
            
            // Now set the actual message
            String email_content = "Dear " + complainantName + ", <br/><br/>"
                    + "This email is in response to your logged case, referencing Case ID " + caseID + "."
                    + "<br/><br/><b>Case Description: </b><br/>" + description
                    + "<br/><br/>" + "<b>Additional Information: </b><br/>" + additional_info
                    + "<br/><br/>" + "<b>Response: </b><br/>" + response;
            if(additional_info.equalsIgnoreCase("N/A")) {
                email_content = "Dear " + complainantName + ", <br/><br/>"
                    + "This email is in response to your logged case, referencing Case ID " + caseID + "."
                    + "<br/><br/><b>Case Description: </b><br/>" + description
                    + "<br/><br/>" + "<b>Response: </b><br/>" + response;
            }
            
            email_content += "<br/><br/> Best Regards, <br/> Complaint and Compliment Department "
                    + "<br/><br/> Please do not reply to this email. For further enquiries, please email to ccms@mom.com or call to 612345678.";
            message.setContent(email_content, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);
         } catch (MessagingException mex) {
         mex.printStackTrace();
      }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
