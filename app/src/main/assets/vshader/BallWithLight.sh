uniform mat4 vMatrix;        //总变换矩阵
uniform mat4 uMMatrix;          //变换矩阵
uniform vec3 uLightLocation;        //光源位置
uniform vec3 uCamera;           //摄像机位置
attribute vec3 vPosition;       //顶点位置
attribute vec3 vNormal;         //法向量
varying vec4 vDiffuse;          //用于传递给片元着色器的散射光最终强度


//返回散射光强度
vec4 pointLight(vec3 normal,vec3 lightLocation,vec4 lightDiffuse){
    //变换后的法向量
    vec3 newTarget=normalize((vMatrix*vec4(normal+vPosition,1)).xyz-(vMatrix*vec4(vPosition,1)).xyz);
    //表面点与光源的方向向量
    vec3 vp=normalize(lightLocation-(vMatrix*vec4(vPosition,1)).xyz);
    return lightDiffuse*max(0.0,dot(newTarget,vp));
}

void main(){
   gl_Position = vMatrix * vec4(vPosition,1); //根据总变换矩阵计算此次绘制此顶点位置

   vec4 at=vec4(1.0,1.0,1.0,1.0);   //光照强度
   vec3 pos=vec3(100.0,80.0,80.0);      //光照位置
   vDiffuse=pointLight(normalize(vPosition),pos,at);
}