/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccms.controller;

import ccms.model.SearchCaseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
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
 * @author Feng Ru Chua
 */
@WebServlet(name = "AddInfo", urlPatterns = {"/ProcessAddInfo.do"})
public class AddInfoController extends HttpServlet {

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
        RequestDispatcher dispatcher = request.getRequestDispatcher("SearchCase.jsp");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String todayDate = dateFormat.format(new Date());
        
        String additional_info = (String) request.getParameter("addInfo");
        String case_id = (String) request.getParameter("caseID");
        int caseID = Integer.parseInt(case_id);
        String complainantName = (String) request.getParameter("complainantName");
        String email = (String) request.getParameter("email");
        
        int case_id_int = 0;
        
        try{
            case_id_int = Integer.parseInt(case_id);
        } catch(NumberFormatException nfe){
            
        }
        
        if (additional_info == null || additional_info.equals("")) {
            request.setAttribute("errorMsg", "Please fill in additional information to be added.");
        }else{
            SearchCaseDAO scDao = new SearchCaseDAO();
            boolean revertCaseToFirstOwner = scDao.revertCaseToFirstOwner(case_id_int, todayDate);
            if(revertCaseToFirstOwner) {
                boolean updateProc = scDao.addInfo(additional_info, case_id_int, todayDate);
                if(updateProc == true) {
                    sendEmail(complainantName, additional_info, email, caseID);
                } else {
                    request.setAttribute("errorMsg", "Adding additional info failed. Please try again.");
                }
            } else {
                request.setAttribute("errorMsg", "Adding additional info failed. Please try again.");
            }
        }
        
        dispatcher.forward(request, response);
        return;
    }
    
    public void sendEmail(String complainantName, String additional_info, String email, int caseID) {        
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
            message.setSubject("[MOM] Additional Information for Case " + caseID);
            
            // Now set the actual message
            String email_content = "Dear " + complainantName + ", <br/><br/>"
                    + "Your additional feedback for case " + caseID + " has been well received. <br/><br/>"
                    + "Following is information added: " + additional_info
                    + "<br/><br/> With this piece of information, you may expect to hear from us in the next 14 days."
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
