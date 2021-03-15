package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import model.Film;

public class LogService {

  public static void addLoggedAction(String value) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter
        ("resources/Log.txt",true))) {

        bufferedWriter.write(value+" " + LocalDateTime.now()+"\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  }

