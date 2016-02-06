package org.jboss.as.quickstarts.ear.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.jboss.as.quickstarts.ear.ejb.AccountEJB;
import org.jboss.as.quickstarts.ear.entity.Account;

@Named("account")
@SessionScoped
public class AccountController implements Serializable {
	
	private static final long serialVersionUID = 8316343523156853897L;
	
	@EJB
	private AccountEJB accountEJB = new AccountEJB(); 
	
	private Account nostro;
	private Account vostro;
	
	public AccountController() {
		nostro = new Account();
		nostro.setBalance(100000);
		vostro = new Account();
	}
	
	
	public double makeTransfer(double money){
		return accountEJB.makeTransfer(nostro, vostro, money);
	}
	
	public double getVostroBalance(){
		return vostro.getBalance();
	}
	
	public double getNostroBalance(){
		return nostro.getBalance();
	}
}
