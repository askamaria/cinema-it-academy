package model;

import java.util.UUID;

public class Ticket {

  private String id;
  private boolean isAvailable;
  private String userId;
  private int placeNumber;
  private String filmUuid;

  public Ticket(boolean isAvailable, String userId , int placeNumber) {
    this.id=UUID.randomUUID().toString();
  }

  public Ticket(String id, boolean isAvailable, String userId, int placeNumber,String filmUuid) {
    this.id = id;
    this.isAvailable = isAvailable;
    this.userId = userId;
    this.placeNumber = placeNumber;
    this.filmUuid=filmUuid;
  }
  public Ticket(String id, String isAvailable, String userId, String placeNumber,String filmUuid) {
    this.id = id;
    this.isAvailable = Boolean.parseBoolean(isAvailable);
    this.userId = userId;
    this.placeNumber = Integer.parseInt(placeNumber);
    this.filmUuid=filmUuid;
  }
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getPlaceNumber() {
    return placeNumber;
  }

  public void setPlaceNumber(int placeNumber) {
    this.placeNumber = placeNumber;
  }

  public String getFilmUuid() {
    return filmUuid;
  }

  public void setFilmUuid(String filmUuid) {
    this.filmUuid = filmUuid;
  }

  @Override
  public String toString() {
    return id +","+ isAvailable +","+ userId + ","+placeNumber +","+ filmUuid;
  }
}

