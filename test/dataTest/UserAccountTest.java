package dataTest;

import data.UserAccount;
import data.exceptions.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar la classe UserAccount seguint la estructura JUnit 5.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserAccountTest {

    @BeforeAll
    static void initAll() {
        System.out.println("Iniciant els tests per a la classe UserAccount.");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testConstructor() {
        assertThrows(NullArgumentException.class, () -> new UserAccount(""));
        assertThrows(NullArgumentException.class, () -> new UserAccount(null));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("ab"));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("0123456789abcdef"));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("user@name"));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("OriolViñes01234"));
        assertDoesNotThrow(() -> new UserAccount("BlaiGene0123456"));
    }

    @Test
    void testGetters() {
        UserAccount account1 = new UserAccount("BlaiGene0123456");
        assertEquals("BlaiGene0123456", account1.getUsername());
    }

    @Test
    void testEquals() {
        UserAccount account1 = new UserAccount("BlaiGene0123456");
        UserAccount account2 = new UserAccount("BlaiGene0123456");
        UserAccount account3 = new UserAccount("OriolVines01234");

        assertTrue(account1.equals(account2));
        assertFalse(account1.equals(account3));
        assertFalse(account1.equals(null));
        assertFalse(account1.equals("BlaiGene0123456"));
    }

    @Test
    void testHashCode() {
        UserAccount account1 = new UserAccount("BlaiGene0123456");
        UserAccount account2 = new UserAccount("BlaiGene0123456");
        UserAccount account3 = new UserAccount("OriolVines01234");

        assertEquals(account1.hashCode(), account2.hashCode());
        assertNotEquals(account1.hashCode(), account3.hashCode());
    }

    @Test
    void testToString() {
        UserAccount account1 = new UserAccount("OriolVines01234");
        String expected = "UserAccount{username='OriolVines01234'}";
        assertEquals(expected, account1.toString());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finalitzat. Netejant el context...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tots els tests han finalitzat.");
    }

}
