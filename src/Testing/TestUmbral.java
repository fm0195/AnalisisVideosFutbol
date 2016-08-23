package Testing;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import algorithms.algoritms;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class TestUmbral {

   
 
   @Test
   public void testUmbral() throws IOException {
	   System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	   
	   File input = new File("proof.jpg");
	   
	   Mat mat = algoritms.cargarImagen("umbral.jpg");
	   
       Mat frame = new Mat();
       VideoCapture camera = new VideoCapture("video.mp4");
       camera.read(frame);
       Mat hsv = algoritms.convertirHSV(frame);
       Mat h = algoritms.obtenerHue(hsv);
       Mat normalizada=algoritms.normalizar(h,h.type());
       Mat varianza =algoritms.imagenVarianza(normalizada,normalizada.type());
       Mat umbral =algoritms.umbralizacion(varianza);
       Mat resultadoUmbral=algoritms.rellenarHuecos(umbral);
       algoritms.guardarImagen(resultadoUmbral, "temp.jpg");
       resultadoUmbral=algoritms.cargarImagen("temp.jpg");
       assert(algoritms.equals(mat, umbral));
   }
}