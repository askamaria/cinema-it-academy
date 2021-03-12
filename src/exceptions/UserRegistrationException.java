package exceptions;

public class UserRegistrationException extends Exception {

  public UserRegistrationException() {
  }

  public UserRegistrationException(String message) {
    super(message);
  }
}
