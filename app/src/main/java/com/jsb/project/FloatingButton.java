package com.jsb.project;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class FloatingButton extends View {

    Paint paint;
    Paint mPaint;

    public FloatingButton(Context context) {
        super(context);
        iniatializer();


    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        iniatializer();
    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniatializer();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
       iniatializer();
    }
  public void iniatializer(){
      paint = new Paint();
      mPaint = new Paint();
  }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = getX();
        float y = getY();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setTextSize(55);
        paint.setFakeBoldText(true);





        int startTop = 0;
        int startLeft = 0;
        int endBottom = getHeight() / 2;
        int endRight = endBottom;// This makes an equal square.

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int upperEdgeX = (int) (centerX + getWidth() / 2 * Math.cos(270 * Math.PI / 180));
        int upperEdgeY = (int) (centerY + getWidth() / 2 * Math.sin(270 * Math.PI / 180));

        int bottomEdgeX = (int) (centerX + getWidth() / 2 * Math.cos(90 * Math.PI / 180));
        int bottomEdgeY = (int) (centerY + getWidth() / 2 * Math.sin(90 * Math.PI / 180));

        int leftEdgeX = (int) (centerX + getWidth() / 2 * Math.cos(180 * Math.PI / 180));
        int leftEdgeY = (int) (centerY + getWidth() / 2 * Math.sin(180 * Math.PI / 180));

        int rightEdgeX = (int) (centerX + getWidth() / 2 * Math.cos(0 * Math.PI / 180));
        int rightEdgeY = (int) (centerY + getWidth() / 2 * Math.sin(0 * Math.PI / 180));

        RectF rect = new RectF(startTop, startLeft, endRight, endBottom);
        canvas.drawCircle(centerX, centerY, getWidth() / 2, mPaint);



//        canvas.drawPaint(paint);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(0xffdaedf0);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffdaedf0);
        mPaint.setColor(0xffb1d0d6);
        canvas.drawCircle(150f,150f, 55, mPaint);
        canvas.drawCircle(150f,150f, 90, mPaint);
        canvas.drawCircle(150f,150f, 50, paint);
        paint.setColor(0xff385a7c);
        canvas.drawText("+",125f,165f,paint);
        paint.setColor(0xff000000);
        canvas.drawText("1",150f,165f,paint);





    }
}
