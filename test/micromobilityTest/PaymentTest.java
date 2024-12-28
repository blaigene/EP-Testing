package micromobilityTest;

import micromobility.exceptions.NotEnoughWalletException;
import data.UserAccount;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Wallet wallet;
    private UserAccount user;
    private BigDecimal initialBalance;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant tests del package Payment...");
    }

    @BeforeEach
    void setUp() {
        // Inicialitzem el saldo inicial del moneder i l'usuari abans de cada test
        initialBalance = new BigDecimal("100.00");
        wallet = new Wallet(initialBalance);
        user = new UserAccount("john_doe");
    }

    @Test
    void testWalletHasSufficientFunds() throws NotEnoughWalletException {
        // Test per comprovar que es processa correctament quan hi ha suficients fons
        BigDecimal paymentAmount = new BigDecimal("50.00");
        WalletPayment walletPayment = new WalletPayment('W', user, paymentAmount, wallet);

        // Processem el pagament
        walletPayment.processPayment();

        // Comprovem que el saldo del moneder ha disminuït adequadament
        BigDecimal expectedBalance = initialBalance.subtract(paymentAmount);
        assertEquals(expectedBalance, wallet.getBalance(), "El saldo del moneder ha de disminuir correctament.");
    }

    @Test
    void testWalletHasInsufficientFunds() {
        // Test per comprovar que es llença una excepció si no hi ha suficients fons
        BigDecimal paymentAmount = new BigDecimal("300.00");
        WalletPayment walletPayment = new WalletPayment('W', user, paymentAmount, wallet);

        // Verifiquem que es llença l'excepció NotEnoughWalletException
        assertThrows(NotEnoughWalletException.class, () -> {
            walletPayment.processPayment();
        }, "S'ha de llençar una excepció quan no hi ha suficients fons al moneder.");
    }

    @Test
    void testWalletBalanceUpdateAfterMultiplePayments() throws NotEnoughWalletException {
        // Test per comprovar l'actualització del saldo després de múltiples pagaments
        BigDecimal firstPayment = new BigDecimal("30.00");
        BigDecimal secondPayment = new BigDecimal("20.00");

        WalletPayment firstWalletPayment = new WalletPayment('W', user, firstPayment, wallet);
        WalletPayment secondWalletPayment = new WalletPayment('W', user, secondPayment, wallet);

        // Processem els dos pagaments
        firstWalletPayment.processPayment();
        secondWalletPayment.processPayment();

        // Comprovem que el saldo del moneder s'ha actualitzat correctament després dels pagaments
        BigDecimal expectedBalance = initialBalance.subtract(firstPayment).subtract(secondPayment);
        assertEquals(expectedBalance, wallet.getBalance(), "El saldo del moneder ha de reflectir els dos pagaments.");
    }

    @Test
    void testAddFundsToWallet() {
        // Test per comprovar que es poden afegir fons al moneder
        BigDecimal fundsToAdd = new BigDecimal("50.00");
        wallet.add(fundsToAdd);

        // Comprovem que el saldo s'actualitza correctament després d'afegir fons
        BigDecimal expectedBalance = initialBalance.add(fundsToAdd);
        assertEquals(expectedBalance, wallet.getBalance(), "El saldo del moneder ha d'augmentar després d'afegir fons.");
    }

    @AfterAll
    static void tearDownAll() {
        // Executat després de tots els tests
        System.out.println("Finalitzats els tests de Payment.");
    }
}
