package ccms.controller;

import ccms.model.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author limtingzhi
 */
@WebServlet(name = "LoginController", urlPatterns = {"/LoginController.do"})
public class LoginController extends HttpServlet {

    DepartmentDAO departmentDAO = new DepartmentDAO();
    EmployeeDAO employeeDAO = new EmployeeDAO(departmentDAO);

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Index.jsp");
        HttpSession session = request.getSession();
        session.removeAttribute("loginuser");

        String inputUserName = request.getParameter("username");
        String inputPassWord = request.getParameter("password");

        // Check if username or password is empty string
        if (inputUserName.equalsIgnoreCase("") || inputPassWord.equals("")) {
            request.setAttribute("errorMsg", "Please enter both username and password.");
        } else {
            ArrayList<Employee> list = employeeDAO.getAllEmployee();

            for (Employee e : list) {
                // Get current employee's username
                String name = e.getUsername();
                // Check if the current employee's username equal to input username
                if (name.equalsIgnoreCase(inputUserName)) {
                    // If equal, retrieve its password
                    String password = e.getPassword();

                    if (inputPassWord.equals(password)) {
                        session.setAttribute("loginuser", e);
                        response.sendRedirect("Home.jsp");
                        return;
                    } else {
                        request.setAttribute("errorMsg", "Invalid username/password.");
                    }
                } else {
                    request.setAttribute("errorMsg", "Invalid username/password.");
                }
            }
        }
        dispatcher.forward(request, response);
        return;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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
