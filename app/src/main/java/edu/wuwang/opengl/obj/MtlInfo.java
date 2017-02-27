package edu.wuwang.opengl.obj;

/**
 * Created by wuwang on 2017/2/22
 */

public class MtlInfo {
    public String newmtl;
    public float[] Ka=new float[3];     //阴影色
    public float[] Kd=new float[3];     //固有色
    public float[] Ks=new float[3];     //高光色
    public float[] Ke=new float[3];     //
    public float Ns;                    //shininess
    public String map_Kd;               //固有纹理贴图
    public String map_Ks;               //高光纹理贴图
    public String map_Ka;               //阴影纹理贴图

    //denotes the illumination model used by the material.
    // illum = 1 indicates a flat material with no specular highlights,
    // so the value of Ks is not used.
    // illum = 2 denotes the presence of specular highlights,
    // and so a specification for Ks is required.
    public int illum;
}
