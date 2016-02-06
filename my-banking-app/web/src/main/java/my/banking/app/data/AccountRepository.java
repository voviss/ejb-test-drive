package my.banking.app.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import my.banking.app.model.Account;

@ApplicationScoped
public class AccountRepository {

    @Inject
    private EntityManager em;

    public Account findById(Long id) {
        return em.find(Account.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Account> findAllOrderedById() {
        // using Hibernate Session and Criteria Query via Hibernate Native API
        Session session = (Session) em.getDelegate();
        Criteria cb = session.createCriteria(Account.class);
        cb.addOrder(Order.asc("id"));
        return (List<Account>) cb.list();
    }
}
