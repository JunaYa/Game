package com.aya.marquee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private ImageView center;
    private View v1, v2, v3, v4, v5, v6, v7, v8, v9;
    private View g1, g2, g3, g4, g5, g6, g7, g8, g9;
    private ValueAnimator valueAnimator;
    private int curValue;
    private int[] img_g =
            {
                    R.drawable.g1,
                    R.drawable.g2,
                    R.drawable.g3,
                    R.drawable.g6,
                    R.drawable.g9,
                    R.drawable.g8,
                    R.drawable.g7,
                    R.drawable.g4
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        center = (ImageView) this.findViewById(R.id.center);

        v1 = this.findViewById(R.id.v1);
        v2 = this.findViewById(R.id.v2);
        v3 = this.findViewById(R.id.v3);
        v4 = this.findViewById(R.id.v4);
        v5 = this.findViewById(R.id.v5);
        v6 = this.findViewById(R.id.v6);
        v7 = this.findViewById(R.id.v7);
        v8 = this.findViewById(R.id.v8);
        v9 = this.findViewById(R.id.v9);

        g1 = this.findViewById(R.id.g1);
        g2 = this.findViewById(R.id.g2);
        g3 = this.findViewById(R.id.g3);
        g4 = this.findViewById(R.id.g4);
        g5 = this.findViewById(R.id.g5);
        g6 = this.findViewById(R.id.g6);
        g7 = this.findViewById(R.id.g7);
        g8 = this.findViewById(R.id.g8);
        g9 = this.findViewById(R.id.g9);

        reSetBg();
        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetBg();
                Random random = new Random();
                int randomNum = random.nextInt(8) + 1;
                startAnim(randomNum);
                valueAnimator.start();
            }
        });
    }

    private void startAnim(int value) {
        valueAnimator = ValueAnimator.ofInt(0, 90 + value);
        valueAnimator.setDuration(3 * 3000);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                curValue = (int) valueAnimator.getAnimatedValue();
                marqueeImage(curValue % 8);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        TranslateAnimation animation = new TranslateAnimation(0,0,0,0);
                        center.setImageResource(img_g[(curValue % 8)]);
                        center.setAnimation(animation);
                        animation.start();
                        g5.setVisibility(View.GONE);
                    }
                }, 1000);

            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

    }

    private void marqueeImage(int value) {
        reSetRbg();
        switch (value) {
            case 0:
                g1.setVisibility(View.VISIBLE);
                break;
            case 1:
                g2.setVisibility(View.VISIBLE);
                break;
            case 2:
                g3.setVisibility(View.VISIBLE);
                break;
            case 3:
                g6.setVisibility(View.VISIBLE);
                break;
            case 4:
                g9.setVisibility(View.VISIBLE);
                break;
            case 5:
                g8.setVisibility(View.VISIBLE);
                break;
            case 6:
                g7.setVisibility(View.VISIBLE);
                break;
            case 7:
                g4.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void reSetRbg() {
        g1.setVisibility(View.GONE);
        g2.setVisibility(View.GONE);
        g3.setVisibility(View.GONE);
        g4.setVisibility(View.GONE);
        g6.setVisibility(View.GONE);
        g7.setVisibility(View.GONE);
        g8.setVisibility(View.GONE);
        g9.setVisibility(View.GONE);
    }

    private void reSetBg() {
        v1.setVisibility(View.VISIBLE);
        v2.setVisibility(View.VISIBLE);
        v3.setVisibility(View.VISIBLE);
        v4.setVisibility(View.VISIBLE);
        v6.setVisibility(View.VISIBLE);
        v7.setVisibility(View.VISIBLE);
        v8.setVisibility(View.VISIBLE);
        v9.setVisibility(View.VISIBLE);
    }

}
