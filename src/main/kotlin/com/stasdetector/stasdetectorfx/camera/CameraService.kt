package com.stasdetector.stasdetectorfx.camera

import com.stasdetector.stasdetectorfx.StasDetectorUnusable
import javafx.scene.image.Image
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import org.opencv.objdetect.Objdetect
import org.opencv.videoio.VideoCapture
import java.io.ByteArrayInputStream
import kotlin.io.path.Path
import kotlin.io.path.pathString


/**
 *
 * @since 1/30/2023
 * @author vmoraru
 */
class CameraService {

    companion object {

        private var camera: VideoCapture
        private var absoluteFaceSize = 0
        private val faceCascade: CascadeClassifier

        init {
            System.load("${System.getProperty("user.dir")}\\opencv_java470.dll")
            camera = VideoCapture()
            faceCascade = CascadeClassifier()
            val pathStr = StasDetectorUnusable::class.java.getResource("lbpcascade_frontalface_improved.xml")
                .path.substring(1)
            var path = Path(pathStr)
            faceCascade.load(path.pathString)
        }

        fun startCamera(cameraIndex: Int) {
            camera.open(cameraIndex)
        }

        fun stopCamera() {
            camera.release()
        }

        fun getCameraFrame(): Image? {
            if (!camera.isOpened) {
                return null
            }

            val mat = Mat()
            camera.read(mat)
            Thread.sleep(30)
            camera.read(mat)

            detectAndDisplay(mat)
            return convertToImage(mat)
        }

        private fun convertToImage(mat: Mat): Image {
            val matOfByte = MatOfByte()
            Imgcodecs.imencode(".png", mat, matOfByte)
            return Image(ByteArrayInputStream(matOfByte.toArray()))
        }

        private fun detectAndDisplay(frame: Mat) {
            // init
            val faces = MatOfRect()
            val grayFrame = Mat()

            // convert the frame in gray scale
            Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY)
            // equalize the frame histogram to improve the result
            Imgproc.equalizeHist(grayFrame, grayFrame)

            // compute minimum face size (20% of the frame height)
            if (absoluteFaceSize == 0) {
                val height = grayFrame.rows()
                if (Math.round(height * 0.2f) > 0) {
                    absoluteFaceSize = Math.round(height * 0.2f)
                }
            }

            // detect faces
            faceCascade.detectMultiScale(
                grayFrame, faces, 1.1, 2, 0 or Objdetect.CASCADE_SCALE_IMAGE, Size(
                    absoluteFaceSize.toDouble(),
                    absoluteFaceSize.toDouble()
                ), Size()
            )

            // each rectangle in faces is a face
            val facesArray: Array<Rect> = faces.toArray()
            for (i in facesArray.indices) Imgproc.rectangle(
                frame,
                facesArray[i].tl(),
                facesArray[i].br(),
                Scalar(0.0, 255.0, 0.0, 255.0),
                3
            )
        }
    }
}
