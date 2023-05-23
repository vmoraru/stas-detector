module com.stasdetector.stasdetectorfx {
  requires javafx.controls;
  requires javafx.fxml;
  requires kotlin.stdlib;

  requires org.controlsfx.controls;
  requires net.synedra.validatorfx;
  requires org.kordamp.bootstrapfx.core;
  requires opencv;

  opens com.stasdetector.stasdetectorfx to javafx.fxml;
  exports com.stasdetector.stasdetectorfx;
}
