package my.banking.app.service;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Session;

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

	public void makeClientToClientTransfer(Long debitAccountID, Long creditAccountId, BigDecimal money) {
		log.info("debitAccountID=" + debitAccountID);
		log.info("creditAccountId=" + creditAccountId);
		log.info("money=" + money.toString());
		Account debitAccount = em.find(Account.class, debitAccountID);
		Account creditAccount = em.find(Account.class, creditAccountId);
		makeTransfer(debitAccount, creditAccount, money);
		em.merge(creditAccount);
		em.merge(debitAccount);
		log.info("debitAccount balance after change: " + debitAccount.getBalance());
		log.info("creditAccount balance after change: " + creditAccount.getBalance());
		accountEventSrc.fire(creditAccount);
		accountEventSrc.fire(debitAccount);
	}

	private void makeTransfer(Account debitAccount, Account creditAccount, BigDecimal money) {
		withdraw(debitAccount, money);
		deposit(creditAccount, money);
	}

	public void deposit(Account account, BigDecimal money) {
		account.setBalance(account.getBalance().add(money));
	}

	public void withdraw(Account account, BigDecimal money) {
		account.setBalance(account.getBalance().subtract(money));
	}
}
