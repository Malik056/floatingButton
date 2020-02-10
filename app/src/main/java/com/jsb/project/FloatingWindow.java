package com.jsb.project;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class FloatingWindow extends Service {

    WindowManager wm;
    LinearLayout ll;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);


        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

  // Button openapp= new Button(this);
        ImageView openapp = new ImageView(this);
        openapp.setImageResource(R.mipmap.ic_launcher);
        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                150,150);
        openapp.setLayoutParams(butnparams);


        ColorDrawable[] color = {new ColorDrawable(Color.TRANSPARENT), new ColorDrawable(Color.TRANSPARENT)};
        TransitionDrawable trans = new TransitionDrawable(color);
        //This will work also on old devices. The latest API says you have to use setBackground instead.
        openapp.setBackgroundDrawable(trans);
        trans.startTransition(5000);


        ll.addView(openapp);
        wm.addView(ll,params);

        openapp.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatepar = params;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        x = updatepar.x;
                        y = updatepar.y;

                        px = motionEvent.getRawX();
                        py = motionEvent.getRawY();

                        break;

                        case MotionEvent.ACTION_MOVE:

                            updatepar.x = (int) (x+(motionEvent.getRawX()-px));
                            updatepar.y = (int) (y+(motionEvent.getRawY()-py));

                            wm.updateViewLayout(ll,updatepar);

                            default:
                            break;
                }

                return false;

            }
        });

        openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(FloatingWindow.this,MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        wm.removeView(ll);
    }
}
