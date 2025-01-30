package micromobility.payment;

import data.ServiceID;
import data.UserAccount;
import micromobility.JourneyService;
import micromobility.exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public abstract class Payment {
    private ServiceID id;
    private UserAccount user;
    private BigDecimal imp;

    public Payment(ServiceID id, UserAccount user, BigDecimal imp) {
        this.id = id;
        this.user = user;
        this.imp = imp;
    }

    // Getters

    public ServiceID getId() {
        return id;
    }

    public UserAccount getUser() {
        return user;
    }

    public BigDecimal getImport() {
        return imp;
    }

    // Mètode que WalletPayment implementara (en aquest cas d'ús)
    public abstract void processPayment() throws NotEnoughWalletException;
}
