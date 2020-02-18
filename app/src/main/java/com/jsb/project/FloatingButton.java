package com.jsb.project;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

public class FloatingButton extends View {

    Paint paint;
    Paint mPaint;
    Paint mPaintForPlus;
    Paint mPaintForOne;
    RectF oval;
    Typeface sriracha = ResourcesCompat.getFont(getContext(), R.font.sriracha_regular);
    Typeface montserrat_bold = ResourcesCompat.getFont(getContext(), R.font.montserrat_extra_bold_italic);
    CornerPathEffect cornerPathEffect;
    int progress = 0;
    int baseRadius = 55;
    int strokeWidth = 18;
    int state = 0;
    int subState = 0;
    int time = 0;

    void setTime(int i) {
        time = i;
        invalidate();
    }

    void setSubState(int subState) {
        this.subState = subState;
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

    public void iniatializer() {
        cornerPathEffect = new CornerPathEffect(baseRadius);
        paint = new Paint();
        mPaint = new Paint();
        mPaintForOne = new Paint();
        mPaintForPlus = new Paint();
        mPaintForOne.setTypeface(sriracha);
        mPaintForPlus.setTypeface(montserrat_bold);
        oval = new RectF(150f - (baseRadius + 20), 150f - (baseRadius + 20), 150f + (baseRadius + 20), 150f + (baseRadius + 20));

    }

    public void setState(int s) {
        state = s;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaintForOne.setTextSize(baseRadius - 10);
        mPaintForOne.setFakeBoldText(false);
        mPaintForPlus.setTextSize(baseRadius - 10);
        mPaintForPlus.setFakeBoldText(false);



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
            paint.setTypeface(montserrat_bold);
            canvas.drawText("+", 125f, 170f, paint);
            paint.setColor(0xffff0000);
            paint.setTypeface(sriracha);
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
            mPaint.setPathEffect(cornerPathEffect);
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.RED);
            canvas.drawArc(oval, -90, -1f + (3.6f * progress), false, mPaint);


        } else if (state == 2) {
            paint.setAntiAlias(true);
            mPaint.setAntiAlias(true);
            mPaint.setColor(0xffedf2ee);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius, mPaint);
            paint.setColor(0xffdbd5d7);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth - 3);
            canvas.drawCircle(150f, 150f, baseRadius - 10, paint);
            mPaintForPlus.setColor(0xff385a7c);
            canvas.drawText("+", 130f, 162f, mPaintForPlus);
            mPaintForOne.setColor(0xffff0000);
            canvas.drawText("1", 150f, 162f, mPaintForOne);

        } else if (state == 3) {
            paint.setAntiAlias(true);
            mPaint.setAntiAlias(true);
            mPaint.setColor(0xff002266);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius, mPaint);
            paint.setColor(0xff666666);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(strokeWidth - 3);
            canvas.drawCircle(150f, 150f, baseRadius - 10, paint);
            mPaintForPlus.setColor(0xffffffff);
            canvas.drawText("+", 130f, 165f, mPaintForPlus);
            mPaintForOne.setColor(0xffff0026);
            canvas.drawText("1", 150f, 162f, mPaintForOne);


        } else if (state == 4) {
            if (time > 0) {
                paint.setColor(0xff666666);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(((baseRadius - 10) * 2) * time / 50);
                canvas.drawCircle(150f, 150f, (baseRadius - 10) * time / 50, paint);
            }

        } else if (state == 5) {
            paint.setAntiAlias(true);
            mPaint.setAntiAlias(true);
            mPaint.setColor(0xffff0000);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(150f, 150f, baseRadius, mPaint);
            mPaintForPlus.setColor(0xffffffff);
            canvas.drawText("+", 130f, 162f, mPaintForPlus);
            mPaintForOne.setColor(0xffffffff);
            canvas.drawText("1", 150f, 162f, mPaintForOne);
        }
        else if(state == 6) {
            if(subState == 0) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.RED);

                mPaintForPlus.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaintForOne.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(150f, 150f, baseRadius+10, mPaint);
                mPaintForPlus.setColor(0xffffffff);
                canvas.drawText("+", 125f, 162f, mPaintForPlus);
                mPaintForOne.setColor(0xffffffff);
                canvas.drawText("1", 155f, 162f, mPaintForOne);
            }
            else if(subState == 1) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.WHITE);
                mPaintForPlus.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaintForOne.setStyle(Paint.Style.FILL_AND_STROKE);
                canvas.drawCircle(140f, 150f, baseRadius+10, mPaint);
                mPaintForPlus.setColor(Color.RED);
                mPaintForOne.setColor(Color.RED);
                canvas.drawText("+ ", 125f, 162f, mPaintForPlus);
                canvas.drawText("1", 155f, 162f, mPaintForOne);
            }
            else if (subState == 2) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.BLACK);
                mPaintForOne.setStyle(Paint.Style.STROKE);
                mPaintForPlus.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(150f, 150f, baseRadius+10, mPaint);
                mPaintForPlus.setColor(0xffffffff);
                canvas.drawText("+ ", 127f, 162f, mPaintForPlus);
                mPaintForOne.setColor(0xffff0000);
                canvas.drawText("1", 153f, 162f, mPaintForOne);
            }
        }



    }

    public void startFlashAnimation(final OnFlashAnimationEnded onFlashAnimationEnded) {
        setState(6);
        setEnabled(false);
        setOnTouchListener(null);
        final Handler handler = new Handler(Looper.getMainLooper());
        final int[] count = {0};
        final int baseTime = 400;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(count[0] == 0) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime/3);
                }
                else if(count[0] == 1) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime/10);

                }
                else if(count[0] == 2) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                }
                else if(count[0] == 3) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime/10);
                }
                else if(count[0] == 4) {
                    setSubState(2);
                    count[0]++;
                    handler.postDelayed(this, baseTime/3);
                }
                else if (count[0] == 5) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime/10);
                }
                else if (count[0] == 6) {
                    setSubState(2);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                }
                else if (count[0] == 7) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime/10);
                }
                else if(count[0] == 8) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime/3);
                }
                else if(count[0] == 9) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime/10);

                }
                else if(count[0] == 10) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                }
                else {
                    onFlashAnimationEnded.onAnimationEnded();
                }
            }
        }, 100);

    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}

