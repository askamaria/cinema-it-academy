package service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class HashingService {
  public static byte[] salt = "my_salt".getBytes();
  public static SecretKeyFactory factory;
  static {
    try {
      factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }
  public static String createHash(String password) throws InvalidKeySpecException {

    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
      byte[] hash = factory.generateSecret(spec).getEncoded();
      String stringHash=new String(hash);
    return stringHash;
  }

}
