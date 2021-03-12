package view;

import exceptions.FilmsException;
import exceptions.UserRegistrationException;
import java.util.List;
import java.util.Scanner;
import model.Film;
import model.Ticket;
import model.User;
import service.FilmService;
import service.LogService;
import service.TicketService;
import service.UserService;

public class Main {


  public static Scanner scanner;

  public static void main(String[] args) {
    // System.out.println(FileService.getAllTicketsFromFile());
    System.out.println(TicketService.getAvailableTicketsByFilm("tcd00"));
//    User user = new User("oooooo", "999999");
//    FileService.addUser(user);
//    filmMenu();
    int exit = 0;
    while (exit != -1) {
      System.out.println("Hello user!");
      System.out.println("Enter 1 for login, 2 for registration:");
      //System.out.println(users);

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
      System.out.println("Hello. It's login menu.");
      System.out.println("Please enter login");
      String login = scanner.nextLine();
      System.out.println("Please enter password");
      String pass = scanner.nextLine();
      User user = UserService.loginUser(login, pass);
      if (user == null) {
        System.out.println("Login or password entered incorrectly. Please, try again.");
        count++;
        if (count == 3) {
          System.out.println("You may not be registered. Register, please.");
        }
      } else {
        System.out.println("User " + login + " logged in");
        count = 3;
        // check is admin type then to AdminConsole static method
        LogService.addLoggedAction("User is logged in");
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
      System.out.println("Please enter login");
      String login = scanner.nextLine();
      System.out.println("Please enter password");
      String pass = scanner.nextLine();

      try {
        User user = UserService.registerUser(login, pass);

        // LogService.addLoggedAction("User is logged in");
        count = 3;
        System.out.println("User " + user.getLogin() + " registered.");
        mainMenu(user);

      } catch (UserRegistrationException e) {
        count++;
        System.out.println(e.getMessage());
      }
    }
  }

  public static void mainMenu(User user) {
    int number = 0;
    while (number != 3) {
      System.out.println("Hello " + user.getLogin() + " !");
      System.out.println("Select an option:");
      System.out.println("1- View a list of available movies");
      System.out.println("2- View purchased tickets or return tickets");
      System.out.println("3- Exit");

      number = scanner.nextInt();
      switch (number) {
        case 1:
          filmMenu(user);
          break;
        case 2:
          returnPurchasedTicketMenu(user);
          break;
        case 3:
          System.out.println("Exit. Come back to Cinema.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void filmMenu(User user) {
    int number = 0;
    while (number != -1) {
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
      number = scanner.nextInt();
      if (number >= films.size() || number < -1) {
        System.out.println("There is no such movie.");
      } else if (number != -1) {
        menuBuyTicket(films.get(number).getId(), user);
      }
    }
  }

  private static void menuBuyTicket(String filmId, User user) {

    int number = 0;
    while (number != -1) {
      List<Ticket> ticketsFilm = TicketService.getAvailableTicketsByFilm(filmId);
      if (ticketsFilm.isEmpty()) {
        System.err.println("There are no available tickets for this film ");
        return;
      }
      System.out.println("Choose a ticket for purchase and -1 to exit. ");
      for (int i = 0; i < ticketsFilm.size(); i++) {
        System.out.println("Ticket " + i + " =" + ticketsFilm.get(i));
      }
      number = scanner.nextInt();
      if (number >= ticketsFilm.size() || number < -1) {
        System.out.println("There is no such ticket.");
      } else if (number != -1) {
        try {
          TicketService.buyTicket(ticketsFilm.get(number).getId(), user.getId());
          System.out.println("Ticket purchased.");
          number = -1;
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
  }

  public static void returnPurchasedTicketMenu(User user) {
    int number = 0;
    while (number != -1) {
      List<Ticket> allTickets = TicketService.getAllTickets(user.getId());
      if (allTickets.isEmpty()) {
        System.err.println("There are no tickets for " + user.getLogin());
        return;
      }
      System.out.println("Enter the number of the ticket to return or -1 to exit");

      for (int i = 0; i < allTickets.size(); i++) {
        System.out.println("Ticket " + i + " =" + allTickets.get(i));
      }
      number = scanner.nextInt();
      if (number >= allTickets.size() || number < -1) {
        System.out.println("There is no such ticket.");
      } else if (number != -1) {
        TicketService.returnTicket(allTickets.get(number).getId());
        System.out.println("Ticket returned.");
      }
    }
  }

  public static void managerMenu() {
    int number = 0;
    while (number != -1) {
      System.out.println("Hello manager!");
      System.out.println("Select an option:");
      System.out.println("1- Edit the movie.");
      System.out.println("2- Buy / return a user ticket.");
      System.out.println("-1 - Exit");

      number = scanner.nextInt();
      switch (number) {
        case 1:
          managerEditFilmMenu();
          break;
        case 2:
          managerTicketMenu();
          break;
        case -1:
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void managerEditFilmMenu() {
    System.out.println("Hello. It's manager edit film menu");
    int number = 0;
    while (number != -1) {
      System.out.println("Select an option:");
      System.out.println("1- Change the movie");
      System.out.println("2- Add the movie.");
      System.out.println("-1 - Exit");
      number = scanner.nextInt();
      switch (number) {
        case 1:
          managerChangeFilmMenu();
          break;
        case 2:
          managerAddFilmMenu();
          break;
        case -1:
          System.out.println("Exit.");
          break;
        default:
          System.out.println("Typo!");
      }
    }
  }

  public static void managerChangeFilmMenu() {
    System.out.println("Hello. It's manager change film menu.");
    int number = 0;
    String str = null;
    while (number != -1) {

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
      number = scanner.nextInt();
      if (number >= films.size() || number < -1) {
        System.out.println("There is no such movie.");
      } else {
        Film film = films.get(number);
        System.out.println("Old movie name: " + film.getFilmName());
        System.out.println("Enter a new name or /'Enter/' to skip");
        scanner.nextLine();
        str = scanner.nextLine();
        if (!str.isEmpty()) {
          film.setFilmName(str);
        }
        System.out.println("Old film time: " + film.getFilmTime());
        System.out.println("Enter a new film time in the format dd.MM.yyyy:HH.mm or /'Enter/' to skip");
        str = scanner.nextLine();
        if (!str.isEmpty()) {
          film.setFilmTime(str);
          FilmService.updateFilm(film);
        }
      }
    }
  }

  public static void managerAddFilmMenu() {
    int count = 0;
    while (count != -1) {
      System.out.println("Hello. It's manager add film menu.");
      System.out.println("Please enter film name or -1 to exit.");
      String filmName = scanner.nextLine();
      System.out.println("Please enter film date or -1 to exit.");
      String filmTime = scanner.nextLine();
      Film film = new Film(filmName, filmTime);
      FilmService.createFilms(film);
    }
  }

  public static void managerTicketMenu() {
    System.out.println("managerTicketMenu");
  }

  public static void adminMenu() {
    System.out.println(" Hello admin!");
  }

}


