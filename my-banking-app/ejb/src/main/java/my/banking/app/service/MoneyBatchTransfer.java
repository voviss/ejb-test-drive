package my.banking.app.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import my.banking.app.data.AccountRepository;
import my.banking.app.model.Account;


@Singleton
@TransactionManagement(TransactionManagementType.CONTAINER)
@Startup
public class MoneyBatchTransfer {

	@Inject
	private AccountRepository accountRepository;
	
	@EJB
	private MoneyTransfer moneyTransfer;
	
	@Schedule(second="0", minute="*/1", hour="*", dayOfMonth="*", month="*", dayOfWeek = "*", year="*")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processBatchTransfer(){
		List<Account> accounts = accountRepository.findAllAccountsByIdJPAQL();
		for(Account account : accounts){
			if(account.getId() != Account.NOSTRO_ACCOUNT_ID){
				moneyTransfer.makeBankToClientTransfer(account.getId(), new BigDecimal(10));
			}
		}
	}
	
}
