package my.banking.app.data;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;

import my.banking.app.model.Account;

@ApplicationScoped
@NamedQuery(name = "findById", query = "select a from Account a where a.id = :id")
public class AccountRepositoryNamedQuery {
	
	@Inject
	private EntityManager em;
	
	public List<Account> findByIdJPANamedQueries(Long id) {
	    TypedQuery<Account> findByName = em.createNamedQuery("findById", Account.class);
	    findByName.setParameter("id", id);
	    return findByName.getResultList();
	}

}
