package my.banking.app.controller;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import my.banking.app.service.MoneyTransfer;

@Named("moneyTransferController")
@SessionScoped
public class MoneyTransferController implements Serializable {
	
	private static final long serialVersionUID = 3996734469801228001L;

	@EJB
	private MoneyTransfer transferService = new MoneyTransfer();	
	
	public void makeTransfer(Long clientId, double money){
		transferService.makeBankToClientTransfer(clientId, BigDecimal.valueOf(money));
	}
	
}
