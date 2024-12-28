package micromobility.payment;

import data.UserAccount;
import micromobility.JourneyService;
import micromobility.exceptions.NotEnoughWalletException;

import java.math.BigDecimal;

public abstract class Payment {
    private char payMeth;
    private UserAccount user;
    private BigDecimal imp;

    public Payment(char payMeth, UserAccount user, BigDecimal imp) {
        this.payMeth = payMeth;
        this.user = user;
        this.imp = imp;
    }

    // Getters

    public char getPayMeth() {
        return payMeth;
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
