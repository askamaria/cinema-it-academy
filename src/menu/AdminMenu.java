package menu;

import exceptions.FilmsException;
import java.util.List;
import java.util.Scanner;
import model.Film;
import model.User;
import model.UserType;
import service.FileService;
import service.FilmService;
import service.LogService;
import service.UserService;

public class AdminMenu {
  public static Scanner scanner=new Scanner(System.in);
  public static void adminMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Admin entered.");
      System.out.println(" Hello admin!");
      System.out.println("Select an option:");
      System.out.println("1- Edit the movie.");
      System.out.println("2- Change/ delete a user");
      System.out.println("-1 - Exit.");

      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      switch (number) {
        case 1:
          adminEditFilmMenu();
          break;
        case 2:
          adminEditUserMenu();
          break;
        case -1:
          LogService.addLoggedAction("Admin exited admin menu");
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void adminEditFilmMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Admin entered to the admin edit film menu.");
      System.out.println("Hello. It's admin edit film menu.");
      System.out.println("Select an option:");
      System.out.println("1- Change the movie.");
      System.out.println("2- Delete the movie.");
      System.out.println("-1 - Exit.");
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      switch (number) {
        case 1:
          ManagerMenu.managerChangeFilmMenu();
          break;
        case 2:
          deleteFilm();
          break;
        case -1:
          LogService.addLoggedAction("Admin exited admin edit film menu");
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void adminEditUserMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Admin entered to the admin edit user menu.");
      System.out.println("Hello. It's admin edit user menu.");
      List<User> users = FileService.getAllUsersFromFile();
      System.out.println("Choose a user number or -1 to exit.");
      for (int i = 0; i < users.size(); i++) {
        System.out.println("User " + i + " =" + users.get(i));
      }
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      if (number == -1) {
        return;
      }
      if (number >= users.size() || number < -1) {
        System.out.println("There is no such user.");
      } else {
        User user = users.get(number);
        System.out.println("Select an option:");
        System.out.println("1- Delete user.");
        System.out.println("2- Change user.");
        System.out.println("-1 - Exit");
        try {
          number = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
          System.err.println("Wrong line. Not a number");
          continue;
        }
        switch (number) {
          case 1:
            UserService.deleteUser(user.getId());
            break;
          case 2:
            adminChangeUser(user);
            break;
          case -1:
            LogService.addLoggedAction("Admin exited admin edit user menu");
            System.out.println("Exit.");
            break;
          default:
            System.out.println("Typo!");
        }
      }
    }
  }

  public static void adminChangeUser(User user) {
    String str;
    LogService.addLoggedAction("Admin entered to the admin change user menu.");
    System.out.println("Hello. It's admin change user menu.");
    System.out.println("Old user login: " + user.getLogin());
    System.out.println("Enter a new login for user or /'Enter/' to skip");
    str = scanner.nextLine();
    if (!str.isEmpty()) {
      user.setLogin(str);
    }
    LogService.addLoggedAction("User "+user.getLogin()+" was changed to "+str);
    System.out.println("Old user type: " + user.getUserType());
    System.out.println("Enter a new user type in UPPER CASE for user or /'Enter/' to skip");
    str = scanner.nextLine();
    if (!str.isEmpty()) {
      try {
        user.setUserType(UserType.valueOf(str));
      } catch (IllegalArgumentException e) {
        System.err.println("Wrong user type. Please try again.");
        return;
      }
    }
    UserService.updateUser(user);
    System.out.println("User was changed.");
  }

  public static void deleteFilm() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Admin entered to the admin delete film menu.");
      System.out.println("Choose a movie number to delete and -1 to exit. ");
      List<Film> films = null;
      try {
        films = FilmService.getAllFilms();
      } catch (FilmsException e) {
        System.out.println(e.getMessage());
        return;
      }
      for (int i = 0; i < films.size(); i++) {
        System.out.println("Film " + i + " =" + films.get(i));
      }
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      if (number == -1) {
        return;
      }
      if (number >= films.size() || number < -1) {
        System.out.println("There is no such movie.");
      } else {
        Film film = films.get(number);
        FilmService.deleteFilm(film.getId());
      }
    }
  }
}
