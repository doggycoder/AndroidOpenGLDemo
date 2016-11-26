/*
 *
 * FastDrawerHelper.java
 * 
 * Created by Wuwang on 2016/11/17
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package edu.wuwang.opengl.utils;

import android.opengl.Matrix;

/**
 * Description:
 */
public class Gl2Utils {

    private Gl2Utils(){

    }

    public static void getShowMatrix(float[] matrix,int imgWidth,int imgHeight,int viewWidth,int
        viewHeight){
        if(imgHeight>0&&imgWidth>0&&viewWidth>0&&viewHeight>0){
            float sWhView=(float)viewWidth/viewHeight;
            float sWhImg=(float)imgWidth/imgHeight;
            float[] projection=new float[16];
            float[] camera=new float[16];
            if(sWhImg>sWhView){
                Matrix.orthoM(projection,0,-sWhView/sWhImg,sWhView/sWhImg,-1,1,1,3);
            }else{
                Matrix.orthoM(projection,0,-1,1,-sWhImg/sWhView,sWhImg/sWhView,1,3);
            }
            Matrix.setLookAtM(camera,0,0,0,1,0,0,0,0,1,0);
            Matrix.multiplyMM(matrix,0,projection,0,camera,0);
        }
    }

    public static float[] rotate(float[] m,float angle){
        Matrix.rotateM(m,0,angle,0,0,1);
        return m;
    }

    public static float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

    public static float[] getOriginalMatrix(){
        return new float[]{
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
        };
    }

}
