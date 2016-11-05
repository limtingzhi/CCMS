package ccms.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ccms.model.CaseDAO;
import ccms.model.Employee;
import ccms.model.Person;
import ccms.model.complaintCase;
import ccms.model.PersonDAO;
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
        PrintWriter out = response.getWriter();
        try {
            CaseDAO casedao = new CaseDAO();
            String person_nric = request.getParameter("person_nric");
            String person_name = request.getParameter("person_name");
            String person_email = request.getParameter("person_email");
            String person_contact = request.getParameter("person_contact_no");
            String description = request.getParameter("description");
            Date reported_date = new java.sql.Date(Calendar.getInstance().getTimeInMillis());
            String type = request.getParameter("type");
            int recorded_employee_id = Integer.parseInt(request.getParameter("recorded_employee_id"));//take in from session
            pdao.createPerson(new Person(person_nric, person_name, person_email, Integer.parseInt(person_contact)));
            if (type.equals("complaint")) {
                String difficulty = request.getParameter("difficulty");
                String issues = request.getParameter("issues");
                casedao.createCase(new complaintCase(description, reported_date, type, recorded_employee_id, person_nric, difficulty, issues));
                casedao.createComplaintCase(new complaintCase(difficulty, issues));
            } else {
                String employee = request.getParameter("employee_name");
                int dept = Integer.parseInt(request.getParameter("employee_dept"));
                casedao.createComplimentCase(dept);
            }
            //for multiple case, make sure u have a concat the 3 case difficulty

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
