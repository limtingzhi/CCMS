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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
@WebServlet(name = "ReassignCaseController", urlPatterns = {"/ReassignCase"})
public class ReassignCaseController extends HttpServlet {

    private CaseDAO caseDAO;
    private EmployeeDAO eDAO;
    private Employee employee;

    public ReassignCaseController() throws ParseException {
        DepartmentDAO dDAO = new DepartmentDAO();
        this.eDAO = new EmployeeDAO(dDAO);
        PersonDAO pDAO = new PersonDAO();
        this.caseDAO = new CaseDAO(eDAO, pDAO);
    }

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
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            LinkedHashMap<Integer, ArrayList<String>> caseList = processOutstandingCases();
            RequestDispatcher rd = request.getRequestDispatcher("PotentialReassignCasesList.jsp");
            request.setAttribute("potentialCases", caseList);
            rd.forward(request, response);
        } finally {
            out.close();
        }
    }

    public LinkedHashMap<Integer, ArrayList<String>> processOutstandingCases() throws ParseException {
        LinkedHashMap<Integer, ArrayList<String>> caseList = caseDAO.getOutstandingCases();

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Set<Integer> caseIDs = caseList.keySet();
        for (int i : caseIDs) {
            ArrayList<String> arr = caseList.get(i);
            String dateReceivedStr = arr.get(0);
            Date dateReceived = sdf1.parse(dateReceivedStr);
            String tempDate = sdf2.format(dateReceived);
            Date dateReceivedD = sdf2.parse(tempDate);

            String expectedResponseDateStr = arr.get(1);

            Date today = new Date();
            Date expectedResponseDate = sdf1.parse(expectedResponseDateStr);
            String todayStr = sdf2.format(today);
            today = sdf2.parse(todayStr);

            int dayDiff = getWorkingDaysBetweenTwoDates(today, dateReceived);

            String lastsavedStr = arr.get(arr.size() - 1);

            Date lastsaved = null;
            if (!lastsavedStr.equals("-")) {
                lastsaved = df1.parse(lastsavedStr);
                String lastsavedtemp = sdf1.format(lastsaved);
                lastsaved = sdf1.parse(lastsavedtemp);
                dayDiff = getWorkingDaysBetweenTwoDates(today, lastsaved);
                int employeeID = Integer.parseInt(arr.get(6));
                String employeeName = eDAO.getEmployeeByID(employeeID).getName();
                arr.set(arr.size() - 1, sdf2.format(lastsaved) + " by " + employeeName);
            }
            //Check if case already overdue
            boolean isOverdue = today.after(expectedResponseDate);
            String remarks = "";

            if (isOverdue) {
                remarks = "Case overdue for " + dayDiff + " days";
            } else if (!isOverdue && dayDiff > 0) {
                remarks = "Case idle for " + dayDiff + " days";
            } else if (dayDiff == 0) {
                remarks = "-";
            }

            String onLeaveStart = arr.get(4);
            String onLeaveEnd = arr.get(5);

            if (onLeaveStart != null && onLeaveEnd != null) {
                Date onLeaveStartD = df1.parse(onLeaveStart);
                String onLeaveStartStr = sdf1.format(onLeaveStartD);
                onLeaveStartD = sdf1.parse(onLeaveStartStr);

                Date onLeaveEndD = df1.parse(onLeaveEnd);
                String onLeaveEndStr = sdf1.format(onLeaveEndD);
                onLeaveEndD = sdf1.parse(onLeaveEndStr);

                boolean checkOverlap = expectedResponseDate.after(onLeaveStartD) && expectedResponseDate.before(onLeaveEndD);
                if (checkOverlap) {
                    remarks = remarks + "<br/> Employee on leave from " + sdf2.format(onLeaveStartD) + " to " + sdf2.format(onLeaveEndD);
                }
            }
            arr.add(remarks);
        }
        return caseList;
    }

    public int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int workDays = 0;

        //Return 0 if start and end are the same
        if (startCal.getTimeInMillis() == endCal.getTimeInMillis()) {
            return 0;
        }

        if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
            startCal.setTime(endDate);
            endCal.setTime(startDate);
        }

        do {
            //excluding start date
            startCal.add(Calendar.DAY_OF_MONTH, 1);
            if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                ++workDays;
            }
        } while (startCal.getTimeInMillis() < endCal.getTimeInMillis()); //excluding end date

        return workDays;
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ReassignCaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ReassignCaseController.class.getName()).log(Level.SEVERE, null, ex);
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
