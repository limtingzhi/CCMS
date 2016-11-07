/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.controller;

import ccms.model.EmployeeDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author Feng Ru Chua
 */
@WebServlet(name = "SendEmail", urlPatterns = {"/SendEmail.do"})
public class SendEmailController extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet SendEmailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SendEmailServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
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

        String emailaddress = (String) request.getAttribute("email");
        String emp_email = (String) request.getAttribute("emp_email");
        if (emp_email == null) {
            emp_email = "";
        }
        String dir_email = (String) request.getAttribute("dir_email");
        if (dir_email == null) {
            dir_email = "";
        }
        String caseDifficulty = (String) request.getAttribute("difficulty");
        if (caseDifficulty == null) {
            caseDifficulty = "";
        }
        String gotCompliment = (String) request.getAttribute("gotCompliment");
        if (gotCompliment == null) {
            gotCompliment = "";
        }
        String caseid = request.getParameter("caseid");

        final String username = "ccms.mom.noreply@gmail.com";
        final String password = "helloworld01";
        // Recipient's email ID needs to be mentioned.
        String to = emailaddress;

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

        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            // Set Subject: header field
            message.setSubject("Thank you for your complaint");
            // Now set the actual message
            if (caseDifficulty.equals("Easy")) {
                message.setText("We have received your complaint. \n"
                        + "We will get back to you in 3 working days. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message);
            } else if (caseDifficulty.equals("Hard")) {
                message.setText("We have received your complaint. \n"
                        + "We will get back to you in 5 working days. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message);
            } else if (caseDifficulty.equals("Super Complex")) {
                message.setText("We have received your complaint. \n"
                        + "We will get back to you in 7 working days. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message);
            }

            if (gotCompliment.equals("compliment")) {
                message.setSubject("Thank you for your compliment");
                message.setText("We have received your compliment. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message);
            }
            if (!emp_email.equals("")) {
                to = emp_email;
                MimeMessage message2 = new MimeMessage(session);
                // Set From: header field of the header.
                message2.setFrom(new InternetAddress(from));
                message2.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
                // Set Subject: header field
                message2.setSubject("You Have been complimented!");
                message2.setText("you have just receive a compliment. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message2);
            }
            if (!dir_email.equals("")) {
                to = dir_email;
                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                // Set Subject: header field
                message.setSubject("Your department has just been complimented!");
                message.setText("Your department has just receive a compliment. \n"
                        + "Thank you. \n"
                        + "\n"
                        + "MOM CCMS Auto-generated Email. Do not reply.");
                Transport.send(message);
            }

            // Send message
            /*String title = "Feedback has been successfully logged.";
             String res = "Acknowledgement email has been sent to user.";
             String docType
             = "<!doctype html public \"-//w3c//dtd html 4.0 "
             + "transitional//en\">\n";
             out.println(docType
             + "<html>\n"
             + "<head><title>" + title + "</title></head>\n"
             + "<body bgcolor=\"#f0f0f0\">\n"
             + "<h1 align=\"center\">" + title + "</h1>\n"
             + "<p align=\"center\">" + res + "</p>\n"
             + "</body></html>");*/
            response.sendRedirect("Home.jsp");
            //RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
           // dispatcher.forward(request, response);
            return;
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
        //processRequest(request, response);
        /*RequestDispatcher dispatcher = request.getRequestDispatcher("Home.jsp");
         dispatcher.forward(request, response);
         return;*/
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
