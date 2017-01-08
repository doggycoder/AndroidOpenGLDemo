package edu.wuwang.opengl.obj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * Created by wuwang on 2017/1/7
 */

public class ObjReader {

    public static void read(InputStream stream,Obj3D obj3D){
        ArrayList<Float> alv=new ArrayList<Float>();//原始顶点坐标列表
        ArrayList<Float> alvResult=new ArrayList<Float>();//结果顶点坐标列表
        try{
            InputStreamReader isr=new InputStreamReader(stream);
            BufferedReader br=new BufferedReader(isr);
            String temps=null;
            while((temps=br.readLine())!=null)
            {
                String[] tempsa=temps.split("[ ]+");
                if(tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                }  else if(tempsa[0].trim().equals("f")) {//此行为三角形面
                    int a=Integer.parseInt(tempsa[1])-1;
                    int b=Integer.parseInt(tempsa[2])-1;
                    int c=Integer.parseInt(tempsa[3])-1;
                    int d=Integer.parseInt(tempsa[4])-1;

                    alvResult.add(alv.get(a*3));
                    alvResult.add(alv.get(a*3+1));
                    alvResult.add(alv.get(a*3+2));
                    alvResult.add(alv.get(b*3));
                    alvResult.add(alv.get(b*3+1));
                    alvResult.add(alv.get(b*3+2));
                    alvResult.add(alv.get(c*3));
                    alvResult.add(alv.get(c*3+1));
                    alvResult.add(alv.get(c*3+2));
                    alvResult.add(alv.get(a*3));
                    alvResult.add(alv.get(a*3+1));
                    alvResult.add(alv.get(a*3+2));
                    alvResult.add(alv.get(c*3));
                    alvResult.add(alv.get(c*3+1));
                    alvResult.add(alv.get(c*3+2));
                    alvResult.add(alv.get(d*3));
                    alvResult.add(alv.get(d*3+1));
                    alvResult.add(alv.get(d*3+2));
                }
            }

            //生成顶点数组
            int size=alvResult.size();
            float[] vXYZ=new float[size];
            for(int i=0;i<size;i++){
                vXYZ[i]=alvResult.get(i);
            }
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*size);
            byteBuffer.order(ByteOrder.nativeOrder());
            obj3D.vert=byteBuffer.asFloatBuffer();
            obj3D.vert.put(vXYZ);
            obj3D.vert.position(0);
            obj3D.vertCount=size/3;
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static class Obj3D{
        public FloatBuffer vert;
        public int vertCount;
    }

}
