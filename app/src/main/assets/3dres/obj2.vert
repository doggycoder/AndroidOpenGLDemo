attribute vec3 vPosition;
attribute vec2 vCoord;
uniform mat4 vMatrix;

varying vec2 textureCoordinate;

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
    gl_Position = vMatrix*vec4(vPosition,1);
    textureCoordinate = vCoord;

   vec4 at=vec4(1.0,1.0,1.0,1.0);   //光照强度
   vec3 pos=vec3(50.0,200.0,50.0);      //光照位置
   vDiffuse=pointLight(vNormal,pos,at);
}