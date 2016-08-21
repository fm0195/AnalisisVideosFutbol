import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import algorithms.algoritms;

public class TestMain {
	public static void main(String[] args) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat  hsv, mascara, resultado;
        Mat mat = new Mat();
        VideoCapture camera = new VideoCapture("video.mp4");
        camera.read(mat);
        algoritms.guardarImagen( mat,"proof.jpg");
        
        Mat mat1 = algoritms.convertirHSV(mat);
        
        ArrayList<Mat> hsv_channel = new ArrayList<Mat>();
        Core.split(mat1, hsv_channel);	         
        algoritms.guardarImagen(hsv_channel.get(0),"h.jpg");
        
        Mat normalizada=algoritms.normalizar(hsv_channel.get(0),hsv_channel.get(0).type());
        algoritms.guardarImagen( normalizada,"normalizada.jpg");
        
        Mat varianza =algoritms.imagenVarianza(normalizada,normalizada.type());
        algoritms.guardarImagen( varianza,"varianza.jpg");
        
        Mat umbral =algoritms.umbralizacion(varianza);
        algoritms.guardarImagen( umbral,"UmbralconHuecos.jpg");
        
        Mat resultadoUmbral=algoritms.rellenarHuecos(umbral);
        algoritms.guardarImagen( resultadoUmbral,"umbral.jpg");
        
        hsv = algoritms.convertirHSV(mat);
        mascara = algoritms.obtenerMascara(hsv);
        resultado = algoritms.rellenarRuido(mascara);
        resultado = algoritms.rellenarHuecos(resultado);
        algoritms.guardarImagen(mascara, "mascara.jpg");
        algoritms.guardarImagen(resultado, "resultado.jpg");
	}
	
}
