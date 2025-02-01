package micromobilityTest;

import data.ServiceID;
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
    private ServiceID id;

    @BeforeAll
    static void setupAll() {
        System.out.println("INICIALIZANDO PAYMENT TESTS");
    }

    @BeforeEach
    void setUp() {
        // Inicializamos el saldo inicial del monedero y el usuario antes de cada test
        initialBalance = new BigDecimal("100.00");  // Establecemos un saldo inicial para el monedero
        wallet = new Wallet(initialBalance);  // Creamos un nuevo monedero con el saldo inicial
        user = new UserAccount("john.doe1234567");  // Crear un usuario con un nombre que cumpla con los 15 caracteres requeridos
        id = new ServiceID("SE123456");
    }

    @Test
    void testWalletHasSufficientFunds() throws NotEnoughWalletException {
        // Test per comprovar que es processa correctament quan hi ha suficients fons
        BigDecimal paymentAmount = new BigDecimal("50.00");
        WalletPayment walletPayment = new WalletPayment(id, user, paymentAmount, wallet);

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
        WalletPayment walletPayment = new WalletPayment(id, user, paymentAmount, wallet);

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

        WalletPayment firstWalletPayment = new WalletPayment(id, user, firstPayment, wallet);
        WalletPayment secondWalletPayment = new WalletPayment(id, user, secondPayment, wallet);

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
        System.out.println("FINALIZANDO PAYMENT TESTS");
        System.out.println();
    }
}
