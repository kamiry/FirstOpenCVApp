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
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import static java.lang.Math.floor;

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
                    Log.d("OpenCV", "no. channels = "+srcMat.channels()+ ", size=" + srcMat.size());
                    // process image here
                    Mat dstMat = new Mat(srcMat.size(), CvType.CV_8UC1);
                    // custom kernel
                    Mat kernel = new Mat(3,3,CvType.CV_8SC1);
                    kernel.put(0,0,0,-1,0,-1,5,-1,0,-1,0); // sharpening
                    //kernel.put(0,0,0,-1,0,-1,4,-1,0,-1,0);
                    // 2D filter
                    Imgproc.filter2D(srcMat, dstMat, srcMat.depth(), kernel);
                    // Thresholding
                    /*int size = (int)srcMat.total()*srcMat.channels();
                    byte[] buff = new byte[size];
                    srcMat.get(0,0,buff);
                    for(int i=0;i<size;i++){
                        buff[i]= (byte) (((char)buff[i]/128)*128);
                    }
                    dstMat.put(0,0,buff);
                    */
                    // write to bmp and show
                    Bitmap bm = Bitmap.createBitmap(dstMat.cols(), dstMat.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(dstMat, bm);
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
