package service;

import exceptions.FilmsException;
import java.util.List;
import model.Film;

public class FilmService {

  public static List<Film> getAllFilms() throws FilmsException {
    List<Film> films = FileService.getAllFilmsFromFile();
    if (films.isEmpty()) {
      throw new FilmsException("No movie list.");
    }
    return films;
  }

  public static void createFilms(Film film) {
    FileService.addFilm(film);
  }

  public static void updateFilm(Film film) {
    List<Film> films = FileService.getAllFilmsFromFile();
    for (int i = 0; i < films.size(); i++) {
      if (films.get(i).getId().equals(film.getId())) {
        films.set(i, film);
        FileService.updateFilms(films);
        return;
      }
    }
  }

  public static void deleteFilm(String filmId) {
    List<Film> films = FileService.getAllFilmsFromFile();
    for (int i = 0; i < films.size(); i++) {
      Film film=films.get(i);
      if (films.get(i).getId().equals(filmId)){
        films.remove(i);
        System.out.println("Film " + film.getFilmName() + " deleted.");
        LogService.addLoggedAction("Film " + film.getFilmName() + " deleted.");
        FileService.updateFilms(films);
        return;
      }
    }
  }
  }

