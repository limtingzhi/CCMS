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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@WebServlet(name = "AutoCaseAllocation", urlPatterns = {"/AutoCaseAllocation"})
public class AutoCaseAllocation extends HttpServlet {

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

        //String caseToBeAllocated = request.getParameter("listOfCaseID");
        String caseToBeAllocated = (String) request.getAttribute("caseIDToACA");
        String caseDifficulty = (String) request.getAttribute("difficultyToACA");

        List<String> caseDifficultyList = Arrays.asList(caseDifficulty.split(","));
        List<String> caseToBeAllocatedList = Arrays.asList(caseToBeAllocated.split(","));

        DepartmentDAO deptDao = new DepartmentDAO();
        EmployeeDAO empDao = new EmployeeDAO(deptDao);
        CaseDAO caseDao = null;
        try {
            caseDao = new CaseDAO();
        } catch (ParseException pe1) {
            System.out.println("Case DAO error.");
        }

        ArrayList<Employee> empList = empDao.getAllEmployee();

        for (int j = 0; j < caseToBeAllocatedList.size(); j++) {

            String difficulty = caseDifficultyList.get(j).trim();
            String caseID = caseToBeAllocatedList.get(j).trim();
            int caseIDInt = 0;
            try {
                caseIDInt = Integer.parseInt(caseID);
            } catch (NumberFormatException nfe) {
                System.out.println("Number format error");
            }

            String relevantRanking = "";
            String relevantRanking2 = "";

            //Get relevant employees based on difficulty of cases.
            if (difficulty.equalsIgnoreCase("easy")) {
                relevantRanking = "Junior Executive";
            } else if (difficulty.equalsIgnoreCase("hard")) {
                relevantRanking = "Senior Executive";
                relevantRanking2 = "Junior Executive";
            } else {
                relevantRanking = "Senior Executive";
            }

            //Get relevant employees based on difficulty of cases & leave days.
            ArrayList<Employee> relevantEmpList = new ArrayList<Employee>();
            for (int i = 0; i < empList.size(); i++) {
                Employee e1 = empList.get(i);

                if (e1.getPosition().equalsIgnoreCase(relevantRanking) || e1.getPosition().equalsIgnoreCase(relevantRanking2)) {

                    try {
                        Timestamp reportedDate = caseDao.getReportedDateOfCase(caseIDInt);

                        if (e1.getOnLeaveStart() != null && e1.getOnLeaveEnd() != null) {

                            if (reportedDate.after(e1.getOnLeaveStart()) && reportedDate.before(e1.getOnLeaveEnd())) {
                                //System.out.println(e1.getName());
                                relevantEmpList.add(e1);
                            }
                        }else{
                            relevantEmpList.add(e1);
                            //System.out.println(e1.getName());
                        }     

                    } catch (ParseException pe) {

                    }

                }

            }
            
            int lowestWorkLoadCount = 999999999;
            Employee bestEmp = null;
            for (int i=0; i < relevantEmpList.size(); i++) {
                int empID = relevantEmpList.get(i).getEmployeeID();
                try {
                    int workLoadEmp = caseDao.getWorkloadByEmpID(empID);
                    System.out.println("Case ID: "+j+ ", Emp Name: "+relevantEmpList.get(i).getName()+", WORKLOAD: "+workLoadEmp);
                    
                    
                    if (workLoadEmp < lowestWorkLoadCount) {
                        lowestWorkLoadCount=workLoadEmp;
                        bestEmp=relevantEmpList.get(i);     
                    }
                }catch (ParseException pe) {
                       
               }
                
            }
            
            String status = "Pending - "+bestEmp.getPosition()+ " " +bestEmp.getName();
            int rowUpdated = caseDao.updateComplaintCase(status, caseIDInt);
            int rowInserted = caseDao.insertComplaintCaseHandling(caseIDInt, bestEmp.getEmployeeID());

        }
        String person_email = (String)request.getAttribute("email");
        String gotCompliment = (String)request.getAttribute("gotCompliment");
        request.setAttribute("difficulty", caseDifficulty);
        request.setAttribute("email", person_email);
        request.setAttribute("gotCompliment", gotCompliment);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/SendEmail.do");
        dispatcher.forward(request, response);
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
