package algorithms;

import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class algoritms {
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
	public static Mat umbralizacion(Mat imagen){
		Imgproc.threshold(imagen,imagen,0,255,Imgproc.THRESH_OTSU);
		return imagen;
	}
	public static Mat imagenVarianza(Mat imagen, int cvType){
		MatOfDouble mean = new MatOfDouble();
		MatOfDouble desviacion = new MatOfDouble();
		int filas=imagen.rows();
		int columnas=imagen.cols();
		Mat resultado= new Mat(filas,columnas,cvType);
		Rect ventana;
		Mat ImagenCortada;
		int ancho=7;//ancho predeterminado se puede cambiar para optimizar
		int alto=8;//alto predeterminado se puede cambiar para optimizar
		int anchoAux,altoAux;
		for (int fila=0;fila<filas;fila++){
			for(int columna=0;columna<columnas;columna++){
				anchoAux=ancho;
				altoAux=alto;
				if(ancho+columna>columnas){
					anchoAux=columnas-columna;
				}
				if(alto+fila>filas){
					altoAux=filas-fila;
				}
				ventana= new Rect(columna,fila,anchoAux,altoAux);
				ImagenCortada = new Mat(imagen,ventana);
				Core.meanStdDev(ImagenCortada, mean, desviacion);
				
				double[] desviacionCanales=desviacion.get(0,0);
				int valor= (int)(Math.pow(desviacionCanales[0],2))%255;
				double[] canalesResultado = new double[]{valor,0,0};
				resultado.put(fila,columna,canalesResultado);
			}			
		}
		return resultado;
	}
	public static Mat normalizar(Mat imagen,int cvType){
		int filas=imagen.rows();
		int columnas=imagen.cols();
		Mat resultado= new Mat(filas,columnas,cvType);
		for (int fila=0;fila<filas;fila++){
			for(int columna=0;columna<columnas;columna++){
				double[] canalesHsv=imagen.get(fila, columna);
				int valor= (int)((canalesHsv[0]/360)*255);
				double[] nuevosCanalesHsv = new double[]{valor,0,0};
				resultado.put(fila,columna,nuevosCanalesHsv);
			}			
		}
		return resultado;
	}
	

	public static Mat obtenerHue(Mat imagen){
		ArrayList<Mat> canalesHsv = new ArrayList<Mat>();
        Core.split(imagen, canalesHsv);
		return canalesHsv.get(0);//obtener el canal 0 del hue		
	}
	public static Boolean equals(Mat mat1,Mat mat2){		
		if(mat1.empty() && mat2.empty()){
			return true;
		}
		if(mat1.cols()!=mat2.cols() || mat1.rows()!=mat2.rows() || mat1.dims()!=mat2.dims()){
			System.out.println("here");
			return false;
		}
		for (int fila=0;fila<mat1.rows();fila++){
			for(int columna=0;columna<mat1.cols();columna++){
				double[] canales1=mat1.get(fila, columna);
				double[] canales2=mat2.get(fila, columna);
				for(int element=0;element<mat1.dims();element++){
					if(canales1[element]!=canales2[element]){
						return false;
					}
				}
				System.out.println("---------------------------");
			}			
		}
		return true;
	}
}
