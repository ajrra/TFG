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
    public static boolean CompareMatEntry(Mat src, Mat comp){




        Mat tmp_1= preprocess(src);
        Mat tmp_2= preprocess(comp);
        Mat tmp_3 = new Mat();
        Core.absdiff(tmp_1,tmp_2,tmp_3);
        double val = Core.countNonZero(tmp_3)*100/tmp_3.size().area();
        return val > 90;

    }

    public static Bitmap unpackMat ( long id){
        Mat m = new Mat( id );
        Imgproc.cvtColor(m, m, Imgproc.COLOR_RGBA2BGR);
        Size size = m.size();
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);

        Imgproc.cvtColor(m, grayImage, Imgproc.COLOR_RGBA2GRAY,4);


        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,115,55);

        Imgproc.erode(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));


        Bitmap bm = Bitmap.createBitmap(grayImage.cols(), grayImage.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(grayImage, bm);
        return bm;
    }




    public static RectF adjustItemTemplate(Mat m , RectF boundingBox){
        //Rect bTemp = new Rect (Math.round(   boundingBox.left), Math.round(   boundingBox.bottom),Math.round(   boundingBox.right-boundingBox.left),Math.round(   boundingBox.bottom-boundingBox.top));
        Point ofs = new Point();

        Point p_1 = new Point(boundingBox.left, boundingBox.top);
        Point p_2 = new Point(boundingBox.right, boundingBox.top);
        Point p_3 = new Point(boundingBox.right, boundingBox.bottom);
        Point p_4 = new Point(boundingBox.left, boundingBox.bottom);
        Rect bTemp =  new Rect(p_4, p_2);
        Mat temp = new Mat (m , bTemp);
        Mat aux = temp.clone();

        temp.locateROI(m.size(),ofs);
        temp = CV_Paper.preprocess_item(temp);
        Bitmap bm = Bitmap.createBitmap(temp.cols(), temp.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp, bm);
        ArrayList<MatOfPoint> contours = findCountour(temp, ofs);
        Imgproc.drawContours(aux,contours,-1,new Scalar(255,0,0),3);
        bm = Bitmap.createBitmap(aux.cols(), aux.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(aux, bm);
       Quadrilateral quad = getQuadrilateral(contours,temp.size(),ofs);


//        Quadrilateral quad = findDocument(temp , temp.size(),ofs);
        if (quad != null){
         /*   Mat M;
            Point[] p1 = {p_1,p_2,p_3,p_4};
            Point[] p2 = {new Point(0, 0), new Point(0, m.height()),new Point(m.width(), m.height()), new Point(m.width(), 0)};
            p2=sortPoints(p2);
            MatOfPoint2f pts1 = new MatOfPoint2f(p1[0],p1[1],p1[2],p1[3]);
            MatOfPoint2f pts2 = new MatOfPoint2f(p2[0],p2[1],p2[2],p2[3]);
            M=Imgproc.getAffineTransform(pts2,pts1);
            M.adjustROI()*/

           return new RectF ((float )quad.points[3].x, (float )quad.points[1].y, (float )quad.points[1].x,(float ) quad.points[3].y);
        }
        return null;
    }


    public static Mat preprocess_item(Mat src){
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2BGR);
        Size size = src.size();
        Mat grayImage = new Mat(size, CvType.CV_8UC4);
        Mat cannedImage = new Mat(size, CvType.CV_8UC1);
        Bitmap bm = Bitmap.createBitmap(cannedImage.cols(), cannedImage.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(src, bm);
        //PASAMOS A GRIS PARA TRABAJAR MEJOR

        Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGBA2GRAY, 4);
        for (int i =0 ; i<3 ; i++){
            Imgproc.erode(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
            Utils.matToBitmap(grayImage, bm);
        }
        //APLICAMOS UN THRESHOLD ADAPTATIVO PARA ELIMINAR SOMBRAS
        Imgproc.adaptiveThreshold(grayImage,cannedImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,25, 4);
        Utils.matToBitmap(cannedImage, bm);
       // Core.copyMakeBorder(cannedImage,cannedImage,5,5,5,5,Core.BORDER_CONSTANT);
        //ALGORITMO CANNY DE DETECCION DE BORDES
        //Imgproc.Canny(grayImage, cannedImage, 200, 250);
        return cannedImage;
    }




    public static Mat preprocess(Mat src){
        Imgproc.cvtColor(src, src, Imgproc.COLOR_RGBA2BGR);
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
        Imgproc.adaptiveThreshold(grayImage,grayImage,255,Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,Imgproc.THRESH_BINARY,115, 4);

        //APLICAMOS UNA DIFUMINACION PARA MEJORAR
      //  Imgproc.GaussianBlur(grayImage,grayImage,new Size(5,5),0);
        Imgproc.dilate(grayImage,grayImage, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3)));
        Core.copyMakeBorder(grayImage,grayImage,5,5,5,5,Core.BORDER_CONSTANT);
        //ALGORITMO CANNY DE DETECCION DE BORDES
        Imgproc.Canny(grayImage, cannedImage, 100, 200);
        Bitmap bm = Bitmap.createBitmap(cannedImage.cols(), cannedImage.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(cannedImage, bm);
        return cannedImage;
    }

    public static ArrayList<MatOfPoint> findCountour(Mat src,Point ofs){
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();

        Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE,ofs);
        Collections.sort(contours, (lhs, rhs) -> Double.valueOf(Imgproc.contourArea(lhs)).compareTo(Imgproc.contourArea(rhs)));

        return  contours;
    }

     public static Quadrilateral findEdges(Bitmap map, Point ofs){


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
        Quadrilateral puntos = CV_Paper.findDocument(mResize,size,ofs);

        if(puntos !=null) {
            ArrayList<MatOfPoint> contour = new ArrayList<>();
            contour.add(puntos.contour);
            Imgproc.drawContours(mResize, contour, -1,  new Scalar(0, 255, 0, 255),50);
        }

        Imgproc.resize(mResize,mRgba,s_mRgba);
        Utils.matToBitmap(mRgba,map);
        return puntos;


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
            Imgproc.approxPolyDP(c2f, approx, 0.03 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length == 4 && Imgproc.isContourConvex(new MatOfPoint(approx.toArray()))) {
                Point[] foundPoints = sortPoints(points);
                if(insideArea(foundPoints,srcSize,ofs))
                return new Quadrilateral(c, foundPoints);
            }
        }

        return null;
    }


    @Nullable
    private static Quadrilateral getQuadrilateral_item(ArrayList<MatOfPoint> contours, Size srcSize,Point ofs) {
        for ( MatOfPoint c: contours ) {
            MatOfPoint2f c2f = new MatOfPoint2f(c.toArray());
            double peri = Imgproc.arcLength(c2f, true);
            MatOfPoint2f approx = new MatOfPoint2f();
            Imgproc.approxPolyDP(c2f, approx, 0.03 * peri, true);

            Point[] points = approx.toArray();

            // select biggest 4 angles polygon
            if (points.length == 4 && Imgproc.isContourConvex(new MatOfPoint(approx.toArray()))) {
                Point[] foundPoints = sortPoints(points);
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

        int width = Double.valueOf(size.width).intValue() ;
        int height = Double.valueOf(size.height).intValue();
        int baseMeasure = height/4;

        int bottomPos = height-baseMeasure;
        int topPos = baseMeasure ;
        int leftPos = width/2-baseMeasure ;
        int rightPos = width/2+baseMeasure;

        return (
                rp[0].x-(int)ofs.x <= leftPos && rp[0].y-(int)ofs.y <= topPos
                        && rp[1].x-(int)ofs.x >= rightPos && rp[1].y-(int)ofs.y <= topPos
                        && rp[2].x-(int)ofs.x >= rightPos && rp[2].y-(int)ofs.y >= bottomPos
                        && rp[3].x-(int)ofs.x <= leftPos && rp[3].y-(int)ofs.y >= bottomPos

        );
    }
}