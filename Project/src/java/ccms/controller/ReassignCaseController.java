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
//import static ccms.model.TestingMain.getWorkingDaysBetweenTwoDates;
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
//            LinkedHashMap<Integer, ArrayList<String>> cList = checkCaseProgress();
            LinkedHashMap<Integer, ArrayList<String>> caseList = caseDAO.getOutstandingCases();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            Set<Integer> caseIDs = caseList.keySet();
            for (int i : caseIDs) {
                ArrayList<String> arr = caseList.get(i);
                String dateReceivedStr = arr.get(0);
                Date dateReceived = sdf1.parse(dateReceivedStr);
            out.println("date received: " + sdf2.format(dateReceived));
                String expectedResponseDateStr = arr.get(1);
//            
                Date today = new Date();
                Date expectedResponseDate = sdf1.parse(expectedResponseDateStr);
//            
                int dayDiff = getWorkingDaysBetweenTwoDates(today, dateReceived);
            out.println("expected response: " + sdf2.format(expectedResponseDate));
                String lastsavedStr = arr.get(arr.size() - 1);
//            int dayDiff = 0;
                Date lastsaved = null;
                if (!lastsavedStr.equals("-")) {
                    lastsaved = sdf1.parse(lastsavedStr);
                    dayDiff = getWorkingDaysBetweenTwoDates(today, lastsaved);
                    int employeeID = Integer.parseInt(arr.get(6));
                    String employeeName = eDAO.getEmployeeByID(employeeID).getName();
                    arr.set(arr.size()-1, (sdf2.format(lastsaved)) + " by " + employeeName);
//                out.println("last saved: " + sdf2.format(lastsaved));                    
                } 
//            //Check if case already overdue
                boolean isOverdue = today.after(expectedResponseDate);
                String remarks = "";

            out.println("is overdue? " + isOverdue);
            out.println("Day Diff: " + dayDiff);
//            long diff = today.getTime() - lastWorkingDate.getTime();
//            int dayDiff = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
//            int dayDiff = getWorkingDaysBetweenTwoDates(today, lastWorkingDate);
                if (isOverdue) {
                    remarks = "Case overdue for " + dayDiff + " days";
                } else if (!isOverdue && dayDiff > 0) {
                    remarks = "Case idle for " + dayDiff + " days";
                }
                
            String onLeaveStart = arr.get(4);
            String onLeaveEnd = arr.get(5);
            
            if(onLeaveStart != null && onLeaveEnd != null) {
                Date onLeaveStartD = sdf1.parse(onLeaveStart);
                Date onLeaveEndD = sdf1.parse(onLeaveEnd);
                
//                System.out.println("start leave: " + sdf2.format(onLeaveStartD));
//                System.out.println("end leave: " + sdf2.format(onLeaveEndD));
                boolean checkOverlap = expectedResponseDate.after(onLeaveStartD) && expectedResponseDate.before(onLeaveEndD);
                if(checkOverlap) {
                    remarks = remarks + "<br/> Employee on leave from " + sdf2.format(onLeaveStartD) + " to " + sdf2.format(onLeaveEndD);
                }
//                System.out.println("Overlap: " + checkOverlap);
            } 
                

                arr.set(0, sdf2.format(dateReceived));
                arr.set(1, sdf2.format(expectedResponseDate));
////            arr.set(arr.size()-1, sdf2.format(lastsaved));
////            remarks = remarks + dayDiff;
                arr.add(remarks);
            }
            RequestDispatcher rd = request.getRequestDispatcher("PotentialReassignCasesList.jsp");
            request.setAttribute("potentialCases", caseList);
            rd.forward(request, response);
        } finally {
            out.close();
        }
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
