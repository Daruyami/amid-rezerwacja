package uk.adamcz.patryk.rezerwacja;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class IdentityHandler {
    public static boolean LoggedIn = false;

    public static UserRole Role = UserRole.Guest;

    public static String Name;

    public static boolean SubmitCredentials(String login, String password, boolean register) {
        String preparedLogin = StringInputUtils.Escape(login);
        String passwordHash = getPasswordHash(password);

        Identity identity;
        if (!register) {
            identity = StorageManager.MatchCredentials(preparedLogin, passwordHash);
        }
        else {
            identity = StorageManager.TryAddCredentials(preparedLogin, passwordHash);

        }

        LoggedIn = identity.LoggedIn;

        if ( LoggedIn ) {
            Role = identity.Role;
            Name = identity.Username;
        }

        return LoggedIn;
    }

    private static String getPasswordHash(String password) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (Exception e) { return ""; }

        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
