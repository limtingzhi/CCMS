/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.controller;

import ccms.model.SearchCase;
import ccms.model.SearchCaseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
@WebServlet(name = "SearchCase", urlPatterns = {"/SearchCase.do"})
public class SearchController extends HttpServlet {

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

        String inputCaseID = request.getParameter("caseid");
        String inputNRIC = request.getParameter("personnric");

        if (inputCaseID.equals("") && inputNRIC.equals("")) {
            SearchCaseDAO searchCaseDao = new SearchCaseDAO();
            ArrayList<SearchCase> searchCaseList = null;
            searchCaseList = searchCaseDao.getAllCase();

            if (searchCaseList != null && searchCaseList.size() != 0) {
                request.setAttribute("searchResult", searchCaseList);
            } else {
                request.setAttribute("noResult", "No Records Found. ");
            }
            
        }else if (inputCaseID.equals("") || inputNRIC.equals("")) {
            request.setAttribute("errorMsg", "Fill in the missing fields.");
        } else {

            try {
                int caseID = Integer.parseInt(inputCaseID);
                SearchCaseDAO searchCaseDao = new SearchCaseDAO();
                ArrayList<SearchCase> searchCaseList = null;
                searchCaseList = searchCaseDao.searchCase(caseID, inputNRIC);

                if (searchCaseList != null && searchCaseList.size() != 0) {
                    request.setAttribute("searchResult", searchCaseList);
                } else {
                    request.setAttribute("noResult", "No Records Found. ");
                }

            } catch (NumberFormatException nfe) {
                request.setAttribute("errorMsg", "Fill in Case-ID as numeric numbers only.");
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
