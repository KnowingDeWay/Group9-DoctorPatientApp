package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.softwareapp.group9.doctorpatientapp.R;

public class HeartBeat extends View {

    private static int parentH = 0;
    private static int parentW = 0;

    private static final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final Matrix m = new Matrix();

    private static Bitmap redB = null;
    private static Bitmap greenB = null;


    public HeartBeat(Context c) {

        super(c);

        redB = BitmapFactory.decodeResource(c.getResources(), R.drawable.heart);
        greenB = BitmapFactory.decodeResource(c.getResources(), R.drawable.blank_heart);

    }

    public HeartBeat(Context c, AttributeSet attributeSet) {

        super(c, attributeSet);

        redB = BitmapFactory.decodeResource(c.getResources(), R.drawable.heart);
        greenB = BitmapFactory.decodeResource(c.getResources(), R.drawable.blank_heart);

    }

    @Override
    protected void onDraw(Canvas c) {
        if (c == null) {
            throw new NullPointerException();
        }

        Bitmap b = null;

        if (HeartRate.TYPE.GREEN == HeartRate.getCurrent()) {
            b = greenB;
        } else {
            b = redB;

        }

        int parentY = parentH / 2;
        int parentX = parentW / 2;

        int bitmapY = b.getHeight() / 2;
        int bitmapX = b.getWidth() / 2;

        int centerY = parentY - bitmapY;
        int centerX = parentX - bitmapX;


        m.reset();
        m.postTranslate(centerX, centerY);
        c.drawBitmap(b, m, paint);
    }

    @Override
    protected void onMeasure(int hMeasureSpec, int wMeasureSpec) {
        super.onMeasure(hMeasureSpec, wMeasureSpec);

        parentH = View.MeasureSpec.getSize(hMeasureSpec);
        parentW = View.MeasureSpec.getSize(wMeasureSpec);

        setMeasuredDimension(parentH, parentW);
    }
}
