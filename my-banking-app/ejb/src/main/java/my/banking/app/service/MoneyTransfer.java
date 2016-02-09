package my.banking.app.service;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import my.banking.app.mdb.MoneyTransferNotifier;
import my.banking.app.model.Account;

@Stateless
public class MoneyTransfer {
	
	private static final Logger log = Logger.getLogger(MoneyTransfer.class.getName());

	@Inject
	private EntityManager em;
	
    @Inject
    private Event<Account> accountEventSrc;
    
    @Inject
    private MoneyTransferNotifier moneyTransferLocalNotifier;
    
    @Resource
    private SessionContext sessionContext;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void makeBankToClientTransfer(Long creditAccountId, BigDecimal money) {
		makeClientToClientTransfer(Account.NOSTRO_ACCOUNT_ID, creditAccountId, money);
	}

	public void makeClientToBankTransfer(Long debitAccountId, BigDecimal money) {
		makeClientToClientTransfer(debitAccountId, Account.NOSTRO_ACCOUNT_ID, money);
	}

	public void makeClientToClientTransfer(Long debitAccountId, Long creditAccountId, BigDecimal money) {
		log.info("debitAccountId=" + debitAccountId);
		log.info("creditAccountId=" + creditAccountId);
		log.info("money=" + money.toString());
		try {
			makeTransfer(debitAccountId, creditAccountId, money);
			moneyTransferLocalNotifier.sendNotification("The amount of " + money + " has been transfered on your account " + creditAccountId);
		} catch (AccountFrozen e) {
			sessionContext.setRollbackOnly();
			moneyTransferLocalNotifier.sendNotification("Mail to administrator: An error occured while transfering " + money + " on account " + creditAccountId);
			log.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	private void makeTransfer(Long debitAccountId, Long creditAccountId, BigDecimal money) throws AccountFrozen {
		withdraw(debitAccountId, money);
		deposit(creditAccountId, money);
		if(creditAccountId == 2l){
			throw new AccountFrozen(creditAccountId);
		}
	}

	public void deposit(Long accountId, BigDecimal money) {
		log.info("account: " + accountId);
		Account account = em.find(Account.class, accountId);
		log.info("Balance before change: " + account.getBalance());
		account.setBalance(account.getBalance().add(money));
		log.info("Balance after change: " + account.getBalance());
		accountEventSrc.fire(account);
	}

	public void withdraw(Long accountId, BigDecimal money) {
		deposit(accountId, money.negate());
	}
}

//@ApplicationException(rollback=true)
class AccountFrozen extends Exception {

	public AccountFrozen(Long accountId) {
		super("Cannot make a transfer frozen account " + accountId);
	}
}


