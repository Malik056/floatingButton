package com.jsb.project;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class FloatingButton extends View {

    Paint baseBtnPaint;
    Paint innerRingPaint;
    Paint baseBtnDuringHoldPaint;
    Paint loadingBarBackgroundPaing;
    Paint loadingBarPaint;
    Paint mPaintForPlus;
    Paint mPaintForOne;
    Paint flashPaint;
    RectF oval;

    int baseBtnNormalColor;
    int baseBtnPressedColor;
    int innerRingNormalColor;
    int innerRingPressedColor;
    int loadingBarBackgroundColor;
    int loadingBarColor;
    int baseBtnDuringHoldColor;
    int plusSignColor;

    float baseBtnRadius;
    float innerRingRadius;
    float baseBtnDuringHoldRadius;
    float loadingBarRadius;

    float textSize;

    //    Typeface sriracha = ResourcesCompat.getFont(getContext(), R.font.sriracha_regular);
    Typeface montserrat_bold = ResourcesCompat.getFont(getContext(), R.font.montserrat_extra_bold_italic);
    CornerPathEffect cornerPathEffect;
    int progress = 0;
    //    int baseRadius = 55;
//    int strokeWidth = 18;
    int state = 0;
    int subState = 0;
    int time = 0;

//    void setTime(int i) {
//        time = i;
//        invalidate();
//    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        cornerPathEffect = new CornerPathEffect(params.width / 2f);
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

        initializer();


    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializer();
    }

    public FloatingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializer();
    }


    public void initializer() {

        baseBtnNormalColor = ContextCompat.getColor(getContext(), R.color.floating_button_base_button_normal);
        baseBtnPressedColor = ContextCompat.getColor(getContext(), R.color.floating_button_base_button_pressed);
        baseBtnDuringHoldColor = ContextCompat.getColor(getContext(), R.color.floating_button_base_button_during_hold);
        innerRingNormalColor = ContextCompat.getColor(getContext(), R.color.floating_button_base_button_inside_ring_normal);
        innerRingPressedColor = ContextCompat.getColor(getContext(), R.color.floating_button_base_button_inside_ring_pressed);
        loadingBarBackgroundColor = ContextCompat.getColor(getContext(), R.color.floating_button_loading_bar_background);
        loadingBarColor = ContextCompat.getColor(getContext(), R.color.floating_button_loading_bar);
        plusSignColor = ContextCompat.getColor(getContext(), R.color.floating_button_plus_sign_normal_color);

//        setLayoutParams(new LinearLayout.LayoutParams((baseRadius + 50) * 2, (baseRadius + 50) * 2));


        baseBtnPaint = new Paint();
        baseBtnPaint.setAntiAlias(true);
        baseBtnPaint.setStrokeWidth(4);

        baseBtnDuringHoldPaint = new Paint();
        baseBtnDuringHoldPaint.setStyle(Paint.Style.FILL);
        baseBtnDuringHoldPaint.setColor(baseBtnDuringHoldColor);
        baseBtnDuringHoldPaint.setStrokeWidth(4);
        baseBtnDuringHoldPaint.setAntiAlias(true);

        innerRingPaint = new Paint();
        innerRingPaint.setStyle(Paint.Style.STROKE);
        innerRingPaint.setAntiAlias(true);

        loadingBarBackgroundPaing = new Paint();
        loadingBarBackgroundPaing.setColor(loadingBarBackgroundColor);
        loadingBarBackgroundPaing.setStyle(Paint.Style.STROKE);
        loadingBarBackgroundPaing.setAntiAlias(true);


        loadingBarPaint = new Paint();
        loadingBarPaint.setStyle(Paint.Style.STROKE);
        loadingBarPaint.setColor(loadingBarColor);
        loadingBarPaint.setAntiAlias(true);
        loadingBarPaint.setDither(true);
        loadingBarPaint.setStrokeJoin(Paint.Join.ROUND);
        loadingBarPaint.setStrokeCap(Paint.Cap.ROUND);
        loadingBarPaint.setPathEffect(cornerPathEffect);

        mPaintForOne = new Paint();
        mPaintForPlus = new Paint();
        mPaintForOne.setTypeface(montserrat_bold);
        mPaintForPlus.setTypeface(montserrat_bold);
        mPaintForPlus.setAntiAlias(true);
        mPaintForOne.setAntiAlias(true);
        mPaintForOne.setColor(loadingBarColor);

        flashPaint = new Paint();
        flashPaint.setStyle(Paint.Style.FILL);
        flashPaint.setAntiAlias(true);

//        oval = new RectF(150f - (baseRadius + 20), 150f - (baseRadius + 20), 150f + (baseRadius + 20), 150f + (baseRadius + 20));

        oval = new RectF(0, 0, 0, 0);

    }


    public void setState(int s) {
        state = s;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        baseBtnRadius = getWidth() / 4;
        baseBtnDuringHoldRadius = baseBtnRadius * 2;
        innerRingRadius = baseBtnRadius * 0.8f;
        loadingBarRadius = baseBtnRadius * 1.55f;


        baseBtnPaint.setShadowLayer(baseBtnRadius * 0.5f, 0, 0, 0x77000000);
        innerRingPaint.setStrokeWidth(baseBtnRadius * 0.2f);
        loadingBarBackgroundPaing.setStrokeWidth(baseBtnDuringHoldRadius * 0.2f - 2f);
        loadingBarPaint.setStrokeWidth(baseBtnDuringHoldRadius * 0.2f);
        textSize = baseBtnRadius * 1.05f;
        mPaintForPlus.setTextSize(textSize);
        mPaintForOne.setTextSize(textSize);

//        float largestCircleMultiplier = 2f;
//        float loadingCircleMultiplier = 1.75f;
//        float insideCircleMultifplier = 0.9f;

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        oval.set(centerX - loadingBarRadius, centerY - loadingBarRadius, centerX + loadingBarRadius, centerY + loadingBarRadius);
//        mPaintForOne.setTextSize(baseRadius - 10);
//        mPaintForOne.setFakeBoldText(false);
//        mPaintForPlus.setTextSize(baseRadius - 10);
//        mPaintForPlus.setFakeBoldText(false);

        if (state == 1) {
//            paint.setStyle(Paint.Style.FILL);
//            paint.setAntiAlias(true);
//            float textSize = baseRadius + 5;
//            paint.setTextSize(textSize);
//            paint.setFakeBoldText(true);
//            mPaint.setStrokeWidth(strokeWidth);
//            mPaint.setColor(0xffdaedf0);
//            mPaint.setAntiAlias(true);
//            mPaint.setStyle(Paint.Style.STROKE);
//            paint.setColor(0xffdaedf0);
//            mPaint.setColor(0xffb1d0d6);
//            paint.setShadowLayer(baseRadius * 0.25f, 0, 0, 0xff000000);
//
//            mPaint.setColor(0xffedf2ee);
//            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(centerX, centerY, baseBtnDuringHoldRadius, baseBtnDuringHoldPaint);
//            paint.setShadowLayer(0, 0, 0, 0x00000000);
//            paint.setTypeface(montserrat_bold);
            baseBtnPaint.setColor(baseBtnNormalColor);
            canvas.drawCircle(centerX, centerY, baseBtnRadius, baseBtnPaint);

//            canvas.drawText("+", centerX - 1.5f * textSize, centerY - textSize / 2f, paint);
//            paint.setTypeface(sriracha);
//            canvas.drawText("1", centerX + textSize / 2f, centerY / 2 - textSize / 2f, paint);


//            mPaintForOne.setTextSize(textSize);
//            mPaintForPlus.setTextSize(textSize);

            mPaintForPlus.setColor(plusSignColor);
            canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
            canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);

//            mPaint.setColor(0xffffffff);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(strokeWidth - 5);
            innerRingPaint.setColor(innerRingNormalColor);
            canvas.drawCircle(centerX, centerY, innerRingRadius, innerRingPaint);

//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeWidth(strokeWidth - 2);
//            mPaint.setColor(0xffffffff);

            canvas.drawCircle(centerX, centerY, loadingBarRadius, loadingBarBackgroundPaing);

//            mPaint.setDither(true);
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setStrokeJoin(Paint.Join.ROUND);
//            mPaint.setStrokeCap(Paint.Cap.ROUND);
//            mPaint.setPathEffect(cornerPathEffect);
//            mPaint.setAntiAlias(true);
//            mPaint.setColor(Color.RED);
            canvas.drawArc(oval, -90, -1f + (3.6f * progress), false, loadingBarPaint);


        } else if (state == 2) {
//            paint.setAntiAlias(true);
//            mPaint.setAntiAlias(true);
//            mPaint.setColor(0xffedf2ee);
//            mPaint.setStyle(Paint.Style.FILL);
            baseBtnPaint.setColor(baseBtnNormalColor);
            canvas.drawCircle(centerX, centerY, baseBtnRadius, baseBtnPaint);
//            paint.setColor(0xffdbd5d7);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(strokeWidth - 3);
            innerRingPaint.setColor(innerRingNormalColor);
            canvas.drawCircle(centerX, centerY, innerRingRadius, innerRingPaint);

            mPaintForPlus.setColor(plusSignColor);

            canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
            canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);


        } else if (state == 3) {
//            paint.setAntiAlias(true);
//            mPaint.setAntiAlias(true);
//            mPaint.setColor(0xff002266);
//            mPaint.setStyle(Paint.Style.FILL);
            baseBtnPaint.setColor(baseBtnPressedColor);
            canvas.drawCircle(centerX, centerY, baseBtnRadius, baseBtnPaint);
//            paint.setColor(0xff666666);
//            paint.setStyle(Paint.Style.STROKE);
//            paint.setStrokeWidth(strokeWidth - 3);
            innerRingPaint.setColor(innerRingPressedColor);
            canvas.drawCircle(centerX, centerY, innerRingRadius, innerRingPaint);
            mPaintForPlus.setColor(Color.WHITE);
//            mPaintForOne.setColor(0xffff0026);
//            float textSize = baseRadius * 0.9f;
//            mPaintForOne.setTextSize(textSize);
//            mPaintForPlus.setTextSize(textSize);

            canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
            canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);


        } else if (state == 4) {
            if (time > 0) {
//                paint.setColor(0xff666666);
//                paint.setStyle(Paint.Style.STROKE);
//                paint.setStrokeWidth(((baseRadius - 10) * 2) * time / 50);
//                canvas.drawCircle(150f, 150f, (baseRadius - 10) * time / 50, paint);
            }

        } else if (state == 5) {
//            paint.setAntiAlias(true);
//            mPaint.setAntiAlias(true);
//            mPaint.setColor(0xffff0000);
//            mPaint.setStyle(Paint.Style.FILL);
//            canvas.drawCircle(150f, 150f, baseBtnRadius, baseBtnPaint);
//            mPaintForPlus.setColor(0xffffffff);
//            canvas.drawText("+", 130f, 162f, mPaintForPlus);
//            mPaintForOne.setColor(0xffffffff);
//            canvas.drawText("1", 150f, 162f, mPaintForOne);
        } else if (state == 6) {
            float sizeMultiplier = 1.2f;
            if (subState == 0) {
//                mPaintForOne.setTextSize(baseBtnRadius + 5);
//                mPaintForPlus.setTextSize(baseBtnRadius + 5);
//                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setColor(Color.RED);

                flashPaint.setColor(loadingBarColor);
                canvas.drawCircle(centerX, centerY, baseBtnRadius * sizeMultiplier, flashPaint);
                mPaintForPlus.setStyle(Paint.Style.FILL);
                mPaintForOne.setStyle(Paint.Style.FILL);
                mPaintForPlus.setColor(Color.WHITE);
                mPaintForOne.setColor(Color.WHITE);
                canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
                canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);

            } else if (subState == 1) {
//                mPaintForOne.setTextSize(baseBtnRadius + 5);
//                mPaintForPlus.setTextSize(baseBtnRadius + 5);
//                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setColor(Color.WHITE);
//                flashPaint.setColor(Color.WHITE);
                mPaintForPlus.setStyle(Paint.Style.FILL);
                mPaintForOne.setStyle(Paint.Style.FILL);
//                canvas.drawCircle(centerX, centerY, baseBtnRadius * sizeMultiplier, flashPaint);
                mPaintForPlus.setColor(loadingBarColor);
                mPaintForOne.setColor(loadingBarColor);
                canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
                canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);
            } else if (subState == 2) {
//                mPaintForOne.setTextSize(baseBtnRadius + 5);
//                mPaintForPlus.setTextSize(baseBtnRadius + 5);
//                mPaint.setStyle(Paint.Style.FILL);
//                mPaint.setColor(Color.BLACK);
                flashPaint.setColor(Color.BLACK);
                canvas.drawCircle(centerX, centerY, baseBtnRadius + sizeMultiplier, flashPaint);
                mPaintForOne.setStrokeWidth(4);
                mPaintForPlus.setStrokeWidth(4);
                mPaintForOne.setStyle(Paint.Style.STROKE);
                mPaintForPlus.setStyle(Paint.Style.STROKE);
                mPaintForPlus.setColor(Color.WHITE);
                mPaintForOne.setColor(loadingBarColor);

                canvas.drawText("+", centerX - (textSize / 2f) - 3f, centerY + (textSize / 3f) + 1.5f, mPaintForPlus);
                canvas.drawText("1", centerX, centerY + (textSize / 3f), mPaintForOne);

            }
        }
//        mPaint.setColor(Color.GREEN);
//        mPaint.setStrokeWidth(1);
//        canvas.drawLine(centerX, 0, centerX, centerY * 2, mPaint);
//        canvas.drawLine(0, centerY, centerX * 2, centerY, mPaint);


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
                if (count[0] == 0) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 3);
                } else if (count[0] == 1) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 10);

                } else if (count[0] == 2) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                } else if (count[0] == 3) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 10);
                } else if (count[0] == 4) {
                    setSubState(2);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 3);
                } else if (count[0] == 5) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 10);
                } else if (count[0] == 6) {
                    setSubState(2);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                } else if (count[0] == 7) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 10);
                } else if (count[0] == 8) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 3);
                } else if (count[0] == 9) {
                    setSubState(1);
                    count[0]++;
                    handler.postDelayed(this, baseTime / 10);

                } else if (count[0] == 10) {
                    setSubState(0);
                    count[0]++;
                    handler.postDelayed(this, baseTime);
                } else {
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

