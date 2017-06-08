package edu.wuwang.opengl.filter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.io.IOException;

/**
 * Created by aiya on 2017/6/8.
 */

public class LookupFilter extends AFilter {

    private int mHMaskTexture;
    private int mHIntensity;
    private Bitmap bitmap;
    private int[] textures=new int[1];
    private float intensity;

    public LookupFilter(Resources mRes) {
        super(mRes);
    }

    @Override
    protected void onCreate() {
        createProgramByAssetsFile("lookup/lookup.vert","lookup/lookup.frag");
        mHMaskTexture=GLES20.glGetUniformLocation(mProgram,"maskTexture");
        mHIntensity=GLES20.glGetUniformLocation(mProgram,"intensity");
    }

    public void setIntensity(float intensity){
        this.intensity=intensity;
    }

    public void setFilterImage(Bitmap bitmap){
        this.bitmap=bitmap;
    }

    public void setFilterImage(String path){
        Bitmap bitmap=null;
        if(path.startsWith("assets/")){
            try {
                bitmap=BitmapFactory.decodeStream(mRes.getAssets().open(path.replaceFirst("assets/","")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            bitmap=BitmapFactory.decodeFile(path);
        }
        if(bitmap!=null){
            setFilterImage(bitmap);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height) {

    }

    @Override
    protected void onSetExpandData() {
        super.onSetExpandData();
        if(bitmap!=null&&!bitmap.isRecycled()){
            if(textures[0]==0){
                GLES20.glGenTextures(1,textures,0);
            }
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D,0,bitmap,0);
            //bitmap.recycle();
        }
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+getTextureType()+1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textures[0]);
        GLES20.glUniform1i(mHMaskTexture,getTextureType()+1);
        GLES20.glUniform1f(mHIntensity,intensity);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
