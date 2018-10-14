package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.softwareapp.group9.doctorpatientapp.R;


public class ViewRing extends View{

    private int x,y;
    private int mRadius = 260;
    private Context mC;
    private Paint mPaintRingAnim;
    private Paint mPaintRing;
    private RectF mR;
    private int mAAngle = -1;
    private final int mHeartPaintW = 60;
    private DrawFilter mDrawFilter;
    private int mHeartBeatWidth;
    private int mTotalHeight,mTotalWidth;


    Path p = new Path();

    private void initial(){
        setLayerType(View.LAYER_TYPE_SOFTWARE,null);

        mPaintRing = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaintRing.setStrokeWidth(mHeartPaintW);
        mPaintRing.setStyle(Paint.Style.STROKE);

        mPaintRingAnim = new Paint(mPaintRing);
        mPaintRingAnim.setColor(mC.getResources().getColor(R.color.P));

        if(!isInEditMode())
        {
            mPaintRing.setColor(mC.getResources().getColor(R.color.Trans));
        }

        startA();

    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);

        c.setDrawFilter(mDrawFilter);

        for (int i=0; i<360; i+=3)
        {
            c.drawArc(mR, i, 1, false, mPaintRing);
        }

        if (mAAngle != -1)
        {
            for (int i=-90; i<mAAngle-90; i+=3)
            {
                c.drawArc(mR, i, 1, false, mPaintRingAnim);
            }
        }
    }


    @Override
    protected void onSizeChanged(int width,int height,int oldHeight, int oldWidth){

        super.onSizeChanged(height, width, oldHeight, oldWidth);

        mTotalWidth = width;
        mTotalHeight = height;

        mHeartBeatWidth = width - mHeartPaintW*2 - 40;

        x = width/2;
        y = height/2;

        mRadius = width/2 - mHeartPaintW/2;
        mR = new RectF(x - mRadius, y - mRadius, x + mRadius, y + mRadius);

    }



    private volatile boolean StopHeartBeatFlag = false;
    private volatile boolean StartHeartBeatFlag = false;


    private void startRingA(){
        mAAngle=0;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while(mAAngle < 360)
                {
                    mAAngle++;
                    try
                    {
                        Thread.sleep(30);
                    }

                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
                mAAngle = -1;
                stopA();
            }
        }).start();
    }


    public ViewRing(Context c, AttributeSet a) {

        super(c, a);

        mC = c;

        initial();
    }


    public ViewRing(Context c, AttributeSet a, int defSAttr) {

        super(c, a, defSAttr);

        mC = c;

        initial();
    }


    public ViewRing(Context c) {

        super(c);

        mC = c;

        initial();
    }

    public void startA(){

        startRingA();
    }

    public void stopA(){

        StartHeartBeatFlag = false;
        StopHeartBeatFlag = true;

    }
}
