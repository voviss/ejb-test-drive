package my.banking.app.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import my.banking.app.model.Account;

@ApplicationScoped
public class AccountRepository {

    @Inject
    private EntityManager em;

    @SuppressWarnings("unchecked")
    public List<Account> findAllOrderedByIdViaHibernateNativeApi() {
        Session session = (Session) em.getDelegate();
        Criteria cb = session.createCriteria(Account.class);
        cb.addOrder(Order.asc("id"));
        return (List<Account>) cb.list();
    }
    
    public List<Account> findAllOrderedByIdJPACriteria() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> from = query.from(Account.class);
        query.orderBy(criteriaBuilder.asc(from.get("id")));
        return em.createQuery(query).getResultList();
    }
    
    public List<Account> findAllAccountsByIdJPAQL() {
        return em.createQuery("select a from Account a order by a.id", Account.class).getResultList();
    }
}
