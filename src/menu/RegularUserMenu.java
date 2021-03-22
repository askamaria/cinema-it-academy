package menu;

import exceptions.FilmsException;
import java.util.List;
import java.util.Scanner;
import model.Film;
import model.Ticket;
import model.User;
import service.FilmService;
import service.LogService;
import service.TicketService;

public class RegularUserMenu {
  public static Scanner scanner=new Scanner(System.in);
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
}
