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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;
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
@WebServlet(name = "DoReassignCaseController", urlPatterns = {"/DoReassignCaseController.do"})
public class DoReassignCaseController extends HttpServlet {

    private final EmployeeDAO employeeDAO;
    private final CaseDAO caseDAO;

    public DoReassignCaseController() throws ParseException {
        this.employeeDAO = new EmployeeDAO(new DepartmentDAO());
        this.caseDAO = new CaseDAO();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        try {
            /* TODO output your page here. You may use following sample code. */
            int caseID = Integer.parseInt(request.getParameter("caseID"));
            int employeeID = Integer.parseInt(request.getParameter("employeeID"));
            String expectedResponseDateStr = request.getParameter("expectedDateResponse");
            java.util.Date expectedResponseDate = df.parse(expectedResponseDateStr);

            ArrayList<String> caseDetails = caseDAO.getCaseDetails(caseID);
            int eId = Integer.parseInt(caseDetails.get(10));
            String currentEmployeeName = employeeDAO.getEmployeeByID(eId).getName();
            caseDetails.add(11, currentEmployeeName);
            LinkedHashMap<Integer, ArrayList<String>> employeeWorkload = caseDAO.getCaseCountByDifficulty();

            LinkedHashMap<Integer, ArrayList<String>> processedEmployeeWorkload = new LinkedHashMap<Integer, ArrayList<String>>();
            Set<Integer> employeeIDs = employeeWorkload.keySet();

            //Iterate through the LinkedHashMap using keys
            for (int id : employeeIDs) {
                Employee e = employeeDAO.getEmployeeByID(id);
                String employeeName = e.getName();
                String employeePosition = e.getPosition();
                java.util.Date onLeaveStartD = e.getOnLeaveStart();
                String onLeaveStartStr = "";
                if (onLeaveStartD != null) {
                    onLeaveStartStr = df.format(onLeaveStartD);
                }

                java.util.Date onLeaveEndD = e.getOnLeaveEnd();
                String onLeaveEndStr = "";
                if (onLeaveEndD != null) {
                    onLeaveEndStr = df.format(onLeaveEndD);
                }

                ArrayList<String> workload = employeeWorkload.get(id);

                //New ArrayList to store case count and remarks
                ArrayList<String> arrToStoreCaseCount = new ArrayList<String>();
                arrToStoreCaseCount.add(employeeName); //store employee name in index 0
                arrToStoreCaseCount.add(employeePosition); //store employee position in index 1
                arrToStoreCaseCount.add("0"); //default count for easy
                arrToStoreCaseCount.add("0"); //hard
                arrToStoreCaseCount.add("0"); //super complex

                if (workload.size() > 0) {
                    for (String casecount : workload) {
                        int lastIndexOf_ = casecount.lastIndexOf("_");
                        String casedifficulty = casecount.substring(0, lastIndexOf_);
                        String numCaseByDifficulty = casecount.substring(lastIndexOf_ + 1);
                        if (casedifficulty.equalsIgnoreCase("easy")) {
                            arrToStoreCaseCount.set(2, numCaseByDifficulty);
                        } else if (casedifficulty.equalsIgnoreCase("hard")) {
                            arrToStoreCaseCount.set(3, numCaseByDifficulty);
                        } else if (casedifficulty.equalsIgnoreCase("super complex")) {
                            arrToStoreCaseCount.set(4, numCaseByDifficulty);
                        }
                    }
                }

                String remarks = "";
                if (onLeaveStartD != null && onLeaveEndD != null) {
                    boolean checkOverlap = expectedResponseDate.after(df.parse(onLeaveStartStr)) && expectedResponseDate.before(df.parse(onLeaveEndStr));
                    if (checkOverlap) {
                        remarks = "Employee on leave from " + sdf2.format(onLeaveStartD) + " to " + sdf2.format(onLeaveEndD);
                    } else {
                        remarks = "-";
                    }
                } else {
                    remarks = "-";
                }
                arrToStoreCaseCount.add(remarks); //add remarks to arraylist

                //Store to LinkedHashMap to be passed over to JSP
                processedEmployeeWorkload.put(id, arrToStoreCaseCount);
            }
            request.setAttribute("caseDetails", caseDetails);
            request.setAttribute("processedEmployeeWorkload", processedEmployeeWorkload);
            RequestDispatcher rd = request.getRequestDispatcher("ViewCase.jsp");
            rd.forward(request, response);
        } finally {
            out.close();
        }
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
            Logger.getLogger(DoReassignCaseController.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DoReassignCaseController.class.getName()).log(Level.SEVERE, null, ex);
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
