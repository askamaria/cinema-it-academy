package menu;

import exceptions.FilmsException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Film;
import model.User;
import service.FileService;
import service.FilmService;
import service.LogService;

public class ManagerMenu {
  public static Scanner scanner=new Scanner(System.in);
  public static void managerMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Manager entered.");
      System.out.println("Hello manager!");
      System.out.println("Select an option:");
      System.out.println("1- Edit the movie.");
      System.out.println("2- Buy / return a user ticket.");
      System.out.println("-1 - Exit");

      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      switch (number) {
        case 1:
          managerEditFilmMenu();
          break;
        case 2:
          managerTicketMenu();
          break;
        case -1:
          LogService.addLoggedAction("Manager exited manager menu");
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void managerEditFilmMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Manager entered to the manager edit film menu.");
      System.out.println("Hello. It's manager edit film menu.");
      System.out.println("Select an option:");
      System.out.println("1- Change the movie.");
      System.out.println("2- Add the movie.");
      System.out.println("-1 - Exit.");
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      switch (number) {
        case 1:
          managerChangeFilmMenu();
          break;
        case 2:
          managerAddFilmMenu();
          break;
        case -1:
          LogService.addLoggedAction("Manager exited manager edit film menu");
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void managerChangeFilmMenu() {
    int number = 0;
    String str = null;
    while (number != -1) {
      LogService.addLoggedAction("Manager entered to the manager change film menu.");
      System.out.println("Hello. It's change film menu.");
      System.out.println("Choose a movie number to change and -1 to exit. ");
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
        System.out.println("Old movie name: " + film.getFilmName());
        System.out.println("Enter a new name or /'Enter/' to skip");
        str = scanner.nextLine();
        if (!str.isEmpty()) {
          film.setFilmName(str);
        }
        LogService.addLoggedAction("Manager entered the new film name " +str);
        int dateExitFlag = 0;
        while (dateExitFlag != -1) {
          System.out.println("Old film time: " + film.getFilmTime());
          System.out
              .println("Enter a new film time in the format dd.MM.yyyy:HH.mm or /'Enter/' to skip");
          str = scanner.nextLine();
          if (!str.isEmpty()) {
            try {
              film.setFilmTime(str);
            } catch (DateTimeParseException e) {
              System.err.println("The format of the entered date is incorrect.");
            }
          } else {
            dateExitFlag = -1;
            LogService.addLoggedAction("Manager exited manager change film menu");
          }
          FilmService.updateFilm(film);
          LogService.addLoggedAction("Manager entered the new film name " +str);
          System.out.println("Film was changed.");
        }
      }
    }
  }

  public static void managerAddFilmMenu() {
    while (true) {
      LogService.addLoggedAction("Manager entered to the manager add film menu.");
      System.out.println("Hello. It's manager add film menu.");
      System.out.println("Please enter film name or -1 to exit.");
      String filmName = scanner.nextLine();
      if (filmName.equals("-1")) {
        return;
      }
      System.out.println("Please enter film date in the format dd.MM.yyyy:HH.mm or -1 to exit.");
      try {
        String filmTime = scanner.nextLine();
        if (filmTime.equals("-1")) {
          LogService.addLoggedAction("Manager exited manager add film menu");
          return;
        }
        Film film = new Film(filmName, filmTime);
        FilmService.createFilms(film);
        LogService.addLoggedAction("Manager added a new movie " +filmName);
        System.out.println("Film was created.");
      } catch (DateTimeParseException e) {
        System.err.println("The format of the entered date is incorrect.");
      }
    }
  }

  public static void managerTicketMenu() {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("Manager entered to the manager ticket menu.");
      System.out.println("Hello. It's manager change ticket menu.");
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
        System.out.println("1- Вuy user ticket.");
        System.out.println("2- Return purchased user ticket.");
        System.out.println("-1 - Exit");
        try {
          number = Integer.parseInt(scanner.nextLine());
        }catch (NumberFormatException e){
          System.err.println("Wrong line. Not a number");
          continue;
        }
        switch (number) {
          case 1:
            RegularUserMenu.filmMenu(user);
            break;
          case 2:
            RegularUserMenu.returnPurchasedTicketMenu(user);
            break;
          case -1:
            LogService.addLoggedAction("Manager exited manager ticket menu");
            System.out.println("Exit.");
            break;
          default:
            System.out.println("Typo!");
        }
      }
    }
  }
}
