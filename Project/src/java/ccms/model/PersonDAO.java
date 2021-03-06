package ccms.model;

import java.sql.*;
import java.util.*;

/**
 *
 * @author limtingzhi
 */
public class PersonDAO {

    private static final String GET_ALL_PERSON = "SELECT * FROM person";
    private static final String GET_PERSON_BY_NRIC = "SELECT * FROM person WHERE nric = ?";
    private static final String Create_Person = "insert into person(nric,name,email,contact_no) values(?,?,?,?)";

    public ArrayList<Person> getAllPerson() {
        ArrayList<Person> personList = new ArrayList<Person>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_ALL_PERSON);
            rs = ps.executeQuery();

            while (rs.next()) {
                Person person = new Person(
                        rs.getString("nric"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("contact_no")
                );
                personList.add(person);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return personList;
    }

    public Person getPersonByNRIC(String nric) {
        Person person = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = ConnectionManager.getConnection();
            ps = con.prepareStatement(GET_PERSON_BY_NRIC);
            ps.setString(1, nric);
            rs = ps.executeQuery();

            while (rs.next()) {
                person = new Person(
                        rs.getString("nric"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getInt("contact_no")
                );
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return person;
    }

    public void createPerson(Person p) {
        try {
            Connection con = ConnectionManager.getConnection();
            PreparedStatement ps = con.prepareStatement(Create_Person);
            ps.setString(1, p.getNric());
            ps.setString(2, p.getName());
            ps.setString(3, p.getEmail());
            ps.setInt(4, p.getContact_no());

            ps.execute();
            con.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }
}
