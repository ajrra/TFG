package com.example.tfg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CV_Paper {


    /**
     *  Object that encapsulates the contour and 4 points that makes the larger
     *  rectangle on the image
     */





     public static Quadrilateral findEdges(Bitmap map){


        Mat mRgba = new Mat(map.getHeight(),map.getWidth(), CvType.CV_8UC4);
        Size s_mRgba = mRgba.size();
        Bitmap tmp = map.copy(Bitmap.Config.ARGB_8888,true);
        Utils.bitmapToMat(tmp, mRgba);
         double ratio = mRgba.size().height / 500;
         int height = Double.valueOf(mRgba.size().height / ratio).intValue();
         int width = Double.valueOf(mRgba.size().width / ratio).intValue();
         Size size = new Size(width,height);
         Mat mResize = mRgba.clone();
         Imgproc.resize(mRgba,mResize,size);
        Quadrilateral puntos = CV_Paper.findDocument(mResize,size);

        if(puntos !=null) {
            ArrayList<MatOfPoint> contour = new ArrayList<MatOfPoint>();
            contour.add(puntos.contour);
            Imgproc.drawContours(mResize, contour, -1,  new Scalar(0, 255, 0, 255),50);
        }

        Imgproc.resize(mResize,mRgba,s_mRgba);
        Utils.matToBitmap(mRgba,map);
        puntos.img=map;
        return puntos;


    }


    public static Quadrilateral findDocument( Mat inputRgba,Size size ) {

        ArrayList<MatOfPoint> contours = findContours(inputRgba,size);
        Quadrilateral quad = getQuadrilateral(contours,size);

        return quad;
    }

    public static Mat preprocess(Mat src){
        Size size = src.size();
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);

        //APLICAMOS UN FILTRO QUE MANTENGA BORDES
        Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGBA2BGR);
        Mat dst = grayImage.clone();
        Imgproc.bilateralFilter(grayImage, dst, 9, 75, 75, Core.BORDER_DEFAULT);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2RGBA);
        //PASAMOS A GRIS PARA TRABAJAR MEJOR
        Imgproc.cvtColor(dst, grayImage, Imgproc.COLOR_RGBA2GRAY, 4);

        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,115,4);

        //APLICAMOS UNA DIFUMINACION PARA MEJORAR
        Imgproc.medianBlur(grayImage, grayImage, 11);
        Imgproc.dilate(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));

        //ALGORITMO CANNY DE DETECCION DE BORDES
        Imgproc.Canny(grayImage, cannedImage, 200, 250);

        return cannedImage;
    }

   public static Quadrilateral houghQuad(Mat src){
         Quadrilateral quad = null;

         return quad;

   }



    public static Mat perspectiveAdjust(Mat src, Quadrilateral quad){
         Mat pts1,pts2,M;
         ArrayList<Point> pt1 = new ArrayList<>();
        ArrayList<Point> pt2 = new ArrayList<>();
         for (Point a : quad.points){
             pt1.add(a);
         }
         pt2.add(new Point(src.height(),0));
         pt2.add(new Point(src.height(),src.width()));
         pt2.add(new Point(0,src.width()));
         pt2.add(new Point(0,0));

         pts1=Converters.vector_Point2d_to_Mat(pt1);
         pts2=Converters.vector_Point2d_to_Mat(pt2);
         M=Imgproc.getPerspectiveTransform(pts1,pts2);
         Imgproc.warpPerspective(src,src,M,src.size());
        return src;
    }


   public static Mat houghFind(Mat dst,Mat src){
         Quadrilateral tmp = null;
         Mat cdst = dst.clone();
         Mat lines = new Mat();
       Imgproc.HoughLines(src, lines, 1, Math.PI/180, 150);// runs the actual detection
       // Draw the lines
       for (int x = 0; x < lines.rows(); x++) {
           double rho = lines.get(x, 0)[0],
                   theta = lines.get(x, 0)[1];
           double a = Math.cos(theta), b = Math.sin(theta);
           double x0 = a*rho, y0 = b*rho;
           Point pt1 = new Point(Math.round(x0 + 1000*(-b)), Math.round(y0 + 1000*(a)));
           Point pt2 = new Point(Math.round(x0 - 1000*(-b)), Math.round(y0 - 1000*(a)));
           Imgproc.line(cdst, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
       }


       return cdst;
   }



    private static ArrayList<MatOfPoint> findContours(Mat resizedImage, Size size) {

        Bitmap a =Bitmap.createBitmap((int)size.width, (int)size.height, Bitmap.Config.ARGB_8888);;

        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);

      Utils.matToBitmap(resizedImage,a);
        //APLICAMOS UN FILTRO QUE MANTENGA BORDES
        Imgproc.cvtColor(resizedImage, resizedImage, Imgproc.COLOR_RGBA2BGR);
        Mat dst = resizedImage.clone();
        Imgproc.bilateralFilter(resizedImage, dst, 9, 75, 75, Core.BORDER_DEFAULT);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2RGBA);
        //PASAMOS A GRIS PARA TRABAJAR MEJOR
        Imgproc.cvtColor(dst, grayImage, Imgproc.COLOR_RGBA2GRAY, 1);
        Utils.matToBitmap(grayImage,a);
        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,115,4);
        Utils.matToBitmap(grayImage,a);
        //APLICAMOS UNA DIFUMINACION PARA MEJORAR
        Imgproc.medianBlur(grayImage, grayImage, 11);
        Utils.matToBitmap(grayImage,a);
        //ALGORITMO CANNY DE DETECCION DE BORDES
        Imgproc.Canny(grayImage, cannedImage, 200, 250);
      //  Mat h_lines = new Mat(size, CvType.CV_8UC1);
      //  Imgproc.HoughLines(cannedImage,h_lines,1, Math.PI/180, 250);


      //  Utils.matToBitmap(cannedImage,a);
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(cannedImage, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(contours, new Comparator<MatOfPoint>() {

            @Override
            public int compare(MatOfPoint lhs, MatOfPoint rhs) {
                return Double.valueOf(Imgproc.contourArea(lhs)).compareTo(Imgproc.contourArea(rhs));
            }
        });



        Quadrilateral quad = getQuadrilateral(contours,size);
        ArrayList<MatOfPoint>aa = new ArrayList<MatOfPoint>();
        aa.add(quad.contour);
        Imgproc.drawContours(resizedImage, aa, -1,  new Scalar(0, 255,0 , 255));
        Utils.matToBitmap(resizedImage,a);
        hierarchy.release();
        resizedImage.release();
        grayImage.release();
        cannedImage.release();

        return contours;
    }

    @Nullable
    private static Quadrilateral getQuadrilateral(ArrayList<MatOfPoint> contours, Size srcSize) {
        for ( MatOfPoint c: contours ) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length == 4) {
                Point[] foundPoints = sortPoints(points);
                if(insideArea(foundPoints,srcSize))
                return new Quadrilateral(c, foundPoints);
            }
        }

        return null;
    }


    private static Point[] sortPoints(Point[] src) {

        ArrayList<Point> srcPoints = new ArrayList<>(Arrays.asList(src));

        Point[] result = { null , null , null , null };

        Comparator<Point> sumComparator = new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y + lhs.x).compareTo(rhs.y + rhs.x);
            }
        };

        Comparator<Point> diffComparator = new Comparator<Point>() {

            @Override
            public int compare(Point lhs, Point rhs) {
                return Double.valueOf(lhs.y - lhs.x).compareTo(rhs.y - rhs.x);
            }
        };

        // top-left corner = minimal sum
        result[0] = Collections.min(srcPoints, sumComparator);

        // bottom-right corner = maximal sum
        result[2] = Collections.max(srcPoints, sumComparator);

        // top-right corner = minimal diference
        result[1] = Collections.min(srcPoints, diffComparator);

        // bottom-left corner = maximal diference
        result[3] = Collections.max(srcPoints, diffComparator);

        return result;
    }
    private static boolean insideArea(Point[] rp, Size size) {

        int width = Double.valueOf(size.width).intValue();
        int height = Double.valueOf(size.height).intValue();
        int baseMeasure = height/4;

        int bottomPos = height-baseMeasure;
        int topPos = baseMeasure;
        int leftPos = width/2-baseMeasure;
        int rightPos = width/2+baseMeasure;

        return (
                rp[0].x <= leftPos && rp[0].y <= topPos
                        && rp[1].x >= rightPos && rp[1].y <= topPos
                        && rp[2].x >= rightPos && rp[2].y >= bottomPos
                        && rp[3].x <= leftPos && rp[3].y >= bottomPos

        );
    }
}