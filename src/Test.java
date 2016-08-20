import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Test {
	public static void main(String[] args) throws IOException{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat bgr, hsv, mascara, resultado;
        bgr = cargarImagen("C:\\Users\\fm010\\workspace\\VideosFutbol\\image.jpg");
        hsv = convertirHSV(bgr);
        mascara = obtenerMascara(hsv);
        resultado = rellenarRuido(mascara);
        resultado = rellenarHuecos(resultado);
        guardarImagen(mascara, "C:\\Users\\fm010\\workspace\\VideosFutbol\\mascara.jpg");
        guardarImagen(resultado, "C:\\Users\\fm010\\workspace\\VideosFutbol\\resultado.jpg");
	}
	
	public static Mat cargarImagen(String path) throws IOException{
		Mat res = Imgcodecs.imread(path);
		if(res.empty())
			throw new IOException("Error cargando la imagen");
		return res;
	}
	
	public static void guardarImagen(Mat img, String path) throws IOException{
		Imgcodecs.imwrite(path, img);
	}
	
	public static Mat convertirHSV(Mat img_bgr){
		Mat res = new Mat(img_bgr.rows(), img_bgr.cols(), img_bgr.type());
		Imgproc.cvtColor(img_bgr, res, Imgproc.COLOR_BGR2HSV);
		return res;
	}
	
	
	
	public static Mat obtenerMascara(Mat img_hsv){
		Mat res = new Mat(img_hsv.rows(), img_hsv.cols(), img_hsv.type());
		Scalar limiteInferior = new Scalar(35, 50, 50); //limite superior de mascara
		Scalar limiteSuperior = new Scalar(70, 255, 255);;//limite inferior de mascara
		Core.inRange(img_hsv, limiteInferior, limiteSuperior, res);//aplicar el filtro
		return res;
	}
	
	public static Mat rellenarContornos(Mat img_hsv){
		Mat res,hierarchy,des;
		res = new Mat(img_hsv.rows(), img_hsv.cols(), img_hsv.type());
		des = new Mat(img_hsv.rows(), img_hsv.cols(), img_hsv.type());
		hierarchy = new Mat();
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Core.bitwise_not(img_hsv, res);
		Imgproc.findContours(res, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
		for(MatOfPoint cnt : contours){
			ArrayList<MatOfPoint> list = new ArrayList<>();
			list.add(cnt);
			Imgproc.drawContours(res, list, 0, new Scalar(255),-1);
		}
		Core.bitwise_not(res, des);
		return des;
	}
	
	public static Mat rellenarHuecos(Mat img_hsv){
		Core.bitwise_not(img_hsv, img_hsv);
		Mat res = rellenarContornos(img_hsv);
		Core.bitwise_not(res, res);
		return res;
	}
	
	public static Mat rellenarRuido(Mat img_hsv){
		return rellenarContornos(img_hsv);
	}
}
