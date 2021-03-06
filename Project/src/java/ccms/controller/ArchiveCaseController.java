/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccms.controller;

import ccms.model.SearchCaseDAO;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ArchiveCase", urlPatterns = {"/ProcessArchive.do"})
public class ArchiveCaseController extends HttpServlet {

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
        
        String archive_case = (String) request.getParameter("closingremark");
        String case_id = (String) request.getParameter("caseID");
        
        int case_id_int = 0;
        
        try{
            case_id_int = Integer.parseInt(case_id);
        }catch(NumberFormatException nfe){
            
        }
        
        if (archive_case == null || archive_case.equals("")) {
            request.setAttribute("errorMsg", "Please fill in closing remarks to archive case.");
        }else{
            SearchCaseDAO scDao = new SearchCaseDAO();
            boolean updateProc = scDao.archiveCase(archive_case, case_id_int);
            if(updateProc != true){
                request.setAttribute("errorMsg", "Archiving failed. Please try again.");
            }
        }
        
        dispatcher.forward(request, response);
        return;
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
