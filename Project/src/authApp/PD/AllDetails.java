package authApp.PD;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;


/**
 * This class handles all communication to the SQLite database reguarding anything to do with Employee Personal Details
 * @author Tsotne
 *
 */
public class AllDetails {
	
	private Connection myDb = null;
	private static MyPdStore myCurrentDetails;
	
	public AllDetails(int id) {
		connectToDb();
		myCurrentDetails = new MyPdStore();
	}
	
	/**
	* Method to connect DB
	* @return Database
	*/
	public Connection connectToDb() {
		try {
			 // load the SQLite-JDBC driver using the current class loader
		      Class.forName("org.sqlite.JDBC");
		      myDb = DriverManager.getConnection("jdbc:sqlite:Auth.db");
		      Statement statement = myDb.createStatement();
		      statement.setQueryTimeout(30);  // set timeout to 30 seconds.
		      return myDb;
		}catch(Exception e){
			JOptionPane.showMessageDialog(null,
	    		    "Cannot Connect to DB",
	    		    "Error",
	    		    JOptionPane.ERROR_MESSAGE);
			
			return null;
		}
	}
	
	/**
	 * Checks if Personal Details Exists for this User
	 * @return Boolean
	 */
	public boolean checkDb(int userId) {
		
		String sql = "select id from PersonalDetails where id='"+userId+"'";
		
		try(Connection conn = myDb;
			Statement stmt = conn.createStatement();
			ResultSet rs  = stmt.executeQuery(sql)){
				if(rs.getInt("id") == userId) {
					rs.close();
					myDb.close();
					return true;
				}
		}catch(SQLException e){
			
			myDb = null;
		}
		return false;
	}
	
	/**
	* Loads the Details from the Database that matches the usersId
	* @param userId
	*/
	public void pullDetails(int userId) {
		String sql = "select * from PersonalDetails where id='"+userId+"'";
		
		connectToDb();
		
		try(Connection conn = myDb;
			Statement stmt = conn.createStatement();
			ResultSet rs  = stmt.executeQuery(sql)){
			
				int foundId = rs.getInt("id");
				if(foundId == userId) {
					// Get new details from database
					String firstName = rs.getString("fName");
					String lastName = rs.getString("sName");
					String dob = rs.getString("dob");
					String address1 = rs.getString("address1");
					String address2 = rs.getString("address2");
					String city = rs.getString("city");
					String county = rs.getString("county");
					String postCode = rs.getString("postcode");
					String mobileNum = rs.getString("mobileNum");
					String telephoneNum = rs.getString("telephoneNum");
					String emergencyContact = rs.getString("emergencyContact");
					String emergencyNum = rs.getString("emergencyContactNum");
			
				
					// Set new Details
					myCurrentDetails.setFirstName(firstName);
					myCurrentDetails.setLastName(lastName);
					myCurrentDetails.setDoB(dob);
					myCurrentDetails.setAddress1(address1);
					myCurrentDetails.setAddress2(address2);
					myCurrentDetails.setCity(city);
					myCurrentDetails.setCounty(county);
					myCurrentDetails.setPostcode(postCode);
					myCurrentDetails.setMobile(mobileNum);
					myCurrentDetails.setTelenum(telephoneNum);
					myCurrentDetails.setEmergencyContact(emergencyContact);
					myCurrentDetails.setEmergencyNum(emergencyNum);
			}else {
				JOptionPane.showMessageDialog(null,
		    		    "Connection Made but User not found!",
		    		    "Error-User Not Found",
		    		    JOptionPane.ERROR_MESSAGE);
			}
		}catch(SQLException e){
			JOptionPane.showMessageDialog(null,
	    		    "Cannot Fetch Your Details!",
	    		    "Error",
	    		    JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * This method saves the new information and pushes it onto the database
	 * @param id
	 */
	public void pushDetails(int id) {
		String sql = "UPDATE PersonalDetails SET fName = ? , "
                + "sName = ? , " + "dob = ? , " + "address1 = ? , " + "address2 = ? , " + "city = ? , " 
				+ "county = ? , " + "postcode = ? , " + "telephoneNum = ? , " + "mobileNum = ? , " 
                + "emergencyContact = ? , " + "emergencyContactNum = ? "
                + "WHERE id = ?";
		connectToDb();
		
        try (Connection conn = myDb;
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, getMyCurrentDetails().getFirstName());
            pstmt.setString(2, getMyCurrentDetails().getLastName());
            pstmt.setString(3, getMyCurrentDetails().getDoB());
            
            
            pstmt.setString(4, getMyCurrentDetails().getAddress1());
            pstmt.setString(5, getMyCurrentDetails().getAddress2());
            pstmt.setString(6, getMyCurrentDetails().getCity());
            pstmt.setString(7, getMyCurrentDetails().getCounty());
            pstmt.setString(8, getMyCurrentDetails().getPostcode());
            pstmt.setString(9, getMyCurrentDetails().getTelenum());
            pstmt.setString(10, getMyCurrentDetails().getMobile());
            pstmt.setString(11, getMyCurrentDetails().getEmergencyContact());
            pstmt.setString(12, getMyCurrentDetails().getEmergencyNum());
            pstmt.setInt(13, id);
            		
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
        	JOptionPane.showMessageDialog(null,
	    		    "Cannot connect to the Database",
	    		    "Error",
	    		    JOptionPane.ERROR_MESSAGE);
        }
	}
	
	
	/**
	 * Returns current Users Personal Details
	 * @return myCurrentDetails
	 */
	public static MyPdStore getMyCurrentDetails() {
		return myCurrentDetails;
	}
}
