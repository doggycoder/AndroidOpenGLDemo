/*
 *
 * FastDrawerHelper.java
 * 
 * Created by Wuwang on 2016/11/17
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.wuwang.easygpu.core;

import java.io.InputStream;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

/**
 * Description:
 */
public class Gl2Utils {

    public static final int TYPE_FITXY=0;
    public static final int TYPE_CENTERCROP=1;
    public static final int TYPE_CENTERINSIDE=2;
    public static final int TYPE_FITSTART=3;
    public static final int TYPE_FITEND=4;

    private static boolean debug=false;

    private Gl2Utils(){

    }

    public static void setDebug(boolean debug){
        Gl2Utils.debug=debug;
    }

    /**
     * 根据图片宽高和显示宽高获取矩阵
     * @param matrix 接受结果的矩阵
     * @param type
     * @param imgWidth
     * @param imgHeight
     * @param viewWidth
     * @param viewHeight
     */
    public static void getMatrix(float[] matrix,int type,int imgWidth,int imgHeight,int viewWidth,
                                 int viewHeight){
        if(imgHeight>0&&imgWidth>0&&viewWidth>0&&viewHeight>0){
            float[] projection=new float[16];
            float[] camera=new float[16];
            if(type==TYPE_FITXY){
                Matrix.orthoM(projection,0,-1,1,-1,1,1,3);
                Matrix.setLookAtM(camera,0,0,0,1,0,0,0,0,1,0);
                Matrix.multiplyMM(matrix,0,projection,0,camera,0);
            }
            float sWhView=(float)viewWidth/viewHeight;
            float sWhImg=(float)imgWidth/imgHeight;
            if(sWhImg>sWhView){
                switch (type){
                    case TYPE_CENTERCROP:
                        Matrix.orthoM(projection,0,-sWhView/sWhImg,sWhView/sWhImg,-1,1,1,3);
                        break;
                    case TYPE_CENTERINSIDE:
                        Matrix.orthoM(projection,0,-1,1,-sWhImg/sWhView,sWhImg/sWhView,1,3);
                        break;
                    case TYPE_FITSTART:
                        Matrix.orthoM(projection,0,-1,1,1-2*sWhImg/sWhView,1,1,3);
                        break;
                    case TYPE_FITEND:
                        Matrix.orthoM(projection,0,-1,1,-1,2*sWhImg/sWhView-1,1,3);
                        break;
                }
            }else{
                switch (type){
                    case TYPE_CENTERCROP:
                        Matrix.orthoM(projection,0,-1,1,-sWhImg/sWhView,sWhImg/sWhView,1,3);
                        break;
                    case TYPE_CENTERINSIDE:
                        Matrix.orthoM(projection,0,-sWhView/sWhImg,sWhView/sWhImg,-1,1,1,3);
                        break;
                    case TYPE_FITSTART:
                        Matrix.orthoM(projection,0,-1,2*sWhView/sWhImg-1,-1,1,1,3);
                        break;
                    case TYPE_FITEND:
                        Matrix.orthoM(projection,0,1-2*sWhView/sWhImg,1,-1,1,1,3);
                        break;
                }
            }
            Matrix.setLookAtM(camera,0,0,0,1,0,0,0,0,1,0);
            Matrix.multiplyMM(matrix,0,projection,0,camera,0);
        }
    }

    /**
     * 将矩阵m进行旋转
     * @param m 原矩阵
     * @param angle 旋转角度
     * @return m
     */
    public static float[] rotate(float[] m,float angle){
        Matrix.rotateM(m,0,angle,0,0,1);
        return m;
    }

    /**
     * 将矩阵m进行翻转
     * @param m 原矩阵
     * @param x 是否x轴翻转
     * @param y 是否y轴翻转
     * @return m
     */
    public static float[] flip(float[] m,boolean x,boolean y){
        if(x||y){
            Matrix.scaleM(m,0,x?-1:1,y?-1:1,1);
        }
        return m;
    }

    /**
     * 获取一个新的单位矩阵
     * @return 4*4单位矩阵
     */
    public static float[] getOriginalMatrix(){
        return new float[]{
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1
        };
    }

    /**
     * 打印错误
     * @param code code
     * @param index 附加信息
     */
    public static void glError(int code,Object index){
        if(debug&&code!=0){
            Log.e("Filter","glError:"+GLES20.glGetError()+". Info:"+index);
        }
    }

    /**
     * 湖区Assets中指定文本文件的内容
     * @param mRes Resource实例
     * @param path 文本资源路径
     * @return 文本内容
     */
    public static String getAssetsText(Resources mRes, String path){
        StringBuilder result=new StringBuilder();
        try{
            InputStream is=mRes.getAssets().open(path);
            int ch;
            byte[] buffer=new byte[1024];
            while (-1!=(ch=is.read(buffer))){
                result.append(new String(buffer,0,ch));
            }
        }catch (Exception e){
            return null;
        }
        return result.toString().replaceAll("\\r\\n","\n");
    }

    /**
     * 创建GL程序
     * @param vertexSource 顶点Shader
     * @param fragmentSource 片元Shader
     * @return GL程序ID
     */
    public static int createGlProgram(String vertexSource, String fragmentSource){
        int vertex=loadShader(GLES20.GL_VERTEX_SHADER,vertexSource);
        if(vertex==0)return 0;
        int fragment=loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentSource);
        if(fragment==0)return 0;
        int program= GLES20.glCreateProgram();
        if(program!=0){
            GLES20.glAttachShader(program,vertex);
            GLES20.glAttachShader(program,fragment);
            GLES20.glLinkProgram(program);
            int[] linkStatus=new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS,linkStatus,0);
            if(linkStatus[0]!= GLES20.GL_TRUE){
                glError(1,"Could not link program:"+ GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program=0;
            }
        }
        return program;
    }

    /**
     * 加载Shader
     * @param shaderType shader的类型，只能为{@link GLES20#GL_FRAGMENT_SHADER}
     * 或{@link GLES20#GL_VERTEX_SHADER}
     * @param source shader内容
     * @return shaderID
     */
    public static int loadShader(int shaderType,String source){

        int shader= GLES20.glCreateShader(shaderType);
        if(0!=shader){
            GLES20.glShaderSource(shader,source);
            GLES20.glCompileShader(shader);
            int[] compiled=new int[1];
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS,compiled,0);
            if(compiled[0]==0){
                glError(1,"Could not compile shader:"+shaderType);
                glError(1,"GLES20 Error:"+ GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader=0;
            }
        }
        return shader;
    }


}
