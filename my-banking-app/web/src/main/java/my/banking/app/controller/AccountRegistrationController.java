package my.banking.app.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import my.banking.app.model.Account;
import my.banking.app.service.AccountRegistration;

// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
// EL name
// Read more about the @Model stereotype in this FAQ:
// http://www.cdi-spec.org/faq/#accordion6
@Model
public class AccountRegistrationController {

    @Inject
    private FacesContext facesContext;

    @EJB
    private AccountRegistration accountRegistrationService;

    private Account newAccount;

    @Produces
    @Named
    public Account getNewAccount() {
        return newAccount;
    }

    public void register() {
        try {
            accountRegistrationService.register(newAccount);
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "Registration successful"));
            initNewAccount();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, "Registration unsuccessful"));
        }
    }

    @PostConstruct
    public void initNewAccount() {
        newAccount = new Account();
    }

    private String getRootErrorMessage(Exception e) {
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            return errorMessage;
        }

        Throwable t = e;
        while (t != null) {
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        return errorMessage;
    }

}
