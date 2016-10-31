/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ccms.model;

/**
 *
 * @author asus user
 */
public class Person {
    private String nric;
    private String name;
    private String email;
    private int contact_no;
    
    public Person(String nric, String name, String email, int contact_no) {
        this.nric = nric;
        this.name = name;
        this.email = email;
        this.contact_no = contact_no;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getContact_no() {
        return contact_no;
    }

    public void setContact_no(int contact_no) {
        this.contact_no = contact_no;
    }    
}
