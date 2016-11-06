package ccms.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ccms.model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Wayne
 */
@WebServlet(name = "processCase", urlPatterns = {"/processCase"})
public class processCase extends HttpServlet {

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
        PersonDAO pdao = new PersonDAO();
        HttpSession sess = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            CaseDAO casedao = new CaseDAO();
            //type of the case
            String[] type = request.getParameterValues("type");
            String complaintDescription = request.getParameter("complaintDescription");
            String complimentDescription = request.getParameter("complimentDescription");
            String difficulty = request.getParameter("difficulty");
            String issues = request.getParameter("issues");
            String empName = request.getParameter("employee_name");
            String deptName = request.getParameter("employee_dept");

            if (type == null) {
                sess.setAttribute("Error", "You are required to check at least 1 type");
                response.sendRedirect("CreateCase.jsp");
            } else if (type[0].equals("compliment") && complimentDescription.isEmpty()) {
                sess.setAttribute("Error", "Compliment Description cannot be empty");
                response.sendRedirect("CreateCase.jsp");
            } else if (type[0].equals("complaint") && complaintDescription.isEmpty()) {
                sess.setAttribute("Error", "Complain Description cannot be empty");
                response.sendRedirect("CreateCase.jsp");
            } else {
                //person's details
                String person_nric = request.getParameter("person_nric");
                String person_name = request.getParameter("person_name");
                String person_email = request.getParameter("person_email");
                String person_contact = request.getParameter("person_contact_no");
                Date reported_date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
                int recorded_employee_id = Integer.parseInt(request.getParameter("recorded_employee_id"));//take in from session
                pdao.createPerson(new Person(person_nric, person_name, person_email, Integer.parseInt(person_contact)));
                out.println("type is" + type[0]);
                if (type.length > 1) {
                    //create case for complaint
                    String caseType = "complaint";
                    casedao.createCase(new complaintCase(complaintDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    casedao.createComplaintCase(new complaintCase(difficulty, issues));

                    //create case for compliment
                    caseType = "compliment";
                    casedao.createCase(new complaintCase(complimentDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    casedao.createComplimentCase(new complimentCase(empName, deptName));
                    casedao.createEmployeeComplimentCase(new complimentCase(empName, deptName));

                } else if (type[0].equals("complaint")) {
                    String caseType = "complaint";
                    casedao.createCase(new complaintCase(complaintDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    casedao.createComplaintCase(new complaintCase(difficulty, issues));

                } else {
                    String caseType = "compliment";
                    casedao.createCase(new complaintCase(complimentDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    casedao.createComplimentCase(new complimentCase(empName, deptName));
                    casedao.createEmployeeComplimentCase(new complimentCase(empName, deptName));
                }
            }

            /*request.setAttribute("email", person_email);
             RequestDispatcher dispatcher = request.getRequestDispatcher("/SendEmail");
             dispatcher.forward(request, response);*/
        } catch (Exception e) {

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
