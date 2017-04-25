package com.example.jcliu.firstopencvapp;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView iv;
    Mat srcMat;

    private BaseLoaderCallback mOpenCVCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                case LoaderCallbackInterface.SUCCESS:
                    Log.d("OpenCV", "OpenCV sucess");
                    try{
                        srcMat = Utils.loadResource(MainActivity.this, R.raw.cameraman);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bm = Bitmap.createBitmap(srcMat.cols(), srcMat.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(srcMat, bm);
                    iv.setImageBitmap(bm);
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)findViewById(R.id.imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!OpenCVLoader.initDebug()){
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mOpenCVCallBack);
        }
        else
            mOpenCVCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);

    }
}
