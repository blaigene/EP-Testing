package micromobility.payment;

import micromobility.exceptions.NotEnoughWalletException;
import java.math.BigDecimal;

public class Wallet {

    private BigDecimal balance;

    public Wallet(BigDecimal initialBalance) {
        this.balance = initialBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void deduct(BigDecimal imp) throws NotEnoughWalletException {
        if(balance.compareTo(imp) < 0) {
            throw new NotEnoughWalletException("Saldo insuficient per realitzar el pagament.");
        }
        balance = balance.subtract(imp);
    }

    public void add(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
