package Testing;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import algorithms.algoritms;
import java.io.IOException;


public class TestVarianza {

   
 
   @Test
   public void testVarianza() throws IOException {
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	   
	   Mat mat = algoritms.cargarImagen("varianza.jpg");
	   
       Mat frame = new Mat();
       VideoCapture camera = new VideoCapture("video.mp4");
       camera.read(frame);
       Mat hsv = algoritms.convertirHSV(frame);
       Mat h = algoritms.obtenerHue(hsv);
       Mat normalizada=algoritms.normalizar(h,h.type());
       Mat varianza =algoritms.imagenVarianza(normalizada,normalizada.type());
       algoritms.guardarImagen(varianza, "temp.jpg");
       varianza=algoritms.cargarImagen("temp.jpg");
       assert(algoritms.equals(mat, varianza));
   }
}