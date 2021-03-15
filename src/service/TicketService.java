package service;

import java.util.ArrayList;
import java.util.List;
import model.Ticket;

public class TicketService {

  public static List<Ticket> getAllTickets() {
    List<Ticket> tickets = FileService.getAllTicketsFromFile();
    return tickets;
  }

  public static List<Ticket> getAllTickets(String userId) {
    List<Ticket> tickets = getAllTickets();
    List<Ticket> userTickets = new ArrayList<>();
    for (Ticket ticket : tickets) {
      if (ticket.getUserId().equals(userId)) {
        userTickets.add(ticket);
      }
    }
    return userTickets;
  }

  public static List<Ticket> getAvailableTicketsByFilm(String filmId) {
    List<Ticket> tickets = FileService.getAllTicketsFromFile();
    List<Ticket> availableTickets = new ArrayList<>();
    for (Ticket ticket : tickets) {
      if (ticket.isAvailable() && ticket.getFilmUuid().equals(filmId)) {
        availableTickets.add(ticket);
      }
    }
    return availableTickets;
  }

  public static Ticket buyTicket(String ticketId, String userId) {
    List<Ticket> tickets = FileService.getAllTicketsFromFile();
    for (Ticket ticket : tickets) {
      if (ticket.getId().equals(ticketId)) {
        ticket.setUserId(userId);
        ticket.setAvailable(false);
        LogService.addLoggedAction("User " +userId+" chose "+ ticket.getPlaceNumber() );
        FileService.updateTickets(tickets);
        return ticket;
      }
    }
    return null;
  }

  public static void returnTicket(String ticketId) {
    List<Ticket> tickets = getAllTickets();
    for (Ticket ticket : tickets) {
      if (ticket.getId().equals(ticketId)) {
        ticket.setUserId(null);
        ticket.setAvailable(true);
        LogService.addLoggedAction("Ticket " +ticket.getFilmUuid()+ticket.getUserId()+ticket.getPlaceNumber()
            + " was returned.");
        FileService.updateTickets(tickets);
      }
    }
  }

}
