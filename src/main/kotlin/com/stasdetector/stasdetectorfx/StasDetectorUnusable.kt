package com.stasdetector.stasdetectorfx

import com.stasdetector.stasdetectorfx.camera.CameraService
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class StasDetectorUnusable : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(StasDetectorUnusable::class.java.getResource("stasdetector-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 800.0)
        stage.title = "Stas detector 9000"
        stage.scene = scene
        val mainController: MainController = fxmlLoader.getController();
        stage.setOnHidden { e -> mainController.shutdown() }
        stage.show()
    }

    override fun stop() {
        super.stop()
        CameraService.stopCamera()
    }
}

fun main() {
    Application.launch(StasDetectorUnusable::class.java)
}
