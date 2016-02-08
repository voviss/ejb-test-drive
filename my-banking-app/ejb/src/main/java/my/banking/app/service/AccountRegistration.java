package my.banking.app.service;

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import my.banking.app.model.Account;

@Stateless
public class AccountRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Account> memberEventSrc;

    public void register(Account account) throws Exception {
        log.info("Registering " + account.getName());
        em.persist(account);
        memberEventSrc.fire(account);
    }
}
