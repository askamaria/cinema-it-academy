package menu;

import exceptions.UserRegistrationException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import model.User;
import service.HashingService;
import service.LogService;
import service.UserService;

public class LoginMenu {

  public static Scanner scanner=new Scanner(System.in);

  public static void starApplication() {
    int exit = 0;
    while (exit != -1) {
      System.out.println("Hello user!");
      System.out.println("Enter 1 for login, 2 for registration:");

      switch (scanner.nextLine()) {
        case "1":
          loginMenu();
          break;
        case "2":
          registerMenu();
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }
  public static void loginMenu() {
    int count = 0;
    while (count != 3) {
      LogService.addLoggedAction("User chose the login menu ");
      System.out.println("Hello. It's login menu.");
      System.out.println("Please enter login.");
      String login = scanner.nextLine();
      System.out.println("Please enter password.");
      String pass = scanner.nextLine();
      String passwordHash;
      try {
        passwordHash= HashingService.createHash(pass);
      } catch (InvalidKeySpecException e) {
        System.err.println("The provided password can not be hashed.");
        return;
      }
      User user = UserService.loginUser(login,passwordHash );
      if (user == null) {
        LogService.addLoggedAction("User  entered login or password incorrectly");
        System.out.println("Login or password entered incorrectly. Please, try again.");

        count++;
        if (count == 3) {
          System.out.println("You may not be registered. Register, please.");
        }
      } else {
        LogService.addLoggedAction("User " + login + " is logged in.");
        System.out.println("User " + login + " logged in");
        count = 3;

        switch (user.getUserType()) {
          case REGULAR:
            RegularUserMenu.mainMenu(user);
            break;
          case MANAGER:
            ManagerMenu.managerMenu();
            break;

          case ADMIN:
            AdminMenu.adminMenu();
            break;

          default:
            System.out.println("Typo!");
        }
      }
    }
  }

  public static void registerMenu() {
    int count = 0;
    while (count != 3) {
      System.out.println("Hello. It's register menu.");
      LogService.addLoggedAction("User chose the register menu ");
      System.out.println("Please enter login.");
      String login = scanner.nextLine();
      LogService.addLoggedAction("User entered login");
      System.out.println("Please enter password.");
      String pass = scanner.nextLine();
      LogService.addLoggedAction("User entered password");

      try {
        String passwordHash= HashingService.createHash(pass);
        User user = UserService.registerUser(login, passwordHash);
        count = 3;
        LogService.addLoggedAction("User "+ user.getLogin() +" was registered");
        System.out.println("User " + user.getLogin() + " registered.");
        RegularUserMenu.mainMenu(user);

      } catch (UserRegistrationException | InvalidKeySpecException e) {
        count++;
        System.out.println(e.getMessage());
      }
    }
  }
}

