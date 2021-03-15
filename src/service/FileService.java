package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Film;
import model.Ticket;
import model.User;
import model.UserType;

public class FileService {

  public static List<User> getAllUsersFromFile() {
    List<User> users = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("resources/Users.txt"))) {
      String s;
      while ((s = br.readLine()) != null) {
        String[] stringParts = s.split(",");
        String id = stringParts[0];
        String login = stringParts[1];
        String password = stringParts[2];
        UserType userType = UserType.valueOf(stringParts[3]);
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setUserType(userType);
        users.add(user);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return users;
  }

  public static User addUserToFile(User user) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Users.txt", true))) {
      String s = user.toString();
      bufferedWriter.write("\n" + s);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static List<Film> getAllFilmsFromFile() {
    List<Film> films = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("resources/Films.txt"))) {
      String s;
      while ((s = br.readLine()) != null) {
        String[] stringParts = s.split(",");
        String id = stringParts[0];
        String filmName = stringParts[1];
        String filmTime = stringParts[2];
        Film film = new Film(id, filmName, filmTime);
        films.add(film);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return films;
  }


  public static List<Ticket> getAllTicketsFromFile() {
    List<Ticket> tickets = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("resources/Tickets.txt"))) {
      String s;
      while ((s = br.readLine()) != null) {
        String[] stringParts = s.split(",");
        String id = stringParts[0];
        String isAvailable = stringParts[1];
        String userId = stringParts[2];
        String placeNumber = stringParts[3];
        String filmUuid = stringParts[4];
        Ticket ticket = new Ticket(id, isAvailable, userId, placeNumber, filmUuid);
        tickets.add(ticket);
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return tickets;
  }

  public static void updateTickets(List<Ticket> tickets) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Tickets.txt"))) {
      for (Ticket ticket : tickets) {
        String s = ticket.toString();
        bufferedWriter.write(s + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void addFilm(Film film) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Films.txt", true))) {
      String s = film.toString();
      bufferedWriter.write("\n" + s);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void updateFilms(List<Film> films) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Films.txt"))) {
      for (Film film : films) {
        String s = film.toString();
        bufferedWriter.write(s + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void updateUsers(List<User> users) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Users.txt"))) {
      for (User user : users) {
        String s = user.toString();
        bufferedWriter.write(s + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

