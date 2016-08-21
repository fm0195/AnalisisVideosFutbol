package Testing;
import org.junit.Test;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import algorithms.algoritms;

import java.io.IOException;


public class Prueba1 {

   
 
   @Test
   public void prueba1() throws IOException {
	   Mat mat = algoritms.cargarImagen("proof.jpg");
	   int alto=mat.rows();
	   int ancho=mat.cols();
	   boolean res=false;
       VideoCapture camera = new VideoCapture("video.mp4");
       while(camera.read(mat)){
    	   if(!(mat.rows()==alto && mat.cols()==ancho)){
    		   res=false;
    	   }else{
    		   res=true;
    	   }
       }
       assert(res);
   }
}