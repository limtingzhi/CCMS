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
import java.sql.Timestamp;
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
        PrintWriter out = response.getWriter();
        int caseID = 0;

        try {
            CaseDAO casedao = new CaseDAO();
            EmployeeDAO empdao = new EmployeeDAO();
            //type of the case
            String[] type = request.getParameterValues("type");
            String complaintDescription = request.getParameter("complaintDescription");
            String complimentDescription = request.getParameter("complimentDescription");
            String difficulty = request.getParameter("difficulty");
            String issues = request.getParameter("issues");
            String empOrDept = request.getParameter("employee_or_dept");
            String empName = request.getParameter("employee_name");
            String deptName = request.getParameter("employee_dept");

            //person's details
            String person_nric = request.getParameter("person_nric");
            String person_name = request.getParameter("person_name");
            String person_email = request.getParameter("person_email");
            String person_contact = request.getParameter("person_contact_no");
            Timestamp reported_date = new Timestamp(System.currentTimeMillis());
            int recorded_employee_id = Integer.parseInt(request.getParameter("recorded_employee_id"));//take in from session

            RequestDispatcher dispatcher = request.getRequestDispatcher("CreateCase.jsp");
            String errorMsg = "";

            // Validations - Person
            if (person_nric == null || person_nric.isEmpty()) {
                errorMsg += "Please fill up the Person's NRIC.<br>";
            } else if (person_nric.length() > 9) {
                errorMsg += "Max length of NRIC should be 9.<br>";
            } else if (person_nric.length() < 9) {
                errorMsg += "Length of NRIC should be 9.<br>";
            }

            if (person_name == null || person_name.isEmpty()) {
                errorMsg += "Please fill up the Person's Name.<br>";
            } else if (person_name.length() > 100) {
                errorMsg += "Max length of Name should be 100.<br>";
            }

            if (person_contact == null || person_contact.isEmpty()) {
                errorMsg += "Please fill up the Person's Contact No.<br>";
            } else if (person_contact.length() > 8) {
                errorMsg += "Max length of Contact No should be 8.<br>";
            } else if (person_contact.length() < 8) {
                errorMsg += "Length of Contact No should be 8.<br>";
            }

            if (person_email == null || person_email.isEmpty()) {
                errorMsg += "Please fill up the Person's Email.<br>";
            } else if (person_email.length() > 100) {
                errorMsg += "Max length of Email should be 100.<br>";
            }

            // Validations - Case
            if (type == null) {
                errorMsg += "Please select at least 1 case type.<br>";
            } else {
                if (type[0].equals("Complaint") || (type.length == 2 && type[1].equals("Complaint"))) {
                    if (complaintDescription == null || complaintDescription.isEmpty()) {
                        errorMsg += "Please fill up the Complaint Description.<br>";
                    } else if (complaintDescription.length() > 300) {
                        errorMsg += "Max length of Complaint Description should be 300.<br>";
                    }
                }

                if (type[0].equals("Compliment") || (type.length == 2 && type[1].equals("Compliment"))) {
                    if (complimentDescription == null || complimentDescription.isEmpty()) {
                        errorMsg += "Please fill up the Compliment Description.<br>";
                    } else if (complimentDescription.length() > 300) {
                        errorMsg += "Max length of Compliment Description should be 300.<br>";
                    }
                }
            }

            request.setAttribute("person_nric", person_nric);
            request.setAttribute("person_name", person_name);
            request.setAttribute("person_contact_no", person_contact);
            request.setAttribute("person_email", person_email);
            request.setAttribute("type", type);
            request.setAttribute("complaintDescription", complaintDescription);
            request.setAttribute("complimentDescription", complimentDescription);
            request.setAttribute("difficulty", difficulty);
            request.setAttribute("issues", issues);
            request.setAttribute("employee_or_dept", empOrDept);
            request.setAttribute("employee_name", empName);
            request.setAttribute("employee_dept", deptName);

            request.setAttribute("message", errorMsg);

            if (errorMsg.equals("")) {
                // clear fields for UI form
                request.setAttribute("person_nric", null);
                request.setAttribute("person_name", null);
                request.setAttribute("person_contact_no", null);
                request.setAttribute("person_email", null);
                request.setAttribute("type", null);
                request.setAttribute("complaintDescription", null);
                request.setAttribute("complimentDescription", null);
                request.setAttribute("difficulty", null);
                request.setAttribute("issues", null);
                request.setAttribute("employee_or_dept", null);
                request.setAttribute("employee_name", null);
                request.setAttribute("employee_dept", null);

                // add person
                Person p = pdao.getPersonByNRIC(person_nric);
                if (p == null) {
                    pdao.createPerson(new Person(person_nric, person_name, person_email, Integer.parseInt(person_contact)));
                }

                // complaint case
                if (type[0].equals("Complaint") || (type.length == 2 && type[1].equals("Complaint"))) {
                    String caseType = "Complaint";
                    casedao.createCase(new complaintCase(complaintDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    casedao.createComplaintCase(new complaintCase(difficulty, issues));
                    request.setAttribute("email", person_email);
                    caseID = casedao.getLatestCaseID();
                    String caseIDToPassToACA = caseID + "";
                    request.setAttribute("difficultyToACA", difficulty);
                    request.setAttribute("caseIDToACA", caseIDToPassToACA);
                }

                // compliment case
                if (type[0].equals("Compliment") || (type.length == 2 && type[1].equals("Compliment"))) {
                    String caseType = "Compliment";
                    casedao.createCase(new complaintCase(complimentDescription, reported_date, caseType, recorded_employee_id, person_nric));
                    request.setAttribute("email", person_email);

                    if (empOrDept.equals("Employee")) {
                        casedao.createComplimentCase(new complimentCase(empName, null));
                        casedao.createEmployeeComplimentCase(new complimentCase(empName, null));
                        String emp_email = empdao.getEmployeeByName(empName);
                        request.setAttribute("emp_email", emp_email);
                    } else {
                        casedao.createComplimentCase(new complimentCase(null, deptName));
                        casedao.createEmployeeComplimentCase(new complimentCase(null, deptName));
                        String dir_email = empdao.getEmployeeByPosition();
                        request.setAttribute("dir_email", dir_email);
                    }
                }

                // send emails
                if (type.length == 2) {

                }

                if (type[0].equals("Complaint") || (type.length == 2 && type[1].equals("Complaint"))) {
                    if (type.length == 2) {
                        request.setAttribute("gotCompliment", "compliment");
                    }
                    dispatcher = request.getRequestDispatcher("/AutoCaseAllocation");
                    dispatcher.forward(request, response);
                }

                if (type[0].equals("Compliment") || (type.length == 2 && type[1].equals("Compliment"))) {
                    request.setAttribute("gotCompliment", "compliment");
                    dispatcher = request.getRequestDispatcher("/SendEmail.do");
                    dispatcher.forward(request, response);
                }

                HttpSession session = request.getSession();
                session.setAttribute("successMsg", "Case Created!");
                response.sendRedirect("CreateCase.jsp");
                return;
            }

            dispatcher.forward(request, response);
            return;
        } catch (Exception e) {
            System.out.println(e);
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
