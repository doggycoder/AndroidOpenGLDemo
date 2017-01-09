package edu.wuwang.opengl.obj;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by wuwang on 2017/1/7
 */

public class ObjReader {

    public static void read(InputStream stream,Obj3D obj3D){
        ArrayList<Float> alv=new ArrayList<Float>();//原始顶点坐标列表
        ArrayList<Float> alvResult=new ArrayList<Float>();//结果顶点坐标列表
        ArrayList<Float> norlArr=new ArrayList<>();
        float[] ab=new float[3],bc=new float[3],norl=new float[3];
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
                    //abc和acd两个三角形组成的四边形

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

                    //用面法向量策略。按理说点法向量更适合这种光滑的3D模型，但是计算起来太复杂了，so
                    //既然主要讲3D模型加载，就先用面法向量策略来吧
                    //通常3D模型里面会包含法向量信息的。
                    //法向量的计算，ABC三个空间点，他们的法向量为向量AB与向量BC的外积，所以有：
                    for (int i=0;i<3;i++){
                        ab[i]=alv.get(a*3+i)-alv.get(b*3+i);
                        bc[i]=alv.get(b*3+i)-alv.get(c*3+i);
                    }
                    norl[0]=ab[1]*bc[2]-ab[2]*bc[1];
                    norl[1]=ab[2]*bc[0]-ab[0]*bc[2];
                    norl[2]=ab[0]*bc[1]-ab[1]*bc[0];
                    for (int i=0;i<6;i++){
                        norlArr.add(norl[0]);
                        norlArr.add(norl[1]);
                        norlArr.add(norl[2]);
                    }
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
            int vbSize=norlArr.size();
            float[] vbArr=new float[size];
            for(int i=0;i<size;i++){
                vbArr[i]=norlArr.get(i);
            }
            ByteBuffer vb=ByteBuffer.allocateDirect(4*vbSize);
            vb.order(ByteOrder.nativeOrder());
            obj3D.vertNorl=vb.asFloatBuffer();
            obj3D.vertNorl.put(vbArr);
            obj3D.vertNorl.position(0);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static class Obj3D{
        public FloatBuffer vert;
        public int vertCount;
        public FloatBuffer vertNorl;
    }

}
