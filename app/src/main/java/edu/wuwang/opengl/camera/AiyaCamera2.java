/*
 *
 * AiyaCamera2.java
 * 
 * Created by Wuwang on 2016/11/22
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import java.util.Arrays;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;

/**
 * Description:
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AiyaCamera2 implements IAiyaCamera {

    private Context mContext;
    private HandlerThread mCameraThread;
    private Handler mCameraHandler;

    private SurfaceTexture mPreviewTexture;

    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private CaptureRequest.Builder mCaptureRequestBuilder;

    private boolean isUserWantPreview=false;

    public AiyaCamera2(Context context) {
        this.mContext = context;
        mCameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);
    }

    private void createThread() {
        if (mCameraThread == null) {
            mCameraThread = new HandlerThread("Camera Thread");
            mCameraThread.start();
            mCameraHandler = new Handler(mCameraThread.getLooper());
        }
    }

    public void setPreviewTexture(SurfaceTexture texture){
        this.mPreviewTexture=texture;
    }

    @Override
    public void setConfig(Config config) {

    }

    @Override
    public void setOnPreviewFrameCallback(PreviewFrameCallback callback) {

    }

    @Override
    public void preview() {
        isUserWantPreview=true;
        try {
            createCameraCaptureSession();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Point getPreviewSize() {
        return new Point(480,640);
    }

    @Override
    public Point getPictureSize() {
        return null;
    }

    @Override
    public boolean close() {
        return false;
    }

    @Override
    public void open(int cameraId) {
        createThread();
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager .PERMISSION_GRANTED) {
        }
        try {
            mCameraManager.openCamera(cameraId + "", mStateCallback, mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private CameraDevice.StateCallback mStateCallback=new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice=camera;
            try {
                createCameraCaptureSession();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            mCameraDevice=null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    private void createCameraCaptureSession() throws CameraAccessException {
        if(isUserWantPreview&&mPreviewTexture!=null&&mCameraDevice!=null){
            mCaptureRequestBuilder=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface=new Surface(mPreviewTexture);
            mCaptureRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface),mSessionPreviewCallback,
                mCameraHandler);
        }
    }

    private CameraCaptureSession.StateCallback mSessionPreviewCallback=new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            mCameraCaptureSession=session;
            try {
                session.setRepeatingRequest(mCaptureRequestBuilder.build(),mCaptureCallback,
                    mCameraHandler);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {
        }
    };

    private CameraCaptureSession.CaptureCallback mCaptureCallback=new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            mCameraCaptureSession=session;
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
            mCameraCaptureSession=session;
            super.onCaptureProgressed(session, request, partialResult);
        }

    };

}
