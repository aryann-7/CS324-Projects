/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author fehnker_a
 */
class Transfer extends java.lang.Thread {    
    final Account myAccount1;
    Account myAccount2;
    double myAmount;
    
    public Transfer(Account account1,Account account2, double amount) {
          this.myAccount1=account1;
          this.myAccount2=account2;
          this.myAmount = amount;
          setName("Transfer");
    }
   
    @Override
    public void run() {
        
        
        myAccount1.withdraw(myAmount);
        myAccount2.deposit(myAmount);
       
        System.out.println(getName() + " from " + myAccount1.accountType + " to " + myAccount2.accountType + " successfully applied");
    }
    
    
    
}

