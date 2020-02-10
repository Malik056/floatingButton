package com.jsb.project;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
        paint.
//        canvas.drawPaint(paint);
        mPaint.setStrokeWidth(20);

        mPaint.setColor(Color.GREEN);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(150f,150f, 150, mPaint);
        canvas.drawCircle(150f,150f, 50, paint);



    }
}
