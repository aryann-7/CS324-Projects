package data_access_layer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import business_layer.*;

public class DAcustomer {
    private Access_JDBC db;
	public DAcustomer(){
        db = new Access_JDBC();
	}

	public void add(BLcustomer cus) throws Exception{
		String sql = "";
		//try{
			db.connect();
			Statement s = db.getConnect().createStatement();
			sql = sql + "INSERT INTO customer(id, lname, fname) ";
			sql = sql + "VALUES (" + cus.getCusId()+",'"+cus.getLName() + "','" + cus.getFName()+ "')";
			s.execute(sql);
			db.disconnect();
		//}
		//catch (Exception e) {
          //System.err.println("Error: " + e);
      //}
	}

	public void delete(BLcustomer cus){
		//TODO: inLab
		
	}

    public List<BLcustomer> getAll() throws Exception {
        List<BLcustomer> customers = new ArrayList<>();

        db.connect();

		Statement statement = db.getConnect().createStatement();
        ResultSet results = statement.executeQuery(
                     "SELECT id, lname, fname FROM customer ORDER BY id");
					 
		while (results.next()) {
			BLcustomer customer = new BLcustomer();
			customer.setCusId(results.getString("id"));
			customer.setLName(results.getString("lname"));
			customer.setFName(results.getString("fname"));
			customers.add(customer);
		}
            db.disconnect();

        return customers;
    }
}
