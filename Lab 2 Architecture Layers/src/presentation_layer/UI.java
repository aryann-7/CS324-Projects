package presentation_layer;

import java.util.*;
import business_layer.*;

class UI{
	private BLcustomer cus;
	private final Scanner in;

	public UI(){
		cus = new BLcustomer();
		in = new Scanner(System.in);
	}

	private int choice;

	public int getChoice() {
		return choice;
	}

	public void setChoice(int choice) {
		this.choice = choice;
	}

	public void add_customer(){
		System.out.println("Enter id: ");
		cus.setCusId(in.next());

		System.out.println("Enter last name: ");
		cus.setLName(in.next());
		System.out.println("Enter first name: ");
		cus.setFName(in.next());

		try{
			cus.add();
			System.out.println("addition successful");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public void update_customer(){
		//TODO: Homework
	}
        
	public void delete_customer(){
		//TODO: inLab
		
	}

	public void view_all_customers(){
		//TODO: inLab (HINT: use List, exmaine the BLcustomer class for the method to call)
		// try {
			
			
		// }
		// catch(Exception e){
		// 	e.printStackTrace();
		// }
	}

	public void print(){
		do {
			System.out.println("press 1 to add a customer");
			System.out.println("press 2 to delete a customer");
			System.out.println("press 3 to update a customer");
			System.out.println("press 4 to view all current customers");
			System.out.println("press 5 to exit the application");
			choice = in.nextInt();

			if (choice == 1){
				this.add_customer();
			}
			else if (choice == 2){
				this.delete_customer();
			}
			else if (choice == 3){
				this.update_customer();
			}
			else if (choice == 4){
				this.view_all_customers();
			}
			else if (choice != 5){
				System.out.println("Invalid choice. Please try again.");
			}
		} while (choice != 5);
	}
}
