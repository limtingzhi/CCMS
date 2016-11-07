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
@WebServlet(name = "ProcessCaseReassignController", urlPatterns = {"/ProcessCaseReassignController.do"})
public class ProcessCaseReassignController extends HttpServlet {

    private CaseDAO caseDAO;
    private EmployeeDAO employeeDAO;
    
    public ProcessCaseReassignController() throws ParseException {
        DepartmentDAO dDAO = new DepartmentDAO();
        this.employeeDAO = new EmployeeDAO(dDAO);
        PersonDAO pDAO = new PersonDAO();
        this.caseDAO = new CaseDAO(employeeDAO, pDAO);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            int caseID = Integer.parseInt(request.getParameter("caseID"));
            int currentEmployeeIDStr = Integer.parseInt(request.getParameter("currentEmployeeID"));
            Employee currentEmployee = employeeDAO.getEmployeeByID(currentEmployeeIDStr);
            String currentEmployeeEmail = currentEmployee.getEmail();
            String currentEmployeeName = currentEmployee.getName();
            
            int newAssignID = Integer.parseInt(request.getParameter("newAssignID"));
            Employee newEmp = employeeDAO.getEmployeeByID(newAssignID);
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateStr = sdf.format(now);
//            String message = "";

            String status = "Pending - " + newEmp.getPosition() + " " + newEmp.getName();
//
//            int update1 = caseDAO.routeCaseToRespectiveInCharge(caseID, inChargeID, date); //Route case to in charge (e.g executive to manager)
//            int update2 = caseDAO.updateCaseStatus(status, caseID);

            int rowUpdate = caseDAO.routeCaseToRespectiveInCharge(caseID, newAssignID, dateStr);
            int update2 = caseDAO.updateCaseStatus(status, caseID);
            
            boolean sent1 = false;
            boolean sent2 = false;
            if (rowUpdate > 0 && update2 > 0) {
                String message1 = "Dear " + currentEmployeeName + ", <br/><br/>"
                    + "We would like to inform you that case with ID " + caseID + " has been reassigned to your colleague."
                    + "<br/> You may continue to work on other cases on hand."
                    + "<br/><br/> Cheers, "
                    + "<br/> MOM CCMS Team";
                sent1 = sendEmail(currentEmployeeEmail, caseID, message1); //Email to inform current employee who handle the case
                
//                if(sent1) {
                String message2 = "Dear " + newEmp.getName() + ", <br/><br/>"
                    + "We would like to inform you that you have been assigned a new case with ID " + caseID + " has been assigned to you."
                    + "<br/><br/> Cheers, "
                    + "<br/> MOM CCMS Team";
                sent2 = sendEmail(newEmp.getEmail(), caseID, message2); //Email to inform new employee who received the assignment
//                }
//        out.println("Case " + caseID + " has been reassigned to " + newEmp.getName());
            }
            if(sent1 && sent2) {
//                out.println("successful");
                request.setAttribute("reassignMessage", "Case " + caseID + " has been reassigned to " + newEmp.getName());
                RequestDispatcher rd = request.getRequestDispatcher("PotentialReassignCasesList.jsp");
                rd.forward(request, response);
            }
        } finally {
            out.close();
        }
    }
    
    public boolean sendEmail(String email, int caseID, String email_content) {
        boolean send = false;
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
            message.setSubject("Update on Case " + caseID);
            
            // Now set the actual message
//            String email_content = "Dear " + employeeName + ", <br/><br/>"
//                    + "We would like to inform you that case with ID " + caseID + " has been reassigned to your colleague as to lighten your workload and to achieve overall efficiency."
//                    + "<br/> You may continue to work on other cases on hand."
//                    + "<br/><br/> Cheers, "
//                    + "<br/><br/> MOM CCMS Team";

            message.setContent(email_content, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);
         } catch (MessagingException mex) {
         mex.printStackTrace();
        }
        send = true;
        return send;
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
