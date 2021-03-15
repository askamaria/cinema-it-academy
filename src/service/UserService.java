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

  public static void updateUser(User user) {
    List<User> users = FileService.getAllUsersFromFile();

    for (int i = 0; i < users.size(); i++) {
      if (users.get(i).getId().equals(user.getId()))
        users.set(i,user);
      FileService.updateUsers(users);
      }
    }

  public static void deleteUser(String userId) {
    List<User> users = FileService.getAllUsersFromFile();
    for (int i = users.size() - 1; i >=0; i--) {
      User user= users.get(i);
      if (users.get(i).getId().equals(userId)) {
        users.remove(i);
        System.out.println("User " + user.getLogin() + " deleted.");
        FileService.updateUsers(users);
        return;
      }
    }
  }
}
