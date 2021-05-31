package com.example.tfg.Views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;


public class RectSubSamplingScaleImage extends SubsamplingScaleImageView {


    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private PointF sPin;
    private Bitmap pin;
    public ArrayList<RectF> rectangles = new ArrayList<RectF>();

    public RectSubSamplingScaleImage(Context context) {
        this(context, null);
    }
    public RectSubSamplingScaleImage(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        initialise();
        invalidate();
    }


    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), android.R.drawable.alert_dark_frame);
        float w = (density/2880f) * pin.getWidth();
        float h = (density/2880f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            sourceToViewCoord(sPin, vPin);
            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }
       for (RectF rec : rectangles){

           PointF top = resize(new PointF(rec.left,rec.top));
           PointF bot = resize(new PointF(rec.right,rec.bottom));

           canvas.drawRect(new RectF(top.x,top.y,bot.x,bot.y),paint);
       }


    }

    public PointF resize(PointF src){
        PointF dst = new PointF();
        sourceToViewCoord(src, dst);
        return dst;
    }

}