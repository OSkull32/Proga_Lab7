package server.utility;

import server.App;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-224");
            byte[] bytes = messageDigest.digest(password.getBytes());
            BigInteger integer = new BigInteger(1, bytes);
            String newPassword = integer.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException ex) {
            App.logger.severe("Не найден алгоритм хэширования пароля");
            throw new IllegalStateException(ex);
        }
    }
}
