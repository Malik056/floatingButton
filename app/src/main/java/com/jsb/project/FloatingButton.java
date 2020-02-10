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

    public FloatingButton(Context context) {
        super(context);
        paint = new Paint();

    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FloatingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = getX();
        float y = getY();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
//        canvas.drawPaint(paint);
        canvas.drawCircle(105f,105f, 50, paint);



    }
}
