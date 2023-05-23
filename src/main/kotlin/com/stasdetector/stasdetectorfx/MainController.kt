package com.stasdetector.stasdetectorfx

import com.stasdetector.stasdetectorfx.camera.CameraService
import javafx.application.Platform
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.image.ImageView
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MainController {

    lateinit var imagePath: TextField
    lateinit var cameraView: ImageView
    lateinit var cameraButton: Button
    var scheduledFuture: ScheduledFuture<*>? = null

    private var cameraStarted: Boolean = false
    val scheduledExecutor = Executors.newSingleThreadScheduledExecutor()

    fun onStartCameraClick() {
        println(System.getenv())
        if (cameraStarted) {
            Platform.runLater { cameraButton.disableProperty().value = true }
            cameraStarted = false
            println("Stopping camera")
            if (scheduledFuture?.isCancelled == false) {
                scheduledFuture?.cancel(true)
            }
            scheduledFuture = null
            cameraView.image = null
            CameraService.stopCamera()
            println("Camera stopped")
            cameraButton.text = "Start camera"
            Platform.runLater { cameraButton.disableProperty().value = false }
            return
        }

//        Platform.runLater {
            cameraButton.disableProperty().value = true
            cameraStarted = true
            println("Starting camera")
            CameraService.startCamera(0)
            println("Camera started")
//        }

        scheduledFuture = scheduledExecutor.scheduleAtFixedRate(
            {
                Platform.runLater {
//                    val start = System.currentTimeMillis()
                    cameraView.image = CameraService.getCameraFrame()
                    cameraView.fitHeight = 680.toDouble()
                    cameraView.isPreserveRatio = true
//                    println("Took ${System.currentTimeMillis() - start}ms")
                }
            },
            0,
            200,
            TimeUnit.MILLISECONDS
        )
//        Platform.runLater {
            cameraButton.text = "Stop camera"
            cameraButton.disableProperty().value = false
//        }
    }

    fun shutdown() {
        if (scheduledFuture?.isCancelled == false) {
            scheduledFuture?.cancel(true)
        }
        scheduledExecutor.shutdownNow()
    }
}
