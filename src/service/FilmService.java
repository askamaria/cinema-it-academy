package service;

import exceptions.FilmsException;
import java.util.List;
import model.Film;

public class FilmService {

  public static List<Film> getAllFilms() throws FilmsException {
    List<Film> films = FileService.getAllFilmsFromFile();
    if(films.isEmpty()){
      throw new FilmsException("No movie list.");
    }
    return films;
  }
  public static void createFilms(Film film){
  }
  public static void updateFilm(Film film){

  }
  public static void deleteFilm(String filmId){

  }
}
