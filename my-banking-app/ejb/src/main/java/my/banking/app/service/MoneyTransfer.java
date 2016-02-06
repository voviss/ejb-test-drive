package my.banking.app.service;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import my.banking.app.model.Account;

@Stateless
public class MoneyTransfer {
	
	private static final Logger log = Logger.getLogger(MoneyTransfer.class.getName());

	@Inject
	private EntityManager em;
	
    @Inject
    private Event<Account> accountEventSrc;

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
		makeTransfer(debitAccountId, creditAccountId, money);
	}

	private void makeTransfer(Long debitAccountId, Long creditAccountId, BigDecimal money) {
		withdraw(debitAccountId, money);
		deposit(creditAccountId, money);
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
