package com.jsb.project;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
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
    Paint m2paint;
    RectF oval;
    int progress = 0;
    int baseRadius = 50;
    int strokeWidth = 15;
    int state =0;
    int time = 0;

    void setTime(int i) {
        time = i;
        invalidate();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

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
      m2paint=new Paint();
      oval = new RectF(150f - (baseRadius+20), 150f - (baseRadius+20), 150f + (baseRadius+20), 150f + (baseRadius+20));

  }

  public void setState(int s){
        state=s;
        invalidate();
  }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (state == 1) {
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            paint.setTextSize(baseRadius + 5);
            paint.setFakeBoldText(true);
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setColor(0xffdaedf0);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);
            paint.setColor(0xffdaedf0);
            mPaint.setColor(0xffb1d0d6);

            paint.setShadowLayer(baseRadius - 41, 0, 0, 0xff000000);


            mPaint.setColor(0xffedf2ee);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius + 40, mPaint);
            canvas.drawCircle(150f, 150f, baseRadius, paint);
            paint.setColor(0xff385a7c);
            paint.setShadowLayer(0, 0, 0, 0x00000000);

            canvas.drawText("+", 125f, 170f, paint);
            paint.setColor(0xffff0000);
            canvas.drawText("1", 150f, 170f, paint);
            mPaint.setColor(0xffffffff);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth - 5);
            canvas.drawCircle(150f, 150f, baseRadius - 10, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth - 2);
            mPaint.setColor(0xffffffff);

            canvas.drawCircle(150f, 150f, baseRadius + 20, mPaint);

            mPaint.setDither(true);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setPathEffect(new CornerPathEffect(baseRadius));
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.RED);
            canvas.drawArc(oval, -90, -1f + (3.6f * progress), false, mPaint);


        }
        else if(state==2){
            paint.setAntiAlias(true);
            mPaint.setAntiAlias(true);
            m2paint.setTextSize(baseRadius-10);
            m2paint.setFakeBoldText(false);
            mPaint.setColor(0xffedf2ee);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius, mPaint);
            paint.setColor(0xffdbd5d7);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth-3);
            canvas.drawCircle(150f, 150f, baseRadius-10, paint);
            m2paint.setColor(0xff385a7c);
            canvas.drawText("+",130f,162f,m2paint);
            m2paint.setColor(0xffff0000);
            canvas.drawText("1", 150f, 162f, m2paint);

        }
        else if(state==3){
            paint.setAntiAlias(true);
            mPaint.setAntiAlias(true);
            m2paint.setTextSize(baseRadius-10);
            m2paint.setFakeBoldText(true);
            mPaint.setColor(0xff002266);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius, mPaint);
            paint.setColor(0xff666666);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth-3);
            canvas.drawCircle(150f, 150f, baseRadius-10, paint);
            m2paint.setColor(0xffffffff);
            canvas.drawText("+",130f,165f,m2paint);
            m2paint.setColor(0xffff0026);
            canvas.drawText("1", 150f, 162f, m2paint);
        }
        else if(state==4){
            if(time > 0) {
                paint.setColor(0xff666666);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(((baseRadius - 10) * 2) * time / 50);
                canvas.drawCircle(150f, 150f, (baseRadius - 10) * time / 50, paint);
            }

        }
    }
}
