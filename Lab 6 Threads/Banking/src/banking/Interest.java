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
class Interest extends java.lang.Thread {
    final Account myAccount;
    double myRate;
    
    public Interest(Account account, double rate) {
          this.myAccount=account;
          this.myRate = rate;
          setName("Interest");
    }
   
    @Override
    public void run() {
        
         System.out.println("Interest this month on "+  myAccount.accountType + ":\t" + myAccount.balance*myRate/100.0);
         myAccount.addinterest(myRate);
         
          //System.out.println(getName() + " to account " + myAccount.accountType + " successfully applied");
    }
    
    
    
}
