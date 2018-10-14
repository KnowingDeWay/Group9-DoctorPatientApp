package com.softwareapp.group9.doctorpatientapp.consultdoctor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;
import com.softwareapp.group9.doctorpatientapp.R;

public class HeartRate extends AppCompatActivity {
    //ViewRing mviewring;
    Button saveBtn;

    boolean camPermission = false;
    private static final AtomicBoolean process = new AtomicBoolean(false);
    private static final String TAG = "HeartRate";

    private static SurfaceHolder previewH = null;
    private static SurfaceView previewV = null;

    private static TextView text = null;
    private static Camera cam = null;
    private static View photo = null;

    private static PowerManager.WakeLock w = null;

    public static enum TYPE {
        RED, GREEN
    };

    private static TYPE gType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return gType;
    }

    private static long startT = 0;
    private static double beats = 0;

    private static int beatIndex = 0;
    private static final int beatASize = 3;
    private static final int[] beatArray = new int[beatASize];


    private static final int AVERAGE_SIZE = 5;
    private static final int[] AVERAGE_ARRAY = new int[AVERAGE_SIZE];

    private static int avgIndex = 0;





    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartrate);

        //mviewring = findViewById(R.id.viewring);
        saveBtn = findViewById(R.id.saveBtn);

        previewV = findViewById(R.id.preview);
        previewH = previewV.getHolder();
        previewH.addCallback(surfaceCb);
        previewH.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        text = findViewById(R.id.text);
        photo = findViewById(R.id.image);

        PowerManager powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
        w = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "Don'tDimScreen");

        if(ContextCompat.checkSelfPermission(HeartRate.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);


        }
        else
        {
            Log.i("TEST","Granted");
        }



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public void onResume(){
        super.onResume();

        w.acquire();


        if(camPermission)
        {
            cam = Camera.open();
            startT = System.currentTimeMillis();
        }



    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();

        w.release();

        if(camPermission) {
            cam.setPreviewCallback(null);

            cam.stopPreview();

            cam.release();

            cam = null;
        }
    }



    @Override
    public void onConfigurationChanged(Configuration c){
        super.onConfigurationChanged(c);
    }


    private static Camera.PreviewCallback pCallback=new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {

            if (data == null)
            {
                throw new NullPointerException();
            }
            Camera.Size size = camera.getParameters().getPreviewSize();

            if (size == null)
            {
                throw new NullPointerException();
            }

            if (!process.compareAndSet(false, true))
            {
                return;
            }



            int avgArrayCnt = 0;
            int avgArrayAvg = 0;


            for (int averageArray : AVERAGE_ARRAY)
            {
                if (averageArray > 0)
                {
                    avgArrayAvg += averageArray;
                    avgArrayCnt++;
                }
            }

            int h = size.height;
            int w = size.width;


            int averageImage = ImageProcess.decodeToRedAvg(h, w, data.clone());
            int rollingAvg = (avgArrayCnt > 0) ? (avgArrayAvg / avgArrayCnt) : 0;

            if (averageImage == 255 ||averageImage == 0)
            {
                process.set(false);
                return;
            }

            TYPE nType = gType;

            if (nType != gType)
            {
                gType = nType;
                photo.postInvalidate();
            }

            if (averageImage > rollingAvg)
            {
                nType = TYPE.GREEN;
            }

            else if (averageImage < rollingAvg)
            {
                nType = TYPE.RED;
                if (nType != gType)
                {
                    beats++;
                }

            }

            if (avgIndex == AVERAGE_SIZE)
            {
                avgIndex = 0;
            }

            if (averageImage == 255 ||averageImage == 0)
            {
                process.set(false);
                return;
            }

            AVERAGE_ARRAY[avgIndex] = averageImage;

            avgIndex++;


            long eTime = System.currentTimeMillis();
            double totalTimeSec = (eTime - startT) / 1000d;

            if (totalTimeSec > 9)
            {
                double bps = (beats / totalTimeSec);
                int dpm = (int) (bps * 60d);
                int beatACnt = 0;
                int beatAAvg = 0;


                if (dpm > 180 || dpm < 30)
                {
                    startT = System.currentTimeMillis();
                    beats = 0;
                    process.set(false);
                    return;
                }

                if (beatASize == beatIndex)
                {
                    beatIndex = 0;
                }

                beatArray[beatIndex] = dpm;
                beatIndex++;

                for (int i = 0; i < beatArray.length; i++)
                {
                    if (beatArray[i] > 0)
                    {
                        beatAAvg += beatArray[i];
                        beatACnt++;
                    }
                }

                int avgBeat = (beatAAvg / beatACnt);

                text.setText(String.valueOf(avgBeat));
                startT = System.currentTimeMillis();
                beats = 0;
                stopCamera();
            }
            process.set(false);
        }
    };


    private static Camera.Size smallestPreviewSz(int w, int h, Camera.Parameters parameters ){
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes())
        {
            if (size.height <= h  && size.width <= w)
            {
                if (result != null)
                {
                    int nArea = size.height * size.width;
                    int rArea = result.height * result.width;


                    if (rArea > nArea)
                    {
                        result = size;
                    }

                }

                else
                {
                    result = size;
                }
            }
        }
        return result;
    }


    private static SurfaceHolder.Callback surfaceCb = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder sH) {
            try
            {
                cam.setPreviewCallback(pCallback);
                cam.setPreviewDisplay(previewH);

            }

            catch (Throwable throwable)
            {
                Log.e(TAG,"Exceptions are in setPreviewDisplay method",throwable);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int f, int h, int w) {


            if(cam != null) {
                Camera.Parameters para = cam.getParameters();
                para.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size size = smallestPreviewSz(h, w, para);


                if (size != null) {
                    para.setPreviewSize(size.height, size.width);
                    Log.d(TAG, "Using height = " + size.height + "width= " + size.width);
                }

                cam.setParameters(para);
                cam.startPreview();
            }
        }

    };

    public static void startCamera()
    {
        cam.startPreview();
        startT=System.currentTimeMillis();
    }

    public static void stopCamera()
    {
        cam.stopPreview();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("HeartRate", "Request Permission");

        if(requestCode == 2 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            Log.d("HeartRate", "Permission Denied");
        }
        else
        {
            Log.d("HeartRate", "Permission Allowed");
            camPermission = true;
        }


    }
}

