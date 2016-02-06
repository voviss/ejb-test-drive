package org.jboss.as.quickstarts.ear.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("account")
@SessionScoped
public class Account implements Serializable {
	
	private static final long serialVersionUID = 8316343523156853897L;
	private long id;
	private double balance;
	private String currency;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double makeTransfer(double transfer){
		balance += transfer;
		return balance;
	}
}
