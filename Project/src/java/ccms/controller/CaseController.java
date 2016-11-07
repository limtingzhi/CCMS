/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.controller;

import ccms.model.PersonDAO;
import ccms.model.CaseDAO;
import ccms.model.DepartmentDAO;
import ccms.model.EmployeeDAO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author asus user
 */
@WebServlet(name = "CaseController", urlPatterns = {"/CaseController.do"})
public class CaseController extends HttpServlet {

    private CaseDAO caseDAO;

    public CaseController() throws ParseException {
        DepartmentDAO dDAO = new DepartmentDAO();
        EmployeeDAO eDAO = new EmployeeDAO(dDAO);
        PersonDAO pDAO = new PersonDAO();
        this.caseDAO = new CaseDAO(eDAO, pDAO);
    }

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
            throws ServletException, IOException, ParseException {
//        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        try {
            int caseID = Integer.parseInt(request.getParameter("caseID"));

//            ArrayList<String> caseDetails = caseDAO.getCaseDetails(caseID);
            ArrayList<String> caseDetails = processCaseDetails(caseID);

            request.setAttribute("caseDetails", caseDetails);

            LinkedHashMap<Integer, ArrayList<String>> responses = caseDAO.getCaseResponses(caseID);
            request.setAttribute("responses", responses);

            RequestDispatcher rd = request.getRequestDispatcher("RespondCase.jsp");
            rd.forward(request, response);
            return;
        } finally {
            out.close();
        }
    }

    public ArrayList<String> processCaseDetails(int caseID) throws ParseException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        ArrayList<String> caseDetails = caseDAO.getCaseDetails(caseID);
        String additional_info = caseDetails.get(6);
        if (additional_info.equals("N/A")) {
            caseDetails.set(6, "-");
        }
        int lastIndex = caseDetails.size() - 1;
        String addOnDate = caseDetails.get(lastIndex);
        if (addOnDate.equals("N/A")) {
            caseDetails.set(lastIndex, "-");
        } else {
            int index1 = addOnDate.indexOf("on");
            int index2 = addOnDate.indexOf(" ", index1);
            String dateStr = addOnDate.substring(index2+1,addOnDate.length()-1);                     
            caseDetails.set(lastIndex, dateStr);
        }
        return caseDetails;
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
