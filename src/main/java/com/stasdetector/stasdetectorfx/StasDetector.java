package com.stasdetector.stasdetectorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StasDetector extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(StasDetector.class.getResource("stasdetector-view.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 800, 800);
    System.out.println("Resource in StasDetector = " + StasDetector.class.getResource("lbpcascade_frontalface_improved.xml"));
    stage.resizableProperty().setValue(false);
    stage.setTitle("Stas detector 3000");
    stage.setScene(scene);
    stage.show();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }
}
