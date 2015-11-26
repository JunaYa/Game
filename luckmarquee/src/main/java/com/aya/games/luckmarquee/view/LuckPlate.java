package com.aya.games.luckmarquee.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aya.games.luckmarquee.R;

/**
 * Created by Single on 2015/10/26.
 */
public class LuckPlate extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    private SurfaceHolder mHolder;
    private Canvas mCanvas;

    private Thread thread;
    private boolean isRunning;

    private String[] mStrs = new String[]{"林黛玉", "贾迎春", "史湘云", "妙玉", "贾元春", "王熙凤"};
    private int[] mImgs = new int[]{R.mipmap.img_lindaiyu, R.mipmap.img_jiayingchun,
            R.mipmap.img_shixiangyun, R.mipmap.img_miaoyu, R.mipmap.img_jiayuanchun, R.mipmap.img_qinkeqin};
    private int[] mColor = new int[]{0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01};

    private int mItemCount = 6;
    //与图片对应的数组
    private Bitmap[] mImagesBitmap;
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);

    //整个盘快的范围
    private RectF mRange = new RectF();
    //整个盘快的直径
    private int mRadius;
    //绘制盘快的画笔
    private Paint mArcPaint;
    //绘制文本的画笔
    private Paint mTextPaint;
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    //滚动的速度
    private double mSpeed;
    //开始角度
    private volatile int mStartAngle = 0;
    //判断是否点击了停止
    private boolean isShouldEnd;

    //转盘的中心位置
    private int mCenter;
    //以pandding left为准
    private int mPadding;


    public LuckPlate(Context context) {
        super(context, null);
    }

    public LuckPlate(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHolder = getHolder();
        mHolder.addCallback(this);

        //获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mPadding = getPaddingLeft();
        mRadius = width - mPadding * 2;
        mCenter = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //初始化绘画 画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);

        mImagesBitmap = new Bitmap[mItemCount];
        for (int i = 0; i < mItemCount; i++) {
            mImagesBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
        }

        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            draw();
            long endTime = System.currentTimeMillis();
            if (endTime - startTime < 50) {
                try {
                    Thread.sleep(50 - (endTime - startTime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void luckStart() {
        mSpeed = 30;
        isShouldEnd = false;
    }

    public void luckEnd() {
        isShouldEnd = true;
    }

    public boolean isStartLuck() {
        return mSpeed != 0;
    }

    public boolean isShouldEnd() {
        return isShouldEnd;
    }

    private void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //绘制背景
                drawBg();
                //绘制盘快
                float tmpAngle = mStartAngle;
                float sweepAngle = 360 / mItemCount;
                for (int i = 0; i < mItemCount; i++) {
                    mArcPaint.setColor(mColor[i]);
                    mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);
                    //绘制文本
                    drawText(tmpAngle, sweepAngle, mStrs[i]);
                    drawIcon(tmpAngle, mImagesBitmap[i]);
                    tmpAngle += sweepAngle;
                }
                mStartAngle += mSpeed;
                //如果点击了停止按钮
                if (isShouldEnd) {
                    mSpeed -= 1;
                }
                if (mSpeed <= 0) {
                    mSpeed = 0;
                    isShouldEnd = false;
                }
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    private void drawIcon(float tmpAngle, Bitmap bitmap) {
        //设置图片的宽度为直径1/8
        int imgWidth = mRadius / 8;
        float angle = (float) ((tmpAngle + 360 / mItemCount / 2) * Math.PI / 180);
        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        //确定图片位置
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        mCanvas.drawBitmap(bitmap, null, rect, null);
    }

    private void drawText(float tmpAngle, float sweepAngle, String str) {
        Path path = new Path();
        path.addArc(mRange, tmpAngle, sweepAngle);
        float textWidth = mTextPaint.measureText(str);
        int hOffset = (int) (mRadius * Math.PI / mItemCount / 2 - textWidth / 2);
        int vOffset = mRadius / 2 / 6;
        mCanvas.drawTextOnPath(str, path, hOffset, vOffset, mTextPaint);
    }

    private void drawBg() {
        mCanvas.drawColor(0xffffffff);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2, mPadding / 2, getMeasuredWidth() - mPadding / 2, getMeasuredHeight() - mPadding / 2), null);
    }
}
