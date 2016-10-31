/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ccms.model;

public class SearchCase {
    private int id;
    private String reportedDate;
    private String personName;
    private String personNric;
    private int contactNo;
    private String email;
    private String type;
    private String status;
    private String description;
    private String difficulty;
    private String issue;
    private String additional_info;
    private String closing_remark;
    
    public SearchCase(int id, String reportedDate, String personName, String personNric, int contactNo, String email, String type, String status, String description, String difficulty, String issue, String additional_info, String closing_remark){
        this.id = id;
        this.reportedDate = reportedDate;
        this.personName = personName;
        this.personNric = personNric;
        this.contactNo = contactNo;
        this.email = email;
        this.type = type;
        this.status = status;
        this.description = description;
        this.difficulty = difficulty;
        this.issue = issue;
        this.additional_info = additional_info;
        this.closing_remark = closing_remark;
    }
    
    public int getID(){
        return id;
    }
    
    public String getReportedDate(){
        return reportedDate;
    }
    
    public String getPersonName(){
        return personName;
    }
    
    public String getType(){
        return type;
    }
    
    public String getPersonNric(){
        return personNric;
    }
    
    public String getStatus(){
        return status;
    }
    
    public int getContactNo(){
        return contactNo;
    }
    public String getEmail(){
        return email;
    }
    
    public String getDescription(){
        return description;
    }
    
    public String getDifficulty(){
        return difficulty;
    }
    
    public String getIssue(){
        return issue;
    }
    
    public String getAdditionalInfo(){
        return additional_info;
    }
    
    public String getClosingRemark(){
        return closing_remark;
    }
}
