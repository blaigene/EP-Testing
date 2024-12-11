package data;

/**
 * Classe que representa un compte d'usuari.
 */
final public class UserAccount {
    private final String username;

    public UserAccount(String username) {
        // CANVIAR EXCEPCIÓ
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nom d'usuari no pot ser nul o buit.");
        }
        // DEFINIR 15 CARÀCTERS
        if (!username.matches("[a-zA-Z0-9._-]{3,15}")) {
            throw new IllegalArgumentException("El nom d'usuari ha de tenir entre 3 i 15 caràcters (lletres, números, '.', '-', '_').");
        }
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "username='" + username + '\'' +
                '}';
    }
}
