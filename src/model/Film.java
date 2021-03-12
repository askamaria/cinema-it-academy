package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Film {

  private String id;
  private String filmName;
  private LocalDateTime filmTime;

  public Film() {
    this.id = UUID.randomUUID().toString();
  }

  public Film(String filmName, LocalDateTime filmTime) {
    this.id = UUID.randomUUID().toString();
    this.filmName = filmName;
    this.filmTime = filmTime;
  }

  public Film(String id, String filmName, LocalDateTime filmTime) {
    this.id = id;
    this.filmName = filmName;
    this.filmTime = filmTime;
  }

  public Film(String id, String filmName, String filmTime) {
    this.id = id;
    this.filmName = filmName;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm");
    this.filmTime = LocalDateTime.parse(filmTime, formatter);
  }
  public Film(String filmName, String filmTime){
    this.id = UUID.randomUUID().toString();
    this.filmName = filmName;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm");
    this.filmTime = LocalDateTime.parse(filmTime, formatter);
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFilmName() {
    return filmName;
  }

  public void setFilmName(String filmName) {
    this.filmName = filmName;
  }

  public LocalDateTime getFilmTime() {
    return filmTime;
  }

  public void setFilmTime(LocalDateTime filmTime) {
    this.filmTime = filmTime;
  }
  public void setFilmTime(String filmTime){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy:HH.mm");
    this.filmTime = LocalDateTime.parse(filmTime, formatter);
  }

  @Override
  public String toString() {
    return id + "," + filmName + "," + filmTime;
  }
}
