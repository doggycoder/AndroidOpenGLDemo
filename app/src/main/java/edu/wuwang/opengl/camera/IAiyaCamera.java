/*
 *
 * IAiyaCamera.java
 * 
 * Created by Wuwang on 2016/11/22
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.camera;

import android.graphics.Point;
import android.graphics.SurfaceTexture;

/**
 * Description:
 */
public interface IAiyaCamera {

    void open(int cameraId);

    void setPreviewTexture(SurfaceTexture texture);

    void setConfig(Config config);

    void setOnPreviewFrameCallback(PreviewFrameCallback callback);

    void preview();

    Point getPreviewSize();

    Point getPictureSize();

    boolean close();

    class Config{
        float rate; //宽高比
        int minPreviewWidth;
        int minPictureWidth;
    }

    interface PreviewFrameCallback{
        void onPreviewFrame(byte[] bytes, int width, int height);
    }

}
