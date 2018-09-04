package com.fxc.myvideoplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MyRelativeView extends RelativeLayout {
    public MyRelativeView(Context context) {
        super(context);
    }

    public MyRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //showTopMenu();
                Log.i("showTopMenu();", "onInterceptTouchEvent:showTopMenu(); ");
                break;
            default:
                break;
        }
        if (event.getAction()==MotionEvent.ACTION_UP){ Log.i("showTopMenu();", "onInterceptTouchEvent:showTopMenu()1111; ");}
        Log.i("showTopMenu();", "onInterceptTouchEvent:showTopMenu()2222; ");

        return super.onInterceptTouchEvent(event);
    }
}
