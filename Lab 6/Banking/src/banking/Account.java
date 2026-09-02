/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

/**
 *
 * @author fehnker_a
 */
public class Account {
    final String accountHolder;
    final String accountType;
    double balance=0;
    
    public Account(String name, String type,double credit) {
        this.accountHolder = name;
        this.accountType = type;        
        this.balance=credit;
    }
    
    public void deposit(double credit) {
        balance += credit;
    }
    
    public void withdraw(double credit) {
        balance -= credit;
    }
    
    public void addinterest(double rate) {
        balance *= (100+rate)/100.0;
    }
    
}
