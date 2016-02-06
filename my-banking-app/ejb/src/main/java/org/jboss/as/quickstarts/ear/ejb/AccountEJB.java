package org.jboss.as.quickstarts.ear.ejb;

import javax.ejb.Stateless;

import org.jboss.as.quickstarts.ear.entity.Account;

@Stateless
public class AccountEJB {

    public double makeTransfer(Account debit, Account credit, double money) {
        withdraw(debit, money);
        return deposit(credit, money);
    }
        
    
	public double deposit(Account account, double money){
		account.setBalance(account.getBalance() + money);
		return account.getBalance();
	}
	
	public double withdraw(Account account, double money){
		account.setBalance(account.getBalance() - money);
		return account.getBalance();
	}
}
