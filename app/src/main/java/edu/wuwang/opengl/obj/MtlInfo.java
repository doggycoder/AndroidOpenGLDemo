package edu.wuwang.opengl.obj;

/**
 * Created by wuwang on 2017/2/22
 */

public class MtlInfo {
    public String newmtl;
    public float[] Ka=new float[3];     //ambient color
    public float[] Kd=new float[3];     //diffuse color
    public float[] Ks=new float[3];     //specular color
    public float[] Ke=new float[3];     //
    public float Ns;                    //shininess
    public String textureName;          //textureName

    //denotes the illumination model used by the material.
    // illum = 1 indicates a flat material with no specular highlights,
    // so the value of Ks is not used.
    // illum = 2 denotes the presence of specular highlights,
    // and so a specification for Ks is required.
    public int illum;
}
