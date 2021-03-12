package model;

import java.util.UUID;

public class User {

  private String id;
  private String login;
  private String password;
  private UserType userType;

  public User() {
    this.id = UUID.randomUUID().toString();

  }

  public User(String id, String login, String password,UserType userType) {
    this.id = id;
    this.login = login;
    this.password = password;
    this.userType = userType;
  }

  public User(String login, String password) {
    this.id = UUID.randomUUID().toString();
    this.login = login;
    this.password = password;
    this.userType=UserType.REGULAR;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
  }

  @Override
  public String toString() {
    return id+"," + login+ "," + password+"," + userType;

  }
}
