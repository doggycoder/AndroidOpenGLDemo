/*
 *
 * AiyaCamera.java
 * 
 * Created by Wuwang on 2016/11/18
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;

/**
 * Description:
 */
public class AiyaCamera implements IAiyaCamera{

    private IAiyaCamera.Config mConfig;
    private Camera mCamera;

    private Camera.Size picSize;
    private Camera.Size preSize;

    private Point mPicSize;
    private Point mPreSize;

    public AiyaCamera(){
        this.mConfig=new IAiyaCamera.Config();
        mConfig.minPreviewWidth=720;
        mConfig.minPictureWidth=720;
        mConfig.rate=1.778f;
    }

    public void open(int cameraId) {
        mCamera=Camera.open(cameraId);

        if(mCamera!=null){
            Camera.Parameters param=mCamera.getParameters();
            picSize=getPropPictureSize(param.getSupportedPictureSizes(),mConfig.rate,
                mConfig.minPictureWidth);
            preSize=getPropPreviewSize(param.getSupportedPreviewSizes(),mConfig.rate,mConfig
                .minPreviewWidth);
            param.setPictureSize(picSize.width,picSize.height);
            param.setPreviewSize(preSize.width,preSize.height);
            if(param.getMaxNumFocusAreas() > 0){
                Rect areaRect1 = new Rect(-50, -50, 50, 50);
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(areaRect1, 1000));
                param.setFocusAreas(focusAreas);
            }
            // if the camera support setting of metering area.
            if (param.getMaxNumMeteringAreas() > 0){
                List<Camera.Area> meteringAreas = new ArrayList<>();
                Rect areaRect1 = new Rect(-100, -100, 100, 100);
                meteringAreas.add(new Camera.Area(areaRect1, 1000));
                param.setMeteringAreas(meteringAreas);
            }
//            List<int[]> al=param.getSupportedPreviewFpsRange();
//            for (int[] a:al){
//                Log.e("previewSize","size->"+a[0]+"/"+a[1]);
//            }
//            param.setPreviewFpsRange(30000,30000);
//            param.setRecordingHint(true);
            mCamera.setParameters(param);
            Camera.Size pre=param.getPreviewSize();
            Camera.Size pic=param.getPictureSize();
            mPicSize=new Point(pic.height,pic.width);
            mPreSize=new Point(pre.height,pre.width);
            Log.e("wuwang","camera previewSize:"+mPreSize.x+"/"+mPreSize.y);
        }
    }

    public Camera getCamera(){
        return mCamera;
    }

    public void setPreviewTexture(SurfaceTexture texture){
        if(mCamera!=null){
            try {
                mCamera.setPreviewTexture(texture);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setConfig(IAiyaCamera.Config config) {
        this.mConfig=config;
    }

    public void preview() {
        if(mCamera!=null){
            mCamera.startPreview();
        }
    }


    public boolean switchTo(int cameraId) {
        close();
        open(cameraId);
        return false;
    }

    public boolean close() {
        if(mCamera!=null){
            try{
                mCamera.stopPreview();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                mCamera.release();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public Point getPreviewSize() {
        return mPreSize;
    }

    public Point getPictureSize() {
        return mPicSize;
    }

    public void setOnPreviewFrameCallback(final IAiyaCamera.PreviewFrameCallback callback) {
        if(mCamera!=null){
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y);
                }
            });
        }
    }

    public void addBuffer(byte[] buffer){
        if(mCamera!=null){
            mCamera.addCallbackBuffer(buffer);
        }
    }

    public void setOnPreviewFrameCallbackWithBuffer(final AiyaCamera.PreviewFrameCallback callback) {
        if(mCamera!=null){
            Log.e("wuwang","Camera set CallbackWithBuffer");
            mCamera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    callback.onPreviewFrame(data,mPreSize.x,mPreSize.y);
                }
            });
        }
    }


    private Camera.Size getPropPreviewSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    private Camera.Size getPropPictureSize(List<Camera.Size> list, float th, int minWidth){
        Collections.sort(list, sizeComparator);

        int i = 0;
        for(Camera.Size s:list){
            if((s.height >= minWidth) && equalRate(s, th)){
                break;
            }
            i++;
        }
        if(i == list.size()){
            i = 0;
        }
        return list.get(i);
    }

    private static boolean equalRate(Camera.Size s, float rate){
        float r = (float)(s.width)/(float)(s.height);
        if(Math.abs(r - rate) <= 0.03)
        {
            return true;
        }
        else{
            return false;
        }
    }

    private Comparator<Camera.Size> sizeComparator=new Comparator<Camera.Size>(){
        public int compare(Camera.Size lhs, Camera.Size rhs) {
            // TODO Auto-generated method stub
            if(lhs.height == rhs.height){
                return 0;
            }
            else if(lhs.height > rhs.height){
                return 1;
            }
            else{
                return -1;
            }
        }
    };


interface PreviewFrameCallback{
    void onPreviewFrame(byte[] bytes, int width, int height);
}

}
