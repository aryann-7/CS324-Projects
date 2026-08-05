/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shiuram_A
 */
package business_layer;
import java.util.List;
import data_access_layer.DAcustomer;

public class BLcustomer{
	private String cusId;
	private String fName;
    private String lName;

    private DAcustomer cusData;

    public BLcustomer(){
    	cusData = new DAcustomer();
    }

	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getFName() {
		return fName;
	}
	public void setFName(String name) {
		fName = name;
	}
	public String getLName() {
		return lName;
	}
	public void setLName(String name) {
		lName = name;
	}

	public void add() throws Exception{
		cusData.add(this);
	}

    public void delete() throws Exception{
		cusData.delete(this);
	}

    public List<BLcustomer> getAll() throws Exception {
        return cusData.getAll();
    }

}