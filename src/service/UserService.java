package service;

import exceptions.UserRegistrationException;
import java.util.List;
import model.User;

public class UserService {

  public static User loginUser(String login, String password) {
    List<User> users = FileService.getAllUsersFromFile();
    for (User user : users) {
      if (user.getLogin().equals(login) && user.getPassword().equals(password)) {
        return user;
      }
    }
    return null;
  }

  public static User registerUser(String login, String password)
      throws UserRegistrationException {
    if (checkUser(login) != null) {
      throw new UserRegistrationException("This user already exists.");
    }
      User user = new User(login, password);
      return FileService.addUserToFile(user);

  }

  public static User checkUser(String login) {
    List<User> users = FileService.getAllUsersFromFile();
    for (User user : users) {
      if (user.getLogin().equals(login)) {
        return user;
      }
    }
    return null;
  }

}
