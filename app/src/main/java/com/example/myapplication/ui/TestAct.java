package com.example.myapplication.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

public class TestAct extends Activity {
    LinearLayout topView;
    ImageButton foo;
    int viewHeight;
    boolean noSwap = true;
    private static int ANIMATION_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        topView = (LinearLayout) findViewById(R.id.top_view);

        foo = (ImageButton) findViewById(R.id.switch_btn);

        ImageButton btnSwitch = (ImageButton) findViewById(R.id.switch_btn);

        ViewTreeObserver viewTreeObserver = foo.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    foo.getViewTreeObserver().addOnGlobalLayoutListener(this);
                    viewHeight = foo.getHeight();
                    foo.getLayoutParams();
                }
            });
        }

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noSwap) {
                    TranslateAnimation ta1 = new TranslateAnimation(0, 0, 0, viewHeight);
                    ta1.setDuration(ANIMATION_DURATION);
                    ta1.setFillAfter(true);
                    topView.startAnimation(ta1);
                    topView.bringToFront();

                    TranslateAnimation ta2 = new TranslateAnimation(0, 0, 0, -viewHeight);
                    ta2.setDuration(ANIMATION_DURATION);
                    ta2.setFillAfter(true);
                    topView.startAnimation(ta2);
                    topView.bringToFront();

                    noSwap = false;
                } else {
                    TranslateAnimation ta1 = new TranslateAnimation(0, 0, viewHeight, 0);
                    ta1.setDuration(ANIMATION_DURATION);
                    ta1.setFillAfter(true);
                    topView.startAnimation(ta1);
                    topView.bringToFront();

                    TranslateAnimation ta2 = new TranslateAnimation(0, 0, -viewHeight, 0);
                    ta2.setDuration(ANIMATION_DURATION);
                    ta2.setFillAfter(true);
                    topView.startAnimation(ta2);
                    topView.bringToFront();

                    noSwap = true;
                }
            }
        });
    }
}
