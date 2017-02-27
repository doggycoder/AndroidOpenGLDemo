package edu.wuwang.opengl.obj;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by wuwang on 2017/2/22
 */

public class Obj3D {
    public FloatBuffer vert;
    public int vertCount;
    public FloatBuffer vertNorl;
    public FloatBuffer vertTexture;

    public MtlInfo mtl;

    private ArrayList<Float> tempVert;
    private ArrayList<Float> tempVertNorl;
    public ArrayList<Float> tempVertTexture;

    public int textureSMode;
    public int textureTMode;

    public void addVert(float d){
        if(tempVert==null){
            tempVert=new ArrayList<>();
        }
        tempVert.add(d);
    }

    public void addVertTexture(float d){
        if(tempVertTexture==null){
            tempVertTexture=new ArrayList<>();
        }
        tempVertTexture.add(d);
    }

    public void addVertNorl(float d){
        if(tempVertNorl==null){
            tempVertNorl=new ArrayList<>();
        }
        tempVertNorl.add(d);
    }

    public void dataLock(){
        if(tempVert!=null){
            setVert(tempVert);
            tempVert.clear();
            tempVert=null;
        }
        if(tempVertTexture!=null){
            setVertTexture(tempVertTexture);
            tempVertTexture.clear();
            tempVertTexture=null;
        }
        if(tempVertNorl!=null){
            setVertNorl(tempVertNorl);
            tempVertNorl.clear();
            tempVertNorl=null;
        }
    }

    public void setVert(ArrayList<Float> data){
        int size=data.size();
        ByteBuffer buffer=ByteBuffer.allocateDirect(size*4);
        buffer.order(ByteOrder.nativeOrder());
        vert=buffer.asFloatBuffer();
        for (int i=0;i<size;i++){
            vert.put(data.get(i));
        }
        vert.position(0);
        vertCount=size/3;
    }

    public void setVertNorl(ArrayList<Float> data){
        int size=data.size();
        ByteBuffer buffer=ByteBuffer.allocateDirect(size*4);
        buffer.order(ByteOrder.nativeOrder());
        vertNorl=buffer.asFloatBuffer();
        for (int i=0;i<size;i++){
            vertNorl.put(data.get(i));
        }
        vertNorl.position(0);
    }

    public void setVertTexture(ArrayList<Float> data){
        int size=data.size();
        ByteBuffer buffer=ByteBuffer.allocateDirect(size*4);
        buffer.order(ByteOrder.nativeOrder());
        vertTexture=buffer.asFloatBuffer();
        for (int i=0;i<size;){
            vertTexture.put(data.get(i));
            i++;
            vertTexture.put(data.get(i));
            i++;
        }
        vertTexture.position(0);
    }

}
