package com.aya.games.luckmarquee.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Single on 2015/10/26.
 */
public class SurfaceViewTemplate extends SurfaceView {

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;

    private Thread thread;

    public SurfaceViewTemplate(Context context) {
        super(context , null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
