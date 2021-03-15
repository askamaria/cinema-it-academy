package view;

import exceptions.FilmsException;
import exceptions.UserRegistrationException;
import java.security.spec.InvalidKeySpecException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import model.Film;
import model.Ticket;
import model.User;
import model.UserType;
import service.FileService;
import service.FilmService;
import service.HashingService;
import service.LogService;
import service.TicketService;
import service.UserService;

public class Main {

  public static Scanner scanner;

  public static void main(String[] args) {
    int exit = 0;
    while (exit != -1) {
      System.out.println("Hello user!");
      System.out.println("Enter 1 for login, 2 for registration:");

      scanner = new Scanner(System.in);
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
            mainMenu(user);
            break;
          case MANAGER:
            managerMenu();
            break;

          case ADMIN:
            adminMenu();
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
        mainMenu(user);

      } catch (UserRegistrationException | InvalidKeySpecException e) {
        count++;
        System.out.println(e.getMessage());
      }
    }
  }

  public static void mainMenu(User user) {
    int number = 0;
    while (number != 3) {
      LogService.addLoggedAction("User "+ user.getLogin() +" entered to the main menu ");
      System.out.println("Hello " + user.getLogin() + " !");
      System.out.println("Select an option:");
      System.out.println("1- View a list of available movies.");
      System.out.println("2- View purchased tickets or return tickets.");
      System.out.println("3- Exit");

      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      switch (number) {
        case 1:
          filmMenu(user);
          break;
        case 2:
          returnPurchasedTicketMenu(user);
          break;
        case 3:
          System.out.println("Exit. Come back to Cinema.");
          LogService.addLoggedAction("User exited user main menu");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void filmMenu(User user) {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("User "+ user.getLogin() +" entered to the film menu ");
      System.out.println("Choose a movie ID to view available tickets and -1 to exit. ");
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
      if (number >= films.size() || number < -1) {
        System.out.println("There is no such movie.");
      } else if (number != -1) {
        menuBuyTicket(films.get(number).getId(), user);
      }
      LogService.addLoggedAction("User exited user film menu");
    }
  }

  private static void menuBuyTicket(String filmId, User user) {

    int number = 0;
    while (number != -1) {
      List<Ticket> ticketsFilm = TicketService.getAvailableTicketsByFilm(filmId);
      if (ticketsFilm.isEmpty()) {
        System.err.println("There are no available tickets for this film. ");
        return;
      }
      LogService.addLoggedAction("User "+ user.getLogin() +" entered to the byu ticket menu ");
      System.out.println("Choose a ticket for purchase and -1 to exit. ");
      for (int i = 0; i < ticketsFilm.size(); i++) {
        System.out.println("Ticket " + i + " =" + ticketsFilm.get(i));
      }
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      if (number >= ticketsFilm.size() || number < -1) {
        System.out.println("There is no such ticket.");
      } else if (number != -1) {
        try {
          TicketService.buyTicket(ticketsFilm.get(number).getId(), user.getId());
          LogService.addLoggedAction("The user bought a ticket for the movie "+filmId);
          System.out.println("Ticket purchased.");
          number = -1;
          LogService.addLoggedAction("User exited user menu by ticket");
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public static void returnPurchasedTicketMenu(User user) {
    int number = 0;
    while (number != -1) {
      LogService.addLoggedAction("User "+ user.getLogin() +
          " entered to the return purchased ticket menu ");
      List<Ticket> allTickets = TicketService.getAllTickets(user.getId());
      if (allTickets.isEmpty()) {
        System.err.println("There are no tickets for " + user.getLogin());
        return;
      }
      System.out.println("Enter the number of the ticket to return or -1 to exit.");

      for (int i = 0; i < allTickets.size(); i++) {
        System.out.println("Ticket " + i + " =" + allTickets.get(i));
      }
      try {
        number = Integer.parseInt(scanner.nextLine());
      }catch (NumberFormatException e){
        System.err.println("Wrong line. Not a number");
        continue;
      }
      if (number >= allTickets.size() || number < -1) {
        System.out.println("There is no such ticket.");
      } else if (number != -1) {
        TicketService.returnTicket(allTickets.get(number).getId());
        LogService.addLoggedAction("User "+ user.getLogin() +  " returned purchased ticket ");
        System.out.println("Ticket returned.");
      }
    }
    LogService.addLoggedAction("User exited user return purchased ticket menu");
  }

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
            filmMenu(user);
            break;
          case 2:
            returnPurchasedTicketMenu(user);
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

  //  public static Film selectionFilm() {
//    int number = 0;
//    while (number != -1) {
//      System.out.println("Choose a movie number to change and -1 to exit. ");
//      List<Film> films = null;
//      try {
//        films = FilmService.getAllFilms();
//      } catch (FilmsException e) {
//        System.out.println(e.getMessage());
//        return;
//      }
//      for (int i = 0; i < films.size(); i++) {
//        System.out.println("Film " + i + " =" + films.get(i));
//      }
//      number = scanner.nextInt();
//      if (number == -1) {
//        return;
//      }
//      if (number >= films.size() || number < -1) {
//        System.out.println("There is no such movie.");
//      } else {
//        Film film = films.get(number);
//        return film;
//      }
//    }
//
//  }
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
          managerChangeFilmMenu();
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


