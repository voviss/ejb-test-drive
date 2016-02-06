package my.banking.app.service;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.logging.Logger;
import org.hibernate.Session;

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
        //em.persist(account);

        // using Hibernate session(Native API) and JPA entitymanager
        Session session = (Session) em.getDelegate();
        session.persist(account);
        memberEventSrc.fire(account);
    }
}
