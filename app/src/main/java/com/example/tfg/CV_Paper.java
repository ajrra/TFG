package com.example.tfg;

import android.graphics.Bitmap;
import android.graphics.RectF;

import org.jetbrains.annotations.Nullable;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
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



//Compara la version pre procesada de las dos matrices para
    public static boolean Eval(Mat src, RectF comp){

        Point ofs = new Point();
        Point p_2 = new Point(comp.right, comp.top);
        Point p_4 = new Point(comp.left, comp.bottom);
        Rect bTemp =  new Rect(p_4, p_2);
        Mat temp = new Mat (src , bTemp);
        temp.locateROI(src.size(),ofs);




        double val = Core.countNonZero(temp)*100/temp.size().area();
        return val < 30;

    }





    public static RectF adjustItemTemplate(Mat m , RectF boundingBox){
        Point ofs = new Point();

        Point p_2 = new Point(boundingBox.right, boundingBox.top);

        Point p_4 = new Point(boundingBox.left, boundingBox.bottom);
        Rect bTemp =  new Rect(p_4, p_2);
        Mat temp = new Mat (m , bTemp);


        temp.locateROI(m.size(),ofs);
        temp = CV_Paper.preprocess_item(temp);

        ArrayList<MatOfPoint> contours = findCountour(temp, ofs);

       Quadrilateral quad = getQuadrilateral(contours,temp.size(),ofs);


        if (quad != null){

           return new RectF ((float )quad.points[3].x, (float )quad.points[1].y, (float )quad.points[1].x,(float ) quad.points[3].y);
        }
        return null;
    }


    public static Mat preprocess_item(Mat src){
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2BGR);
        Size size = src.size();
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);

        Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGBA2BGR);
        Mat dst = grayImage.clone();
        Imgproc.bilateralFilter(grayImage, dst, 9, 75, 75, Core.BORDER_DEFAULT);

        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2RGBA);
        Imgproc.cvtColor(dst, grayImage, Imgproc.COLOR_RGBA2GRAY, 4);

        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,25, 15);

        for (int i =0 ; i<0 ; i++){
            Imgproc.dilate(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));

        }


        return grayImage;
    }




    public static Mat preprocess(Mat src){
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2BGR);
        Size size = src.size();
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);

        //APLICAMOS UN FILTRO QUE MANTENGA BORDES PARA REDUCIR RUIDO Y SUAVIZAR LA IMAGEN
        Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGBA2BGR);
        Mat dst = grayImage.clone();
        Imgproc.bilateralFilter(grayImage, dst, 9, 75, 75, Core.BORDER_DEFAULT);
        Imgproc.cvtColor(dst, dst, Imgproc.COLOR_RGB2RGBA);
        //PASAMOS A GRIS PARA TRABAJAR MEJOR
        Imgproc.cvtColor(dst, grayImage, Imgproc.COLOR_RGBA2GRAY, 4);

        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,115, 4);

        //APLICAMOS UNA DIFUMINACION PARA MEJORAR
        Imgproc.GaussianBlur(grayImage,grayImage,new Size(5,5),0);
        Imgproc.dilate(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
        Core.copyMakeBorder(grayImage,grayImage,5,5,5,5,Core.BORDER_CONSTANT);
        //ALGORITMO CANNY DE DETECCION DE BORDES
        Imgproc.Canny(grayImage, cannedImage, 50, 200);


        return cannedImage;
    }

    public static ArrayList<MatOfPoint> findCountour(Mat src,Point ofs){
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE,ofs);
        Collections.sort(contours, (lhs, rhs) -> Double.valueOf(Imgproc.contourArea(lhs)).compareTo(Imgproc.contourArea(rhs)));

        return  contours;
    }




    public static Quadrilateral findDocument( Mat inputRgba,Size size ,Point ofs) {

        ArrayList<MatOfPoint> contours = findContours(inputRgba,size, ofs);
       return getQuadrilateral(contours,size,ofs);


    }


    public static Mat perspectiveAdjust(Mat src, Quadrilateral quad){
         Mat M;
        Point[] p1 = quad.points;
        Point[] p2 = {new Point(0, 0), new Point(0, src.height()),new Point(src.width(), src.height()), new Point(src.width(), 0)};
        p2=sortPoints(p2);
        MatOfPoint2f pts1 = new MatOfPoint2f(p1[0],p1[1],p1[2],p1[3]);
        MatOfPoint2f pts2 = new MatOfPoint2f(p2[0],p2[1],p2[2],p2[3]);
         M=Imgproc.getPerspectiveTransform(pts1,pts2);
         Imgproc.warpPerspective(src,src,M,src.size());
        return src;
    }


  /* public static Mat houghFind(Mat dst,Mat src){
         Quadrilateral tmp = null;
         Mat cdst = dst.clone();
         Mat lines = new Mat();
       Imgproc.HoughLines(src, lines, 1, Math.PI/180, 150);

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

*/

    private static ArrayList<MatOfPoint> findContours(Mat resizedImage, Size size, Point ofs) {



        resizedImage = preprocess(resizedImage);
        ArrayList<MatOfPoint> con = findCountour(resizedImage,ofs);
        return con;
    }

    @Nullable
    private static Quadrilateral getQuadrilateral(ArrayList<MatOfPoint> contours, Size srcSize,Point ofs) {
        for ( MatOfPoint c: contours ) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.02 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length >= 4 && Imgproc.isContourConvex(new MatOfPoint(approx.toArray()))) {
                Point[] foundPoints = sortPoints(points);

                if(insideArea(foundPoints,srcSize,ofs))
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
    private static boolean insideArea(Point[] rp, Size size,Point ofs) {

        if(ofs.x > 0 ) return true;

        int width = Double.valueOf(size.width).intValue() +(int)ofs.x;
        int height = Double.valueOf(size.height).intValue()+(int)ofs.y;
        int baseMeasure = height/4;

        int bottomPos = height-baseMeasure;
        int topPos = baseMeasure ;
        int leftPos = width/2-baseMeasure ;
        int rightPos = width/2+baseMeasure;

        return (
                rp[0].x <= leftPos && rp[0].y <= topPos
                        && rp[1].x >= rightPos && rp[1].y <= topPos
                        && rp[2].x>= rightPos && rp[2].y >= bottomPos
                        && rp[3].x <= leftPos && rp[3].y >= bottomPos

        );
    }
}