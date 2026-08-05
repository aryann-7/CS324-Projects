package data_access_layer;

import java.sql.*;


public class Access_JDBC {
    private String username;
    private String password;
    private String connectionString;
    private String url;
    private Connection con;

    public void connect() {
    	try {
          
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");           


            // con=DriverManager.getConnection("jdbc:ucanaccess://test.mdb");
             con=DriverManager.getConnection("jdbc:ucanaccess://database//new_test.accdb");
           // con = DriverManager.getConnection( connectionString ,username,password);
          
        }
        catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    public void disconnect(){
    	try{
			//	closes the connection (optional)
			con.close();
    	}
        catch (Exception e) {
            System.err.println("Error: " + e);
        }
    }

    public Connection getConnect(){
    	return this.con;
    }

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

