package com.example.tfg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class CV_Paper {

    /**
     *  Object that encapsulates the contour and 4 points that makes the larger
     *  rectangle on the image
     */
    public static class Quadrilateral {
        public MatOfPoint contour;
        public Point[] points;

        public Quadrilateral(MatOfPoint contour, Point[] points) {
            this.contour = contour;
            this.points = points;
        }
    }

    public static Quadrilateral findDocument( Mat inputRgba ) {
        ArrayList<MatOfPoint> contours = findContours(inputRgba);
        Quadrilateral quad = getQuadrilateral(contours,inputRgba.cols()*inputRgba.rows());


        //DEBUGGING CODE PARA VER EL BITMAP EN ANDROID STUDIO
        double ratio = inputRgba.size().height / 800;
        int height = Double.valueOf(inputRgba.size().height / ratio).intValue();
        int width = Double.valueOf(inputRgba.size().width / ratio).intValue();
        Size size = new Size(width,height);
        Mat resizedImage = new Mat(size, CvType.CV_8UC4);
        Imgproc.resize(inputRgba,resizedImage,size);
        Bitmap a =Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        ArrayList<MatOfPoint>aa = new ArrayList<MatOfPoint>();
        aa.add(quad.contour);
        Imgproc.drawContours(resizedImage, aa, -1,  new Scalar(0, 0, 255, 255),50);
        Utils.matToBitmap(resizedImage,a);



        return quad;
    }

    private static ArrayList<MatOfPoint> findContours(Mat src) {

        double ratio = src.size().height / 800;
        int height = Double.valueOf(src.size().height / ratio).intValue();
        int width = Double.valueOf(src.size().width / ratio).intValue();
        Size size = new Size(width,height);
        Bitmap a =Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);;
        Mat resizedImage = new Mat(size, CvType.CV_8UC4);
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);
        //NUEVO TAMAÑO PARA TRABAJAR
        Imgproc.resize(src,resizedImage,size);
        Utils.matToBitmap(resizedImage,a);
        //APLICAMOS UN FILTRO QUE MANTENGA BORDES
        Imgproc.cvtColor(resizedImage, resizedImage, Imgproc.COLOR_RGBA2BGR);
        Mat dst = resizedImage.clone();
        Imgproc.bilateralFilter(resizedImage, dst, 15, 80, 80, Core.BORDER_DEFAULT);
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
        Imgproc.Canny(grayImage, cannedImage, 75, 200);
        Utils.matToBitmap(cannedImage,a);
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(cannedImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Imgproc.drawContours(resizedImage, contours, -1,  new Scalar(0, 0, 255, 255));
        Utils.matToBitmap(resizedImage,a);
        hierarchy.release();

        Collections.sort(contours, new Comparator<MatOfPoint>() {

            @Override
            public int compare(MatOfPoint lhs, MatOfPoint rhs) {
                return Double.valueOf(Imgproc.contourArea(rhs)).compareTo(Imgproc.contourArea(lhs));
            }
        });

        resizedImage.release();
        grayImage.release();
        cannedImage.release();

        return contours;
    }

    private static Quadrilateral getQuadrilateral(ArrayList<MatOfPoint> contours,double MAX_CONTOUR_AREA) {
        double max_area_found = 0;
        Quadrilateral out =null;
        for ( MatOfPoint c: contours ) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length == 4 && Imgproc.contourArea(c) > max_area_found && Imgproc.contourArea(c) < MAX_CONTOUR_AREA  ) {
                Point[] foundPoints = sortPoints(points);

                out= new Quadrilateral(c, foundPoints);
            }
        }

        return out;
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

}