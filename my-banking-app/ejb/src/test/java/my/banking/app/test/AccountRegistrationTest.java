package my.banking.app.test;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import my.banking.app.model.Account;
import my.banking.app.service.AccountRegistration;
import my.banking.app.util.Resources;

@RunWith(Arquillian.class)
public class AccountRegistrationTest {
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Account.class, AccountRegistration.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Inject
    AccountRegistration accountRegistration;

    @Inject
    Logger log;

    @Test
    public void testRegister() throws Exception {
        Account newAccount = new Account();
        newAccount.setName("John Doe");
        newAccount.setId(7l);
        newAccount.setBalance(new BigDecimal(300000));
        accountRegistration.register(newAccount);
        assertNotNull(newAccount.getId());
        log.info(newAccount.getName() + " was persisted with id " + newAccount.getId());
    }

}
