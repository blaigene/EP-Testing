package micromobility.payment;

import data.ServiceID;
import data.UserAccount;
import micromobility.JourneyService;
import micromobility.exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class WalletPayment extends Payment {
    private Wallet wallet;

    public WalletPayment(ServiceID id, UserAccount user, BigDecimal imp, Wallet wallet) {
        super(id, user, imp); // Crida al constructor de Payment
        this.wallet = wallet;
    }

    @Override
    public void processPayment() throws NotEnoughWalletException {
        wallet.deduct(getImport()); //En cas que l'usuari tingui els suficients diners es cobra l'import.
    }
}
