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

    public MtlInfo mtl;

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

}
